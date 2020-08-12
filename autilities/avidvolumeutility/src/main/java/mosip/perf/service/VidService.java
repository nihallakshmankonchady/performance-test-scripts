package mosip.perf.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mosip.perf.entity.VidEntity;
import mosip.perf.repository.VidEntityRepository;
import mosip.perf.util.DataUtil;

@Service
public class VidService extends DataUtil {

	@Autowired
	VidEntityRepository vidEntityRepo;

	public VidService() {

	}

	@Transactional
	public void saveVidEntity() {
		VidEntity entity = new VidEntity();
		entity.setVid(generateRandomVID());
		entity.setCreatedBy("p.data@perf.test");
		entity.setUpdatedBy("p.data@perf.test");
		LocalDateTime localTime = getCurrLocalDateTime();
		entity.setCreatedtimes(localTime);
		entity.setUpdatedtimes(localTime);
		entity.setStatus("DUMMY_DATA");
		entity.setIsDeleted(false);
		entity.setVidExpiry(getCurrLocalDateTime());

		System.out.println("data***************************************");
		System.out.println(entity.toString());

		vidEntityRepo.save(entity);

	}

	public void sayHello() {

		System.out.println("Hello from VidService class....");

	}

}
