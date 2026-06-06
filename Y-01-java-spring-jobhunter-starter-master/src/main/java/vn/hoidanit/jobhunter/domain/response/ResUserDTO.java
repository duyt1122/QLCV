package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import vn.hoidanit.jobhunter.util.constant.GenderEnum;

public class ResUserDTO {
	private long id;
	private String email;
	private String name;
	private GenderEnum gender;
	private String address;
	private int age;
	private Instant updatedAt;
	private Instant createdAt;
	private CompanyUser company;
	private RoleUser role;

	public RoleUser getRole() {
		return role;
	}

	public void setRole(RoleUser role) {
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

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public CompanyUser getCompany() {
		return company;
	}

	public void setCompany(CompanyUser company) {
		this.company = company;
	}

	public ResUserDTO(long id, String email, String name, GenderEnum gender, String address, int age, Instant updatedAt,
			Instant createdAt, CompanyUser company) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.age = age;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
		this.company = company;
	}

	public ResUserDTO() {

	}

	public static class CompanyUser {
		private Long id;
		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public CompanyUser(Long id, String name) {
			this.id = id;
			this.name = name;

		}

		public CompanyUser() {

		}

	}

	public static class RoleUser {
		private long id;
		private String name;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public RoleUser(long id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public RoleUser() {

		}

	}

}
