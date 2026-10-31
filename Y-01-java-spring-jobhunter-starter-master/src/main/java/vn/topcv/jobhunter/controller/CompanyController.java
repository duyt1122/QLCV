package vn.topcv.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import vn.topcv.jobhunter.domain.Company;
import vn.topcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.topcv.jobhunter.service.CompanyService;
import vn.topcv.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@PostMapping("/companies")
	public ResponseEntity<Company> createACompany(@Valid @RequestBody Company company) {
		Company cpn = this.companyService.createCompany(company);
		return ResponseEntity.ok().body(cpn);
	}

	@GetMapping("/companies")
	@ApiMessage("fetch companies")
	public ResponseEntity<ResultPaginationDTO> getCompany(@Filter Specification<Company> spec, Pageable pageable) {

		return ResponseEntity.ok(this.companyService.findAllCompany(spec, pageable));
	}

	@GetMapping("companies/{id}")
	@ApiMessage("fetch company by id")
	public ResponseEntity<Company> getACompany(@PathVariable("id") Long id) {
		Company company = this.companyService.getCompanyById(id).isPresent() ? companyService.getCompanyById(id).get()
				: null;
		return ResponseEntity.ok().body(company);
	}

	@DeleteMapping("companies/{id}")
	public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
		this.companyService.deleteACompany(id);
		return ResponseEntity.ok().body(null);
	}

	@PutMapping("companies")
	public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
		Company updateCompany = this.companyService.updateCompany(company);
		return ResponseEntity.ok(updateCompany);
	}
}
