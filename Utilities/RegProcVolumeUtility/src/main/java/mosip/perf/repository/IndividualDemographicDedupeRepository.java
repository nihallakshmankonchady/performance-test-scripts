package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.IndividualDemographicDedupeEntity;

public interface IndividualDemographicDedupeRepository
		extends JpaRepository<IndividualDemographicDedupeEntity, String> {

	IndividualDemographicDedupeEntity save(IndividualDemographicDedupeEntity entity);

}
