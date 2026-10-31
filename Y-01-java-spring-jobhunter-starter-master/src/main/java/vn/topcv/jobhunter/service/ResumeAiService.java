package vn.topcv.jobhunter.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.topcv.jobhunter.domain.Job;
import vn.topcv.jobhunter.domain.Skill;
import vn.topcv.jobhunter.domain.response.resume.ResAiResumeMatchDTO;
import vn.topcv.jobhunter.repository.JobRepository;

@Service
public class ResumeAiService {

	@Value("${topcv.upload-file.base-uri}")
	private String baseUri;

	@Value("${topcv.ai.enabled:false}")
	private boolean aiEnabled;

	@Value("${topcv.ai.provider:openai}")
	private String aiProvider;

	@Value("${topcv.ai.endpoint:}")
	private String aiEndpoint;

	@Value("${topcv.ai.api-key:}")
	private String aiApiKey;

	@Value("${topcv.ai.model:gpt-4o-mini}")
	private String aiModel;

	@Value("${topcv.ai.azure-deployment:}")
	private String azureDeployment;

	@Value("${topcv.ai.api-version:2024-02-01}")
	private String aiApiVersion;

	private final JobRepository jobRepository;

	public ResumeAiService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public ResAiResumeMatchDTO analyzeMatch(Long jobId, String fileName) {
		ResAiResumeMatchDTO result = new ResAiResumeMatchDTO();
		if (jobId == null || fileName == null || fileName.isBlank()) {
			result.setMatchScore(0);
			result.setMatchedSkills(List.of());
			result.setMissingSkills(List.of());
			result.setSummary("Thiếu thông tin vị trí công việc hoặc file CV để đánh giá.");
			result.setSuggestions(List.of("Tải lại CV và chọn vị trí ứng tuyển hợp lệ."));
			return result;
		}

		Job job = this.jobRepository.findById(jobId).orElse(null);
		if (job == null) {
			result.setMatchScore(0);
			result.setMatchedSkills(List.of());
			result.setMissingSkills(List.of());
			result.setSummary("Không tìm thấy vị trí công việc để chấm phù hợp.");
			result.setSuggestions(List.of("Vui lòng chọn vị trí tuyển dụng hợp lệ."));
			return result;
		}

		result.setJobId(job.getId());
		result.setJobName(job.getName());
		if (job.getCompany() != null) {
			result.setCompanyName(job.getCompany().getName());
		}

		String cvText = this.extractCvText(fileName);
		if (cvText == null || cvText.isBlank()) {
			result.setMatchScore(0);
			result.setMatchedSkills(List.of());
			result.setMissingSkills(List.of());
			result.setSummary("Không đọc được nội dung CV. Vui lòng tải lên file PDF/DOC/DOCX hợp lệ.");
			result.setSuggestions(List.of("Tải lại file CV ở định dạng PDF, DOC hoặc DOCX.", "Kiểm tra file không bị hỏng hoặc rỗng."));
			return result;
		}

		String normalizedCv = normalizeText(cvText);
		List<String> skillNames = this.getSkillNames(job);
		List<String> matchedSkills = new ArrayList<>();
		List<String> missingSkills = new ArrayList<>();

		for (String skill : skillNames) {
			if (skill == null || skill.isBlank()) {
				continue;
			}
			if (normalizedCv.contains(skill)) {
				matchedSkills.add(skill);
			} else {
				missingSkills.add(skill);
			}
		}

		int matchScore = 0;
		if (!skillNames.isEmpty()) {
			matchScore = (int) Math.round((matchedSkills.size() * 100.0) / skillNames.size());
		}

		if (skillNames.isEmpty()) {
			matchScore = this.calculateTextSimilarity(job, normalizedCv);
		}

		String summary = buildSummary(job.getName(), matchScore, matchedSkills, missingSkills);
		List<String> suggestions = buildSuggestions(missingSkills, matchedSkills);

		result.setMatchScore(matchScore);
		result.setMatchedSkills(matchedSkills);
		result.setMissingSkills(missingSkills);
		result.setSummary(summary);
		result.setSuggestions(suggestions);

		return this.enhanceWithAi(job, cvText, result);
	}

	private String extractCvText(String fileName) {
		try {
			Path filePath = this.resolveUploadPath(fileName);
			if (!Files.exists(filePath)) {
				return "";
			}
			Tika tika = new Tika();
			return tika.parseToString(filePath.toFile());
		} catch (Exception e) {
			return "";
		}
	}

	private Path resolveUploadPath(String fileName) {
		try {
			String folder = "resume/";
			URI uri = new URI(baseUri + folder + fileName);
			return Paths.get(uri);
		} catch (URISyntaxException e) {
			return Paths.get(baseUri.replace("file:/", "") + "resume/" + fileName);
		}
	}

