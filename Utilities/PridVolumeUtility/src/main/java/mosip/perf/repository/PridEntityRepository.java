package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mosip.perf.entity.PridEntity;

@Repository
public interface PridEntityRepository extends JpaRepository<PridEntity, String> {

	PridEntity save(PridEntity entity);

}
