/**
 * 
 */
package mosip.perf.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mosip.perf.entity.UinEntity;
import mosip.perf.repository.UinEntityRepository;
import mosip.perf.util.DataUtil;

/**
 * @author madmin
 *
 */
@Component
public class UinService extends DataUtil {

	@Autowired
	private UinEntityRepository uinRepo;

	/**
	 * 
	 */
	public UinService() {

	}

	@Transactional
	public void saveUinEntity() {

		UinEntity uinEntity = new UinEntity();
		uinEntity.setUin(generateRandomUIN());
		uinEntity.setCreatedBy("perf.test@dummy.email");
		uinEntity.setUpdatedBy("p.data@perf.test");
		LocalDateTime localTime = getCurrLocalDateTime();
		uinEntity.setCreatedtimes(localTime);
		uinEntity.setUpdatedtimes(localTime);
		uinEntity.setStatus("DUMMY_DATA");
		uinEntity.setIsDeleted(false);

		uinRepo.save(uinEntity);

	}

}
