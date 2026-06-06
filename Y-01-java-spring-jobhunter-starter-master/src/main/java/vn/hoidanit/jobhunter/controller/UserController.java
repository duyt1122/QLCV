package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/users")
	public ResponseEntity<ResCreateUserDTO> CreateNewUser(@Valid @RequestBody User user) throws IdInvalidException {
		boolean isEmailExist = this.userService.isEmailExits(user.getEmail());
		if (isEmailExist) {
			throw new IdInvalidException("email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác!");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User newUser = this.userService.handleCreateUser(user);
		ResCreateUserDTO res = userService.convertToResCreateUserDTO(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<ResUserDTO> findUserById(@PathVariable("id") Long id) throws IdInvalidException {
		User user = this.userService.findUser(id);
		if (user == null) {
			throw new IdInvalidException("User với id = " + id + " không tồn tại!");
		}
		ResUserDTO res = userService.convertToResUserDTO(user);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
		User user = this.userService.findUser(id);
		if (user == null) {
			throw new IdInvalidException("user vớ Id = " + id + " không tồn tại!");
		}
		this.userService.deleteUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/users")
	@ApiMessage("fetch all users")
	public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {
//		String sCurrent = currentOptional.isPresent() == true ? currentOptional.get() : "";
//		String sPageSize = pageSizeOptional.isPresent() == true ? pageSizeOptional.get() : "";
//		int current = Integer.parseInt(sCurrent);
//		int pageSize = Integer.parseInt(sPageSize);
//		Pageable pageable = PageRequest.of(current - 1, pageSize);
		ResultPaginationDTO users = this.userService.getUsers(spec, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@PutMapping("/users")
	public ResponseEntity<ResUpdateUserDTO> putUser(@RequestBody User presentUser) {
		User user = this.userService.updateUser(presentUser);
		ResUpdateUserDTO res = this.userService.convertToResUpdateDTO(user);
		return ResponseEntity.ok(res);
	}
}
