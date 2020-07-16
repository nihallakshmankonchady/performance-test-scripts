package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.RegBioRefEntity;

public interface RegBioRefRepository extends JpaRepository<RegBioRefEntity, String> {

	RegBioRefEntity save(RegBioRefEntity entity);
	
}
