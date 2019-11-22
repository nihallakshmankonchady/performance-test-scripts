package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.AbisResponseDetEntity;

public interface AbisResponseDetRepository extends JpaRepository<AbisResponseDetEntity, String> {

	AbisResponseDetEntity save(AbisResponseDetEntity entity);
}
