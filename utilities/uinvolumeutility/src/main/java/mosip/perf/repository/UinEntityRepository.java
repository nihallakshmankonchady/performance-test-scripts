package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mosip.perf.entity.UinEntity;

@Repository
public interface UinEntityRepository extends JpaRepository<UinEntity, String> {

	UinEntity save(UinEntity entity);

}
