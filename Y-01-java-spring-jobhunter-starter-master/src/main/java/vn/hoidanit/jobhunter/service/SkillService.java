package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

	public final SkillRepository skillRepository;

	public SkillService(SkillRepository skillRepository) {
		this.skillRepository = skillRepository;
	}

	public Skill createSkill(Skill s) {
		return this.skillRepository.save(s);
	}

	public boolean isNameExsist(String name) {
		return this.skillRepository.existsByName(name);
	}

	public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
		Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageSkill.getTotalPages());
		mt.setTotal(pageSkill.getTotalElements());

		rs.setMeta(mt);
		rs.setResult(pageSkill.getContent());
		return rs;
	}

	public Skill updateSkill(Skill s) {
		return this.skillRepository.save(s);
	}

	public Skill fetchSkillById(long id) {
		Optional<Skill> skillOptional = this.skillRepository.findById(id);
		if (skillOptional.isPresent()) {
			return skillOptional.get();
		}
		return null;
	}

	@Transactional
	public void deleteSkill(long id) {
		// delete job (inside job_skill table)
		Optional<Skill> skillOptional = this.skillRepository.findById(id);
		Skill currentSkill = skillOptional.get();
		currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

		// delete subscriber (inside subscriber_skill table)
		currentSkill.getSubscribers().forEach(x -> x.getSkills().remove(currentSkill));
		// delete skill
		this.skillRepository.delete(currentSkill);

	}

}
