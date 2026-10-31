package vn.topcv.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.topcv.jobhunter.domain.Skill;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.service.SkillService;
import vn.topcv.jobhunter.util.annotation.ApiMessage;
import vn.topcv.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillCotroller {

	private final SkillService skillService;

	public SkillCotroller(SkillService skillService) {
		this.skillService = skillService;
	}

	@PostMapping("/skills")
	@ApiMessage("Create a skill")
	public ResponseEntity<Skill> create(@Valid @RequestBody Skill s) throws IdInvalidException {
		// check name
		if (skillService.isNameExsist(s.getName())) {
			throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(s));
	}

	@GetMapping("/skills")
	@ApiMessage("fetch all skills")
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec, pageable));
	}

	@PutMapping("/skills")
	public ResponseEntity<Skill> update(@RequestBody Skill skill) throws IdInvalidException {
		Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
		// check id
		if (currentSkill == null) {
			throw new IdInvalidException("Skill id = " + skill.getId() + " không tồn tại!");
		}
		// check name
		if (skill.getName() == null && this.skillService.isNameExsist(skill.getName())) {
			throw new IdInvalidException("skill name = " + skill.getName() + " đã tồn tại!");
		}

		currentSkill.setName(skill.getName());
		return ResponseEntity.status(HttpStatus.OK).body(this.skillService.updateSkill(currentSkill));
	}

	@GetMapping("/skills/{id}")
	public ResponseEntity<Skill> getSkillById(long id) {
		return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchSkillById(id));
	}
}
