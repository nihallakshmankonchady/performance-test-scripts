/**
 * 
 */
package mosip.perf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mosip.perf.entity.RegAbisRefEntity;

/**
 * @author M1030608
 *
 */
public interface RegAbisRefRepository extends JpaRepository<RegAbisRefEntity, String> {

	RegAbisRefEntity save(RegAbisRefEntity entity);
	
}