	private List<String> getSkillNames(Job job) {
		if (job.getSkills() == null || job.getSkills().isEmpty()) {
			return List.of();
		}

		return job.getSkills().stream()
				.map(Skill::getName)
				.filter(name -> name != null && !name.isBlank())
				.map(this::normalizeSkillName)
				.distinct()
				.sorted(Comparator.naturalOrder())
				.collect(Collectors.toList());
	}

	private String normalizeSkillName(String value) {
		String normalized = normalizeText(value);
		return normalized.replace("-", " ").replace("/", " ").trim();
	}

	private String normalizeText(String value) {
		if (value == null) {
			return "";
		}
		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		normalized = normalized.toLowerCase(Locale.ROOT);
		normalized = normalized.replaceAll("[^a-z0-9\\s]+", " ");
		normalized = normalized.replaceAll("\\s+", " ").trim();
		return normalized;
	}

	private int calculateTextSimilarity(Job job, String normalizedCv) {
		StringBuilder text = new StringBuilder();
		text.append(job.getName()).append(" ");
		text.append(job.getDescription() == null ? "" : job.getDescription()).append(" ");
		if (job.getSkills() != null) {
			for (Skill skill : job.getSkills()) {
				if (skill != null && skill.getName() != null) {
					text.append(skill.getName()).append(" ");
				}
			}
		}
		String normalizedJob = normalizeText(text.toString());
		Set<String> jobTokens = Arrays.stream(normalizedJob.split(" ")).collect(Collectors.toSet());
		Set<String> cvTokens = Arrays.stream(normalizedCv.split(" ")).collect(Collectors.toSet());
		if (jobTokens.isEmpty() || cvTokens.isEmpty()) {
			return 0;
		}
		long intersection = jobTokens.stream().filter(cvTokens::contains).count();
		long union = jobTokens.size() + cvTokens.size() - intersection;
		if (union == 0) {
			return 0;
		}
		return (int) Math.round((intersection * 100.0) / union);
	}

	private String buildSummary(String jobName, int score, List<String> matchedSkills, List<String> missingSkills) {
		if (score >= 80) {
			return "CV của bạn có độ phù hợp cao với vị trí " + jobName + ". Bạn đã đáp ứng hầu hết kỹ năng yêu cầu, chỉ cần tinh chỉnh một số chi tiết để tăng sức thuyết phục.";
		}
		if (score >= 60) {
			return "CV của bạn có mức độ phù hợp khá tốt với vị trí " + jobName + ". Bạn đã đáp ứng nhiều kỹ năng chính nhưng vẫn cần bổ sung vài điểm mạnh để nổi bật hơn.";
		}
		if (score >= 30) {
			return "CV của bạn đang ở mức trung bình cho vị trí " + jobName + ". Cần làm rõ kỹ năng và kinh nghiệm liên quan tới công việc hơn nữa.";
		}
		return "CV của bạn chưa thật sự phù hợp với vị trí " + jobName + ". Nên bổ sung rõ hơn các kỹ năng và thành tích chủ chốt theo mô tả công việc.";
	}

	private List<String> buildSuggestions(List<String> missingSkills, List<String> matchedSkills) {
		List<String> suggestions = new ArrayList<>();
		if (missingSkills == null || missingSkills.isEmpty()) {
			suggestions.add("CV của bạn đã đạt mức phù hợp tốt. Hãy nêu rõ KPI, đóng góp và dự án quan trọng để tăng sức thuyết phục.");
			return suggestions;
		}

		for (String skill : missingSkills.stream().limit(3).collect(Collectors.toList())) {
			suggestions.add("Bổ sung kỹ năng " + skill + " và mô tả rõ kinh nghiệm thực tế liên quan tới kỹ năng này.");
		}

		if (matchedSkills != null && !matchedSkills.isEmpty()) {
			suggestions.add("Giữ nguyên các kỹ năng phù hợp như " + String.join(", ", matchedSkills.stream().limit(3).toList()) + ", và nhấn mạnh thành tích thực tế của từng kỹ năng.");
		}

		suggestions.add("Viết lại phần Mục tiêu nghề nghiệp để phù hợp với mô tả công việc và vị trí ứng tuyển.");
		return suggestions;
	}

