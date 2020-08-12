package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.AbisResponseEntity;

public interface AbisResponseRepository extends JpaRepository<AbisResponseEntity, String> {

	AbisResponseEntity save(AbisResponseEntity entity);
}
