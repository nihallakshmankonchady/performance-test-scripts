package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.RegDemoDedupeListEntity;

public interface RegDemoDedupeListRepository extends JpaRepository<RegDemoDedupeListEntity, String> {

	RegDemoDedupeListEntity save(RegDemoDedupeListEntity entity);
}
