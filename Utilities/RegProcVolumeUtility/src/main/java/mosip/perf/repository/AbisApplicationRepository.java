package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.AbisApplicationEntity;

public interface AbisApplicationRepository extends JpaRepository<AbisApplicationEntity, String> {

	AbisApplicationEntity save(AbisApplicationEntity entity);

}
