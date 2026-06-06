package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final SecurityUtil securityUtil;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
	private Long refreshTokenExpiration;

	public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
			UserService userService, PasswordEncoder passwordEncoder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.securityUtil = securityUtil;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/auth/login")
	public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO login) {
		// Nạp input gồm username/password vào Security || Lấy thông tin username
		// password tạo một token chưa xác thực và gửi cho AuthenticationManager để xác
		// thực
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				login.getUsername(), login.getPassword());

		// xác thực người dùng => cần viết hàm loadUserByUsername sau khi xác thực nó sẽ
		// xóa phần password và thêm role để trạng thái authentication = true
		// getObject() trả về AuthenticationManager – interface chính của Spring
		// Security để xác thực user.
		ResLoginDTO res = new ResLoginDTO();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		User user = this.userService.handleGetUserByUserName(login.getUsername());
		if (user != null) {
			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName(),
					user.getRole());
			res.setUser(userLogin);
		}
		// create token
		String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);

		// create refresh token
		String refres_token = this.securityUtil.createRefreshToken(login.getUsername(), res);
		// set thông tin vào security contextholder
		SecurityContextHolder.getContext().setAuthentication(authentication);

		res.setAccessToken(access_token);
		// update user Token
		this.userService.updateUserToken(refres_token, login.getUsername());
		// set cookies
		ResponseCookie resCookies = ResponseCookie.from("refresh_token", refres_token).httpOnly(true).secure(true)
				.path("/").maxAge(refreshTokenExpiration).build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
	}

	@GetMapping("/auth/account")
	@ApiMessage("Fetch account")
	public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
		String email = securityUtil.getCurrentUserLogin().isPresent() ? securityUtil.getCurrentUserLogin().get() : "";
		User currentUser = this.userService.handleGetUserByUserName(email);
		ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
		ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
		if (currentUser != null) {
			userLogin.setId(currentUser.getId());
			userLogin.setEmail(currentUser.getEmail());
			userLogin.setName(currentUser.getName());
			userLogin.setRole(currentUser.getRole());
			userGetAccount.setUser(userLogin);
		}
		return ResponseEntity.ok().body(userGetAccount);
	}

	@GetMapping("/auth/refresh")
	@ApiMessage("Get User by refresh token")
	public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token)
			throws IdInvalidException {

		// Check valid
		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
		String email = decodedToken.getSubject();
		User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
		if (currentUser == null) {
			throw new IdInvalidException("Refresh token không hợp lệ");
		}
		ResLoginDTO res = new ResLoginDTO();
		User user = this.userService.handleGetUserByUserName(email);
		if (user != null) {
			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName(),
					user.getRole());
			res.setUser(userLogin);
		}
		// create token
		String access_token = this.securityUtil.createAccessToken(email, res);

		// create refresh token
		String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

		res.setAccessToken(access_token);
		// update user
		this.userService.updateUserToken(new_refresh_token, email);
		// set cookies
		ResponseCookie resCookies = ResponseCookie.from("refresh_token", new_refresh_token).httpOnly(true).secure(true)
				.path("/").maxAge(refreshTokenExpiration).build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<Void> logout() throws IdInvalidException {
		String email = securityUtil.getCurrentUserLogin().isPresent() ? securityUtil.getCurrentUserLogin().get() : "";
		if (email.equals("")) {
			throw new IdInvalidException("Access Token không hợp lệ");
		}
		// update refresh token bằng null
		this.userService.updateUserToken(null, email);

		ResponseCookie deleteSpringCookie = ResponseCookie.from("refresh_token", null).httpOnly(true).secure(true)
				.path("/").maxAge(0).build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).body(null);
	}

	@PostMapping("/auth/register")
	@ApiMessage("Register a new User")
	public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User postManUser) throws IdInvalidException {
		boolean isEmailExist = this.userService.isEmailExits(postManUser.getEmail());

		if (isEmailExist) {
			throw new IdInvalidException(
					"Email " + postManUser.getEmail() + " đã tồn tại vui lòng sử dụng email khác!");
		}

		String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
		postManUser.setPassword(hashPassword);
		User user = this.userService.handleCreateUser(postManUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(user));
	}
}
