package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.ManualVerificationEntity;

public interface ManualVerificationRepository extends JpaRepository<ManualVerificationEntity, String> {

	ManualVerificationEntity save(ManualVerificationEntity entity);
}
