package vn.topcv.jobhunter.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.topcv.jobhunter.domain.Company;
import vn.topcv.jobhunter.domain.Job;
import vn.topcv.jobhunter.domain.Resume;
import vn.topcv.jobhunter.domain.User;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.domain.response.resume.ResAiResumeMatchDTO;
import vn.topcv.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.topcv.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.topcv.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.topcv.jobhunter.service.ResumeAiService;
import vn.topcv.jobhunter.service.ResumeService;
import vn.topcv.jobhunter.service.UserService;
import vn.topcv.jobhunter.util.SecurityUtil;
import vn.topcv.jobhunter.util.annotation.ApiMessage;
import vn.topcv.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

	private final ResumeService resumeService;
	private final UserService userService;
	private final ResumeAiService resumeAiService;

	private final FilterBuilder filterBuilder;
	private final FilterSpecificationConverter filterSpecificationConverter;

	public ResumeController(ResumeService resumeService, UserService userService, ResumeAiService resumeAiService,
			FilterBuilder filterBuilder, FilterSpecificationConverter filterSpecificationConverter) {
		this.resumeService = resumeService;
		this.userService = userService;
		this.resumeAiService = resumeAiService;
		this.filterBuilder = filterBuilder;
		this.filterSpecificationConverter = filterSpecificationConverter;
	}

	@PostMapping("/resumes")
	@ApiMessage("create a resume")
	public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {

		// check exists user and job
		boolean isIdExists = this.resumeService.checkResumeUserandJob(resume);
		if (!isIdExists) {
			throw new IdInvalidException("user id/ job id không tồn tại!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
	}

	@PutMapping("/resumes")
	@ApiMessage("update a resume")
	public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
		// check id exist
		Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
		if (!resumeOptional.isPresent()) {
			throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
		}

		Resume reqResume = resumeOptional.get();

		reqResume.setStatus(resume.getStatus());

		return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.update(reqResume));
	}

	@DeleteMapping("/resumes/{id}")
	@ApiMessage("delete a resume")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
		Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);

		if (reqResumeOptional.isEmpty()) {
			throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
		}
		this.resumeService.delete(id);
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("/resumes/{id}")
	@ApiMessage("Fetch Resume By Id")
	public ResponseEntity<ResFetchResumeDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
		Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
		if (resumeOptional.isEmpty()) {
			throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
		}
		Resume resume = resumeOptional.get();
		return ResponseEntity.ok().body(this.resumeService.getResume(resume));
	}

	@GetMapping("/resumes")
	@ApiMessage("get all resumes")
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Resume> spec, Pageable pageable) {
		List<Long> arrJobIds = null;
		String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
				: "";
		User currentUser = this.userService.handleGetUserByUserName(email);
		if (currentUser != null) {
			Company userCompany = currentUser.getCompany();
			if (userCompany != null) {
				List<Job> companyJobs = userCompany.getJobs();
				if (companyJobs != null && companyJobs.size() > 0) {
					arrJobIds = companyJobs.stream().map(x -> x.getId()).collect(Collectors.toList());
				}
			}
		}
		// Lọc Resume
		Specification<Resume> jobInSpec = filterSpecificationConverter
				.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());

		Specification<Resume> finalSpec = jobInSpec.and(spec);

		return ResponseEntity.ok().body(this.resumeService.fetchAllResume(finalSpec, pageable));
	}

	@GetMapping("/resumes/ai-match")
	@ApiMessage("Analyze CV match score against job requirements")
	public ResponseEntity<ResAiResumeMatchDTO> analyzeMatch(@RequestParam("jobId") Long jobId,
			@RequestParam("fileName") String fileName) {
		ResAiResumeMatchDTO result = this.resumeAiService.analyzeMatch(jobId, fileName);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/resumes/by-user")
	@ApiMessage("Get list resumes by user")
	public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

		return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
	}
}
