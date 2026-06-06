package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {

	private final PermissionRepository permissionRepository;

	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	public Permission fetchById(long id) {
		Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
		if (permissionOptional.isPresent()) {
			return permissionOptional.get();
		}
		return null;
	}

	public boolean isPermissionExist(Permission permission) {
		return permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(),
				permission.getModule());
	}

	public Permission create(Permission permission) {
		return this.permissionRepository.save(permission);
	}

	public Permission update(Permission permission) {
		Permission permissionDB = this.fetchById(permission.getId());
		if (permissionDB != null) {
			permissionDB.setName(permission.getName());
			permissionDB.setApiPath(permission.getApiPath());
			permissionDB.setMethod(permission.getMethod());
			permissionDB.setModule(permission.getModule());

			return this.permissionRepository.save(permissionDB);
		}
		return null;
	}

	public void delete(long id) {
		// delete permission_role
		Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
		Permission currentPermission = permissionOptional.get();
		currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

		// delete permission
		this.permissionRepository.delete(currentPermission);
	}

	public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
		Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);

		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(pPermissions.getTotalPages());
		meta.setTotal(pPermissions.getTotalElements());

		rs.setMeta(meta);
		rs.setResult(pPermissions.getContent());

		return rs;
	}

	public boolean isSameName(Permission p) {
		Permission permissionDB = this.fetchById(p.getId());
		if (permissionDB != null) {
			if (permissionDB.getName().equals(p.getName()))
				;
			return true;
		}
		return false;
	}
}
