package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.SyncRegistrationEntity;

public interface RegistrationListRepository extends JpaRepository<SyncRegistrationEntity, String> {

	SyncRegistrationEntity save(SyncRegistrationEntity entity);
}
