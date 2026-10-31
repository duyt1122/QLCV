package vn.topcv.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import vn.topcv.jobhunter.domain.Role;

@Getter
@Setter
public class ResLoginDTO {
	@JsonProperty("access_token")
	private String accessToken;
	private UserLogin user;

	@Setter
	@Getter
	public static class UserLogin {
		private long id;
		private String email;
		private String name;
		private Role role;

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public UserLogin(long id, String email, String name, Role role) {
			super();
			this.id = id;
			this.email = email;
			this.name = name;
			this.role = role;
		}

		public UserLogin() {

		}

	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public UserLogin getUser() {
		return user;
	}

	public void setUser(UserLogin user) {
		this.user = user;
	}

	// class userGetAccount
	public static class UserGetAccount {
		private UserLogin user;

		public UserLogin getUser() {
			return user;
		}

		public void setUser(UserLogin user) {
			this.user = user;
		}

		public UserGetAccount(UserLogin user) {
			super();
			this.user = user;
		}

		public UserGetAccount() {

		}

	}

	public static class UserInsideToken {
		private long id;
		private String email;
		private String name;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public UserInsideToken(long id, String email, String name) {
			super();
			this.id = id;
			this.email = email;
			this.name = name;
		}

		public UserInsideToken() {
			super();
		}

	}

}
