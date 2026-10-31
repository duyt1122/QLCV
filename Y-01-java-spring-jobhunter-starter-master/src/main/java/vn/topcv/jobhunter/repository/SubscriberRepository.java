package vn.topcv.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.topcv.jobhunter.domain.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
	boolean existsByEmail(String email);

	Subscriber findByEmail(String email);
}
