package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
	}

	public boolean existsByName(String name) {
		return this.roleRepository.existsByName(name);
	}

	public Role fetchById(long id) {
		Optional<Role> roleOptional = this.roleRepository.findById(id);

		if (roleOptional.isPresent()) {
			return roleOptional.get();
		}
		return null;
	}

	public Role create(Role role) {
		// check permission
		if (role.getPermissions() != null) {
			List<Long> listId = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
			List<Permission> listPermission = this.permissionRepository.findByIdIn(listId);

			role.setPermissions(listPermission);
		}

		return this.roleRepository.save(role);
	}

	public Role update(Role role) {
		Role roleDB = this.fetchById(role.getId());
		// check permission
		if (role.getPermissions() != null) {
			List<Long> listId = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
			List<Permission> listPermission = this.permissionRepository.findByIdIn(listId);

			role.setPermissions(listPermission);
		}

		roleDB.setName(role.getName());
		roleDB.setDescription(role.getDescription());
		roleDB.setActive(role.isActive());
		roleDB.setPermissions(role.getPermissions());

		return roleRepository.save(roleDB);
	}

	public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
		Page<Role> listRoles = this.roleRepository.findAll(spec, pageable);
		ResultPaginationDTO res = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(listRoles.getTotalPages());
		meta.setTotal(listRoles.getTotalElements());

		res.setMeta(meta);
		res.setResult(listRoles.getContent());

		return res;
	}

	public void delete(long id) {
		this.roleRepository.deleteById(id);
	}

}
