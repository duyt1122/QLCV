package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

	private final SubscriberService subscriberService;

	public SubscriberController(SubscriberService subscriberService) {
		this.subscriberService = subscriberService;
	}

	@PostMapping("/subscribers")
	public ResponseEntity<Subscriber> create(@RequestBody Subscriber subscriber) throws IdInvalidException {
		// check email
		boolean isExist = this.subscriberService.isExistByEmail(subscriber.getEmail());
		if (isExist) {
			throw new IdInvalidException("Email " + subscriber.getEmail() + " đã tồn tại!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(subscriber));
	}

	@PutMapping("/subscribers")
	public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) throws IdInvalidException {
		// check id

		Subscriber subsDB = this.subscriberService.findById(subscriber.getId());
		if (subsDB == null) {
			throw new IdInvalidException("Id " + subscriber.getId() + " không tồn tại!");
		}

		return ResponseEntity.status(HttpStatus.OK).body(this.subscriberService.update(subsDB, subscriber));
	}

	public ResponseEntity<Subscriber> getSubscriberSkill() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
				: "";
		return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
	}

}