	private ResAiResumeMatchDTO enhanceWithAi(Job job, String cvText, ResAiResumeMatchDTO result) {
		if (!this.aiEnabled || this.aiApiKey == null || this.aiApiKey.isBlank()) {
			return result;
		}
		if (job == null || cvText == null || cvText.isBlank()) {
			return result;
		}

		try {
			String prompt = buildAiPrompt(job, cvText);
			String responseBody = this.callAiModel(prompt);
			JsonNode root = new ObjectMapper().readTree(responseBody);
			JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
			if (contentNode.isMissingNode() || contentNode.isNull()) {
				return result;
			}
			String content = contentNode.asText();
			String jsonPayload = extractJsonBody(content);
			if (jsonPayload == null || jsonPayload.isBlank()) {
				return result;
			}
			JsonNode aiResult = new ObjectMapper().readTree(jsonPayload);
			if (aiResult.has("matchScore") && aiResult.path("matchScore").isNumber()) {
				result.setMatchScore(Math.max(0, Math.min(100, aiResult.path("matchScore").asInt())));
			}
			if (aiResult.has("summary") && !aiResult.path("summary").isNull()) {
				result.setSummary(aiResult.path("summary").asText());
			}
			if (aiResult.has("matchedSkills") && aiResult.path("matchedSkills").isArray()) {
				result.setMatchedSkills(this.readStringArray(aiResult.path("matchedSkills")));
			}
			if (aiResult.has("missingSkills") && aiResult.path("missingSkills").isArray()) {
				result.setMissingSkills(this.readStringArray(aiResult.path("missingSkills")));
			}
			if (aiResult.has("suggestions") && aiResult.path("suggestions").isArray()) {
				result.setSuggestions(this.readStringArray(aiResult.path("suggestions")));
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	private String buildAiPrompt(Job job, String cvText) {
		String skillsText = job.getSkills() == null ? "" : job.getSkills().stream()
				.map(Skill::getName)
				.filter(name -> name != null && !name.isBlank())
				.collect(Collectors.joining(", "));

		return "Bạn là một chuyên gia tuyển dụng và tư vấn CV. Hãy đánh giá mức độ phù hợp giữa CV và công việc. "
				+ "Trả về JSON thuần (không markdown, không giải thích) với các trường: matchScore, matchedSkills, missingSkills, summary, suggestions. "
				+ "matchScore là số nguyên từ 0 đến 100. matchedSkills và missingSkills là mảng string. suggestions là mảng string gợi ý cải thiện CV. "
				+ "Hãy trả lời bằng tiếng Việt. "
				+ "Vị trí công việc: " + job.getName() + ". "
				+ "Mô tả công việc: " + (job.getDescription() == null ? "" : job.getDescription()) + ". "
				+ "Kỹ năng yêu cầu: " + skillsText + ". "
				+ "Nội dung CV: " + cvText;
	}

	private String callAiModel(String prompt) throws Exception {
		String endpointUrl;
		String requestBody;
		Map<String, Object> messageBody = Map.of(
				"role", "user",
				"content", prompt
		);
		Map<String, Object> rootBody = Map.of(
				"model", this.aiModel,
				"messages", List.of(messageBody),
				"temperature", 0.2
		);
		if ("azure-openai".equalsIgnoreCase(this.aiProvider)) {
			String deployment = this.azureDeployment;
			if (deployment == null || deployment.isBlank()) {
				throw new IllegalArgumentException("Missing Azure OpenAI deployment name");
			}
			endpointUrl = this.aiEndpoint + "/openai/deployments/" + deployment + "/chat/completions?api-version=" + this.aiApiVersion;
			requestBody = new ObjectMapper().writeValueAsString(rootBody);
			return this.sendHttpRequest(endpointUrl, requestBody, "api-key", this.aiApiKey);
		}
		if (this.aiEndpoint == null || this.aiEndpoint.isBlank()) {
			this.aiEndpoint = "https://api.openai.com/v1";
		}
		endpointUrl = this.aiEndpoint + "/chat/completions";
		requestBody = new ObjectMapper().writeValueAsString(rootBody);
		return this.sendHttpRequest(endpointUrl, requestBody, "Authorization", "Bearer " + this.aiApiKey);
	}

	private String sendHttpRequest(String endpointUrl, String requestBody, String headerName, String headerValue) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endpointUrl))
				.header("Content-Type", "application/json")
				.header(headerName, headerValue)
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() >= 400) {
			throw new IllegalStateException("AI request failed: " + response.statusCode() + " - " + response.body());
		}
		return response.body();
	}

	private String extractJsonBody(String content) {
		if (content == null) {
			return null;
		}
		String trimmed = content.trim();
		if (trimmed.startsWith("```")) {
			trimmed = trimmed.replaceFirst("^```(?:json)?\\s*", "").replaceFirst("\\s*```$", "").trim();
		}
		return trimmed;
	}

	private List<String> readStringArray(JsonNode node) {
		if (node == null || !node.isArray()) {
			return List.of();
		}
		List<String> items = new ArrayList<>();
		for (JsonNode item : node) {
			if (item != null && !item.isNull()) {
				items.add(item.asText());
			}
		}
		return items;
	}
}
