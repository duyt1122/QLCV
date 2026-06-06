package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final CompanyService companyService;
	private final RoleService roleService;

	public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
		this.userRepository = userRepository;
		this.companyService = companyService;
		this.roleService = roleService;
	}

	public User handleCreateUser(User user) {
		if (user.getCompany() != null) {
			Optional<Company> companyOptional = this.companyService.getCompanyById(user.getCompany().getId());
			Company company = companyOptional.isPresent() ? companyOptional.get() : null;
			user.setCompany(company);
		}
		// check role

		if (user.getRole() != null) {
			Role r = this.roleService.fetchById(user.getRole().getId());
			user.setRole(r != null ? r : null);
		}
		return this.userRepository.save(user);
	}

	public User findUser(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public void deleteUser(Long id) {
		this.userRepository.deleteById(id);
	}

	public ResultPaginationDTO getUsers(Specification<User> spec, Pageable pageable) {
		Page<User> pageUser = this.userRepository.findAll(spec, pageable);
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(pageUser.getTotalPages());
		meta.setTotal(pageUser.getTotalElements());

		ResultPaginationDTO pagi = new ResultPaginationDTO();

		pagi.setMeta(meta);
		List<ResUserDTO> users = pageUser.getContent().stream().map(item -> this.convertToResUserDTO(item))
				.collect(Collectors.toList());
		pagi.setResult(users);
		return pagi;
	}

	public User handleGetUserByUserName(String username) {
		return this.userRepository.findByEmail(username);
	}

	public User updateUser(User user) {
		User currentUser = this.findUser(user.getId());
		if (currentUser != null) {
			currentUser.setAddress(user.getAddress());
			currentUser.setGender(user.getGender());
			currentUser.setAge(user.getAge());
			currentUser.setName(user.getName());

			// check company
			if (user.getCompany() != null) {
				Optional<Company> companyOptional = this.companyService.getCompanyById(user.getCompany().getId());
				currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);

			}
			// check role
			if (user.getRole() != null) {
				Role r = this.roleService.fetchById(user.getRole().getId());
				currentUser.setRole(r != null ? r : null);
			}
			currentUser = this.userRepository.save(currentUser);
		}
		return currentUser;

	}

	public boolean isEmailExits(String email) {
		return this.userRepository.existsByemail(email);
	}

	public ResCreateUserDTO convertToResCreateUserDTO(User user) {
		ResCreateUserDTO res = new ResCreateUserDTO();
		ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();
		res.setId(user.getId());
		res.setEmail(user.getEmail());
		res.setName(user.getName());
		res.setAge(user.getAge());
		res.setGender(user.getGender());
		res.setAddress(user.getAddress());
		res.setCreatedAt(user.getCreatedAt());
		if (user.getCompany() != null) {
			com.setId(user.getCompany().getId());
			com.setName(user.getCompany().getName());
			res.setCompany(com);
		}
		return res;
	}

	public ResUserDTO convertToResUserDTO(User user) {
		ResUserDTO res = new ResUserDTO();
		ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
		ResUserDTO.RoleUser role = new ResUserDTO.RoleUser();

		if (user.getCompany() != null) {
			com.setId(user.getCompany().getId());
			com.setName(user.getCompany().getName());
			res.setCompany(com);
		}
		if (user.getRole() != null) {
			role.setId(user.getRole().getId());
			role.setName(user.getRole().getName());
			res.setRole(role);
		}
		res.setId(user.getId());
		res.setEmail(user.getEmail());
		res.setName(user.getName());
		res.setGender(user.getGender());
		res.setAddress(user.getAddress());
		res.setAge(user.getAge());
		res.setUpdatedAt(user.getUpdatedAt());
		res.setCreatedAt(user.getCreatedAt());

		return res;
	}

	public ResUpdateUserDTO convertToResUpdateDTO(User user) {
		ResUpdateUserDTO res = new ResUpdateUserDTO();
		ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();

		if (user.getCompany() != null) {
			com.setId(user.getCompany().getId());
			com.setName(user.getCompany().getName());
			res.setCompany(com);
		}
		res.setId(user.getId());
		res.setName(user.getName());
		res.setAddress(user.getAddress());
		res.setAge(user.getAge());
		res.setGender(user.getGender());
		res.setUpdateAt(user.getUpdatedAt());
		return res;
	}

	public void updateUserToken(String token, String email) {
		User currentUser = this.handleGetUserByUserName(email);
		if (currentUser != null) {
			currentUser.setRefreshToken(token);
			this.userRepository.save(currentUser);
		}
	}

	public User getUserByRefreshTokenAndEmail(String token, String email) {
		return this.userRepository.findByRefreshTokenAndEmail(token, email);
	}

}
