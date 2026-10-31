package vn.topcv.jobhunter.domain.response.resume;

import java.util.List;

public class ResAiResumeMatchDTO {
	private Long jobId;
	private String jobName;
	private String companyName;
	private int matchScore;
	private List<String> matchedSkills;
	private List<String> missingSkills;
	private String summary;
	private List<String> suggestions;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(int matchScore) {
		this.matchScore = matchScore;
	}

	public List<String> getMatchedSkills() {
		return matchedSkills;
	}

	public void setMatchedSkills(List<String> matchedSkills) {
		this.matchedSkills = matchedSkills;
	}

	public List<String> getMissingSkills() {
		return missingSkills;
	}

	public void setMissingSkills(List<String> missingSkills) {
		this.missingSkills = missingSkills;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}
}
