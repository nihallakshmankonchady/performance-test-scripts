package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.AbisRequestEntity;

public interface AbisRequestRepository extends JpaRepository<AbisRequestEntity, String>{

	AbisRequestEntity save(AbisRequestEntity entity);
	
}
