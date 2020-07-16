/**
 * 
 */
package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mosip.perf.entity.RegistrationStatusEntity;

/**
 * @author M1030608
 *
 */
@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationStatusEntity, String> {

	RegistrationStatusEntity save(RegistrationStatusEntity entity);

	// RegistrationStatusEntity findOne(String registrationId);

}
