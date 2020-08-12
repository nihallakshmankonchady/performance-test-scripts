package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.RegLostUinDetEntity;

public interface RegLostUinDetRepository extends JpaRepository<RegLostUinDetEntity, String> {
	RegLostUinDetEntity save(RegLostUinDetEntity entity);
}
