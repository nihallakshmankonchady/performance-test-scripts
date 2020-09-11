package mosip.perf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mosip.perf.dao.RegistrationDao;
import mosip.perf.entity.AbisRequestEntity;
import mosip.perf.entity.AbisResponseEntity;
import mosip.perf.entity.RegistrationStatusEntity;
import mosip.perf.entity.TransactionEntity;

@Component
public class RegistrationService {

	@Autowired
	RegistrationDao dao;

	public void addRegistrationEntity() {

		for (int i = 0; i < 10; i++) {
			try {
				RegistrationStatusEntity registrationStatusEntity = dao.addRegistrationEntity();
				TransactionEntity trEntity = dao.addRegistrationTransaction(registrationStatusEntity);
				dao.addSyncRegistrationEntity(registrationStatusEntity);
				dao.addIndividualDemoDedupeRecords(registrationStatusEntity);
				dao.addRegDemoDedupRecords(registrationStatusEntity, trEntity);

				dao.addRegManualVerification(registrationStatusEntity);
				dao.addRegLostUinDet(registrationStatusEntity);
				dao.addRegBioRefEntity(registrationStatusEntity);

				AbisRequestEntity abisRequestEntity = dao.addAbisRequest();
				if (abisRequestEntity != null) {
					AbisResponseEntity abisResponseEntity = dao.addAbisResponse(abisRequestEntity);
					dao.addAbisResponseDet(abisRequestEntity, abisResponseEntity);
				}
				System.out.println("Iteration " + i + " for thread " + Thread.currentThread().getName() + " over");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
