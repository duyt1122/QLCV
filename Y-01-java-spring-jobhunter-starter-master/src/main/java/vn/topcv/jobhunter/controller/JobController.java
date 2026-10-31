package vn.topcv.jobhunter.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.topcv.jobhunter.domain.Job;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.topcv.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.topcv.jobhunter.service.JobService;
import vn.topcv.jobhunter.util.annotation.ApiMessage;
import vn.topcv.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {

	private final JobService jobService;

	JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@PostMapping("/jobs")
	@ApiMessage("Create a job")
	public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
	}

	@PutMapping("/jobs")
	@ApiMessage("update a job")
	public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
		Optional<Job> currentOptional = this.jobService.fetchJobById(job.getId());
		if (!currentOptional.isPresent()) {
			throw new IdInvalidException("job not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(this.jobService.update(job, currentOptional.get()));
	}

	@GetMapping("/jobs")
	@ApiMessage("get all jobs")
	public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(this.jobService.fetchAll(spec, pageable));
	}

	@DeleteMapping("/jobs/{id}")
	@ApiMessage("delete a job")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
		Optional<Job> optionalJob = this.jobService.fetchJobById(id);
		if (!optionalJob.isPresent()) {
			throw new IdInvalidException("job not found");
		}
		this.jobService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/jobs/{id}")
	@ApiMessage("fetch job by id")
	public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
		Optional<Job> optionalJob = this.jobService.fetchJobById(id);

		if (!optionalJob.isPresent()) {
			throw new IdInvalidException("job not found!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(optionalJob.get());

	}
}
