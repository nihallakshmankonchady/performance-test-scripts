package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mosip.perf.entity.VidEntity;

@Repository
public interface VidEntityRepository extends JpaRepository<VidEntity, String> {

	VidEntity save(VidEntity vidEntity);

}
