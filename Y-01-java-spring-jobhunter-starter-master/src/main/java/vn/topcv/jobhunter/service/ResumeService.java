package vn.topcv.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.topcv.jobhunter.domain.Job;
import vn.topcv.jobhunter.domain.Resume;
import vn.topcv.jobhunter.domain.User;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.topcv.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.topcv.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.topcv.jobhunter.repository.JobRepository;
import vn.topcv.jobhunter.repository.ResumeRepository;
import vn.topcv.jobhunter.repository.UserRepository;
import vn.topcv.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {

	@Autowired
	FilterBuilder fb;

	@Autowired
	private FilterParser filterParser;

	@Autowired
	private FilterSpecificationConverter filterSpecificationConverter;

	private final ResumeRepository resumRepository;
	private final UserRepository userRepository;
	private final JobRepository jobRepository;

	public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
			JobRepository jobRepository) {
		this.resumRepository = resumeRepository;
		this.jobRepository = jobRepository;
		this.userRepository = userRepository;
	}

	public Optional<Resume> fetchById(Long id) {
		return this.resumRepository.findById(id);
	}

	public ResCreateResumeDTO create(Resume resume) {
		resume = this.resumRepository.save(resume);

		ResCreateResumeDTO res = new ResCreateResumeDTO();
		res.setId(resume.getId());
		res.setCreatedAt(resume.getCreatedAt());
		res.setCreatedBy(resume.getCreatedBy());

		return res;
	}

	public boolean checkResumeUserandJob(Resume resume) {
		// check user by id
		if (resume.getUser() == null) {
			return false;
		}

		Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());

		if (userOptional.isEmpty()) {
			return false;
		}

		// check job by id

		if (resume.getJob() == null) {
			return false;
		}

		Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());

		if (jobOptional.isEmpty()) {
			return false;
		}

		return true;
	}

	public ResUpdateResumeDTO update(Resume resume) {
		resume = this.resumRepository.save(resume);

		ResUpdateResumeDTO dto = new ResUpdateResumeDTO();

		dto.setUpdatedAt(resume.getUpdaedAt());
		dto.setUpdatedBy(resume.getUpdateBy());

		return dto;
	}

	public void delete(long id) {
		this.resumRepository.deleteById(id);
	}

	public ResFetchResumeDTO getResume(Resume resume) {
		ResFetchResumeDTO dto = new ResFetchResumeDTO();

		dto.setId(resume.getId());
		dto.setEmail(resume.getEmail());
		dto.setUrl(resume.getUrl());
		dto.setStatus(resume.getStatus());
		dto.setCreatedAt(resume.getCreatedAt());
		dto.setCreatedBy(resume.getCreatedBy());
		dto.setUpdatedAt(resume.getUpdaedAt());
		dto.setUpdatedBy(resume.getUpdateBy());

		if (resume.getJob() != null) {
			dto.setCompanyName(resume.getJob().getCompany().getName());
		}

		dto.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
		dto.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

		return dto;
	}

	public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
		Page<Resume> pageResume = this.resumRepository.findAll(spec, pageable);
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPageSize(pageResume.getTotalPages());
		meta.setTotal(pageResume.getTotalElements());

		ResultPaginationDTO rs = new ResultPaginationDTO();

		rs.setMeta(meta);

		// remove sensitive data

		List<ResFetchResumeDTO> listResume = pageResume.getContent().stream().map(item -> this.getResume(item))
				.collect(Collectors.toList());
		rs.setResult(listResume);
		return rs;
	}

	public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
		// query builder
		String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
				: "";
		FilterNode node = filterParser.parse("email='" + email + "'");
		FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
		Page<Resume> pageResume = this.resumRepository.findAll(spec, pageable);

		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());

		mt.setPages(pageResume.getTotalPages());
		mt.setTotal(pageResume.getTotalElements());

		rs.setMeta(mt);

		// remove sensitive data
		List<ResFetchResumeDTO> listResume = pageResume.getContent().stream().map(item -> this.getResume(item))
				.collect(Collectors.toList());

		rs.setResult(listResume);

		return rs;
	}

}
