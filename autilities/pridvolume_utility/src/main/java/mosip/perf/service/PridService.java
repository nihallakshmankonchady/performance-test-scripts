/**
 * 
 */
package mosip.perf.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mosip.perf.entity.PridEntity;
import mosip.perf.repository.PridEntityRepository;
import mosip.perf.util.DataUtil;

/**
 * @author madmin
 *
 */
@Component
public class PridService extends DataUtil {

	@Autowired
	private PridEntityRepository pridRepo;

	/**
	 * 
	 */
	public PridService() {

	}

	@Transactional
	public void saveUinEntity() {

		PridEntity uinEntity = new PridEntity();
		uinEntity.setPrid(generateRandomPrid());
		uinEntity.setCreatedBy("perf.test@dummy.email");
		uinEntity.setUpdatedBy("p.data@perf.test");
		LocalDateTime localTime = getCurrLocalDateTime();
		uinEntity.setCreatedtimes(localTime);
		uinEntity.setUpdatedtimes(localTime);
		uinEntity.setStatus("DUMMY_DATA");
		uinEntity.setIsDeleted(false);

		pridRepo.save(uinEntity);

	}

}
