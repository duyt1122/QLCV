package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final UserRepository userRepository;

	public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
		this.companyRepository = companyRepository;
		this.userRepository = userRepository;
	}

	public Company createCompany(Company company) {
		return this.companyRepository.save(company);
	}

	public ResultPaginationDTO findAllCompany(Specification<Company> spec, Pageable pageable) {
		Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());

		mt.setPages(pCompany.getTotalPages());
		mt.setTotal(pCompany.getTotalElements());

		rs.setMeta(mt);
		rs.setResult(pCompany.getContent());
		return rs;
	}

	public Optional<Company> getCompanyById(Long id) {
		return this.companyRepository.findById(id);
	}

	public Company updateCompany(Company company) {
		Company currentCompany = getCompanyById(company.getId()).isPresent() ? getCompanyById(company.getId()).get()
				: null;
		currentCompany.setName(company.getName());
		currentCompany.setDescription(company.getDescription());
		currentCompany.setAddress(company.getAddress());
		currentCompany.setLogo(company.getLogo());

		return this.companyRepository.save(currentCompany);

	}

	public void deleteACompany(Long id) {
		Optional<Company> com = this.companyRepository.findById(id);
		if (com.isPresent()) {
			List<User> users = this.userRepository.findByCompany(com.get());
			this.userRepository.deleteAll(users);
		}
		this.companyRepository.deleteById(id);
	}
}
