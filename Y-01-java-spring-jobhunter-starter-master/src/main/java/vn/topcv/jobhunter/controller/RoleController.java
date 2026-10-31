package vn.topcv.jobhunter.controller;

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
import vn.topcv.jobhunter.domain.Role;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.service.RoleService;
import vn.topcv.jobhunter.util.annotation.ApiMessage;
import vn.topcv.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping("/roles")
	@ApiMessage("create a Role")
	public ResponseEntity<Role> create(@Valid @RequestBody Role role) throws IdInvalidException {

		// check name
		if (this.roleService.existsByName(role.getName())) {
			throw new IdInvalidException("Role với name = " + role.getName() + " đã tồn tại");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
	}

	@PutMapping("/roles")
	@ApiMessage(" update a role")
	public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws IdInvalidException {
		// check id
		if (this.roleService.fetchById(role.getId()) == null) {
			throw new IdInvalidException("role với id = " + role.getId() + " không tồn tại!");
		}
		// check name
//		if (this.roleService.existsByName(role.getName())) {
//			throw new IdInvalidException("role với name = " + role.getName() + " đã tồn tại");
//		}

		return ResponseEntity.ok().body(this.roleService.update(role));
	}

	@GetMapping("/roles")
	@ApiMessage("fetch all roles")
	public ResponseEntity<ResultPaginationDTO> getRoles(@Filter Specification<Role> spec, Pageable pageable) {

		return ResponseEntity.ok().body(this.roleService.getRoles(spec, pageable));
	}

	@DeleteMapping("/roles/{id}")
	@ApiMessage("delete a role")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
		// check role
		if (this.roleService.fetchById(id) == null) {
			throw new IdInvalidException("role với id = " + id + " không tồn tại !");
		}
		// delete role
		this.roleService.delete(id);
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("/roles/{id}")
	@ApiMessage("Fetch role by Id")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
		Role role = this.roleService.fetchById(id);
		if (role == null) {
			throw new IdInvalidException("Role với id = " + id + " không tồn tại!");
		}
		return ResponseEntity.ok().body(role);
	}
}
