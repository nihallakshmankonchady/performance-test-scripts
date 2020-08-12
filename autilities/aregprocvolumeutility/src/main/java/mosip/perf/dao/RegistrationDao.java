package mosip.perf.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mosip.perf.entity.AbisRequestEntity;
import mosip.perf.entity.AbisRequestPKEntity;
import mosip.perf.entity.AbisResponseDetEntity;
import mosip.perf.entity.AbisResponseDetPKEntity;
import mosip.perf.entity.AbisResponseEntity;
import mosip.perf.entity.AbisResponsePKEntity;
import mosip.perf.entity.IndividualDemographicDedupeEntity;
import mosip.perf.entity.IndividualDemographicDedupePKEntity;
import mosip.perf.entity.ManualVerificationEntity;
import mosip.perf.entity.ManualVerificationPKEntity;
import mosip.perf.entity.RegBioRefEntity;
import mosip.perf.entity.RegBioRefPKEntity;
import mosip.perf.entity.RegDemoDedupeListEntity;
import mosip.perf.entity.RegDemoDedupeListPKEntity;
import mosip.perf.entity.RegLostUinDetEntity;
import mosip.perf.entity.RegLostUinDetPKEntity;
import mosip.perf.entity.RegistrationStatusEntity;
import mosip.perf.entity.SyncRegistrationEntity;
import mosip.perf.entity.TransactionEntity;
import mosip.perf.repository.AbisApplicationRepository;
import mosip.perf.repository.AbisRequestRepository;
import mosip.perf.repository.AbisResponseDetRepository;
import mosip.perf.repository.AbisResponseRepository;
import mosip.perf.repository.IndividualDemographicDedupeRepository;
import mosip.perf.repository.ManualVerificationRepository;
import mosip.perf.repository.RegBioRefRepository;
import mosip.perf.repository.RegDemoDedupeListRepository;
import mosip.perf.repository.RegLostUinDetRepository;
import mosip.perf.repository.RegistrationRepository;
import mosip.perf.repository.SyncRegistrationRepository;
import mosip.perf.repository.TransactionRepository;
import mosip.perf.util.RegProcsessorUtil;

@Service
public class RegistrationDao {

//	@Autowired
//	RegistrationDaoImpl dao;

	@Autowired(required = true)
	RegistrationRepository registrationRepo;

	@Autowired(required = true)
	TransactionRepository transactionRepo;

	@Autowired(required = true)
	RegProcsessorUtil util;

	@Autowired
	SyncRegistrationRepository regSyncRepository;

	@Autowired
	IndividualDemographicDedupeRepository indDemoRepo;

	@Autowired
	RegDemoDedupeListRepository regDemodedupRepo;

	@Autowired
	ManualVerificationRepository manualVerificationRepo;

	@Autowired
	RegBioRefRepository regBioRefRepo;

	@Autowired
	AbisApplicationRepository abisApplicationRepo;

	@Autowired
	AbisRequestRepository abisRequestRepo;

	@Autowired
	AbisResponseRepository abisResponseRepo;

	@Autowired
	AbisResponseDetRepository AbisResponseDetRepo;

	@Autowired
	RegLostUinDetRepository regLostUinDetRepo;

	private int counter;

	@Transactional
	public RegistrationStatusEntity addRegistrationEntity() {

		RegistrationStatusEntity entity = new RegistrationStatusEntity();

		String reg_id = util.generateRegId();
		entity.setId(reg_id);
		entity.setRegistrationType("NEW");
		entity.setStatusCode("PROCESSING");
		entity.setLangCode("eng");
		entity.setStatusComment("Perf test data");
		entity.setCreatedBy("MOSIP_SYSTEM");
		entity.setApplicantType("Foreigner");
		entity.setLatestTransactionTypeCode("PACKET_RECEIVER");
		entity.setLatestTransactionStatusCode("PROCESSING");
		LocalDateTime createDateTime = util.getCurrLocalDateTime();
		entity.setCreateDateTime(createDateTime);
		entity.setUpdatedBy("MOSIP_SYSTEM");
		LocalDateTime updateDateTime = util.getCurrLocalDateTime();
		entity.setUpdateDateTime(updateDateTime);
		entity.setIsDeleted(false);
		LocalDateTime deletedDateTime = util.getCurrLocalDateTime();
		entity.setDeletedDateTime(deletedDateTime);
		entity.setRegProcessRetryCount(0);
		entity.setRegistrationStageName("PacketReceiverStage");
		entity.setIsActive(true);

		String latestRegistrationTransactionId = util.getRandomUuid();
		entity.setLatestRegistrationTransactionId(latestRegistrationTransactionId);
		entity.setLatestTransactionTypeCode("PACKET_RECEIVER");
		entity.setLatestTransactionStatusCode("PROCESSING");
		LocalDateTime latestTransactionTimes = util.getCurrLocalDateTime();
		entity.setLatestTransactionTimes(latestTransactionTimes);
		entity.setRetryCount(0);

		registrationRepo.save(entity);
		// System.out.println("Registration created");
		// addRegistrationTransaction(entity);
		return entity;

	}

	public TransactionEntity addRegistrationTransaction(RegistrationStatusEntity entity) {

		TransactionEntity trEntity = new TransactionEntity();
		trEntity.setId(util.getRandomUuid());
		trEntity.setRegistrationId(entity.getId());
		trEntity.setTrntypecode("PACKET_RECEIVER");
		trEntity.setRemarks("Performance tsting data");
		trEntity.setReferenceId(util.generateRegId());
		trEntity.setReferenceIdType("Added registration record");
		trEntity.setStatusCode("PROCESSING");
		trEntity.setLangCode("eng");
		trEntity.setStatusComment("Packet is in PACKET_RECEIVED status");
		trEntity.setCreatedBy("MOSIP_SYSTEM");
		trEntity.setCreateDateTime(util.getCurrLocalDateTime());
		trEntity.setUpdatedBy("MOSIP_SYSTEM ");
		trEntity.setSubStatusCode("DEFAULT");
		trEntity.setUpdateDateTime(util.getCurrLocalDateTime());
		trEntity.setIsDeleted(false);
		transactionRepo.save(trEntity);
		// System.out.println("transaction created");
		return trEntity;

	}

	public SyncRegistrationEntity addSyncRegistrationEntity(RegistrationStatusEntity entity) {
		SyncRegistrationEntity syncEntity = new SyncRegistrationEntity();
		syncEntity.setId(util.getRandomUuid());
		syncEntity.setCreateDateTime(util.getCurrLocalDateTime());
		syncEntity.setRegistrationId(entity.getId());
		syncEntity.setRegistrationType("NEW");
		syncEntity.setPacketHashValue(util.getPacketHashValue());
		syncEntity.setPacketSize(new BigInteger("3500"));
		syncEntity.setLangCode("eng");
		syncEntity.setCreatedBy("MOSIP");
		syncEntity.setStatusCode("APPROVED");
		syncEntity.setStatusComment("APPROVED");
		syncEntity.setUpdatedBy("MOSIP_SYSTEM ");
		syncEntity.setUpdateDateTime(util.getCurrLocalDateTime());
		syncEntity.setSupervisorStatus("APPROVED");
		syncEntity.setSupervisorComment("APPROVED");
		syncEntity.setUpdateDateTime(util.getCurrLocalDateTime());
		syncEntity.setIsDeleted(false);
		regSyncRepository.save(syncEntity);
		return syncEntity;
	}

	public void addIndividualDemoDedupeRecords(RegistrationStatusEntity entity) {
		IndividualDemographicDedupeEntity demoDedupEntity = new IndividualDemographicDedupeEntity();
		IndividualDemographicDedupePKEntity id = new IndividualDemographicDedupePKEntity();
		id.setRegId(entity.getId());
		id.setLangCode("eng");
		demoDedupEntity.setId(id);
		String fullname = Base64.encodeBase64URLSafeString((util.generateFullName().getBytes()));
		String dob = Base64.encodeBase64URLSafeString((util.generateDOB().getDate().getBytes()));
		String gender = Base64.encodeBase64URLSafeString((util.genrateGenderEng().getBytes()));
		// demoDedupEntity.setCrBy("SYSTEM");
		demoDedupEntity.setCrDtimes(util.getCurrLocalDateTime());
		demoDedupEntity.setIsActive(true);
		demoDedupEntity.setIsDeleted(false);
		demoDedupEntity.setUpdDtimes(util.getCurrLocalDateTime());
		demoDedupEntity.setDelDtimes(util.getCurrLocalDateTime());
		demoDedupEntity.setName(fullname);
		demoDedupEntity.setDob(dob);
		demoDedupEntity.setGender(gender);
		indDemoRepo.save(demoDedupEntity);
	}

	public void addRegDemoDedupRecords(RegistrationStatusEntity entity, TransactionEntity trEntity) {

		RegDemoDedupeListEntity regDemoEntity = new RegDemoDedupeListEntity();
		RegDemoDedupeListPKEntity id = new RegDemoDedupeListPKEntity();
		id.setRegtrnId(trEntity.getId());
		id.setMatchedRegId(entity.getId());
		regDemoEntity.setId(id);
		regDemoEntity.setCrDtimes(util.getCurrLocalDateTime());
		regDemoEntity.setUpdDtimes(util.getCurrLocalDateTime());
		regDemoEntity.setDelDtimes(util.getCurrLocalDateTime());
		regDemoEntity.setIsDeleted(false);
		regDemoEntity.setRegId(entity.getId());
		regDemodedupRepo.save(regDemoEntity);
	}

	public void addRegManualVerification(RegistrationStatusEntity entity) {

		ManualVerificationEntity manVerificationEntity = new ManualVerificationEntity();
		ManualVerificationPKEntity id = new ManualVerificationPKEntity();
		id.setRegId(entity.getId());
		id.setMatchedRefId(util.generateRegId());
		id.setMatchedRefType("rid");
		manVerificationEntity.setId(id);
		manVerificationEntity.setIsActive(true);
		manVerificationEntity.setCrBy("SYSTEM");
		manVerificationEntity.setStatusCode("PENDING");
		manVerificationEntity.setReasonCode("Potential Match");
		manVerificationEntity.setStatusComment("Assigned to manual adjudication for perf test data");
		manVerificationEntity.setTrnTypCode("BIO");
		manVerificationEntity.setLangCode("eng");
		manVerificationEntity.setCrDtimes(Timestamp.valueOf(util.getCurrLocalDateTime()));
		manVerificationEntity.setUpdDtimes(Timestamp.valueOf(util.getCurrLocalDateTime()));
		manVerificationEntity.setIsActive(true);
		manualVerificationRepo.save(manVerificationEntity);
	}

	public void addRegLostUinDet(RegistrationStatusEntity entity) {

		RegLostUinDetEntity regLostUinDetEntity = new RegLostUinDetEntity();
		RegLostUinDetPKEntity id = new RegLostUinDetPKEntity();
		id.setRegId(entity.getId());
		regLostUinDetEntity.setId(id);
		regLostUinDetEntity.setLatestRegId(util.generateRegId());
		regLostUinDetEntity.setCrBy("SYSTEM");
		regLostUinDetEntity.setCrDtimes(util.getCurrLocalDateTime());
		regLostUinDetEntity.setIsDeleted(false);
		regLostUinDetEntity.setUpdBy("SYSTEM");
		regLostUinDetEntity.setUpdDtimes(util.getCurrLocalDateTime());
		regLostUinDetEntity.setDelDtimes(util.getCurrLocalDateTime());
		regLostUinDetRepo.save(regLostUinDetEntity);
	}

	public void addRegBioRefEntity(RegistrationStatusEntity entity) {
		RegBioRefEntity bioRefEntity = new RegBioRefEntity();
		RegBioRefPKEntity id = new RegBioRefPKEntity();
		id.setRegId(entity.getId());
		bioRefEntity.setId(id);
		bioRefEntity.setCrBy("SYSTEM");
		bioRefEntity.setUpdBy("SYSTEM");
		bioRefEntity.setCrDtimes(util.getCurrLocalDateTime());
		bioRefEntity.setBioRefId(util.getRandomUuid());
		bioRefEntity.setIsActive(true);
		bioRefEntity.setIsDeleted(false);
		bioRefEntity.setDelDtimes(util.getCurrLocalDateTime());
		regBioRefRepo.save(bioRefEntity);
	}

//	public void addAbisApplication() {
//		AbisApplicationEntity abisEntity = new AbisApplicationEntity();
//		AbisApplicationPKEntity id = new AbisApplicationPKEntity();
//		id.setCode("ABIS_" + Thread.currentThread().getName() + "_" + counter);
//		synchronized (this) {
//			counter++;
//		}
//		id.setLangCode("eng");
//		abisEntity.setName("ABIS_" + Thread.currentThread().getName() + "_" + counter);
//		abisEntity.setDescr("ABIS" + Thread.currentThread().getName() + "_" + counter);
//		abisEntity.setStatusCode("ACTIVE");
//		abisEntity.setCrDtimes(util.getCurrLocalDateTime());
//		abisEntity.setCrBy("SYSTEM");
//		abisEntity.setUpdDtimes(util.getCurrLocalDateTime());
//		abisEntity.setUpdBy("MOSIP_SYSTEM");
//		abisEntity.setIsDeleted(false);
//		abisApplicationRepo.save(abisEntity);
//	}

	public AbisRequestEntity addAbisRequest() {
		AbisRequestEntity abisReqEntity = new AbisRequestEntity();
		AbisRequestPKEntity id = new AbisRequestPKEntity();
		id.setId(util.getRandomUuid());
		abisReqEntity.setId(id);
		abisReqEntity.setReqBatchId(util.getRandomUuid());
		abisReqEntity.setAbisAppCode(util.getRandomAppCode());
		abisReqEntity.setRequestType(util.getRandomRequestType());
		abisReqEntity.setRequestDtimes(util.getCurrLocalDateTime());
		abisReqEntity.setBioRefId(util.getRandomUuid());
		abisReqEntity.setRefRegtrnId(util.getRandomUuid());
		abisReqEntity.setStatusCode("IN_PROCESS");
		abisReqEntity.setStatusComment("peformance testing volume data");
		abisReqEntity.setLangCode("eng");
		abisReqEntity.setCrBy("SYSTEM");
		abisReqEntity.setCrDtimes(util.getCurrLocalDateTime());
		abisReqEntity.setUpdBy("MOSIP_SYSTEM");
		abisReqEntity.setUpdDtimes(util.getCurrLocalDateTime());
		abisReqEntity.setIsDeleted(false);
		abisReqEntity.setDelDtimes(util.getCurrLocalDateTime());
		abisRequestRepo.save(abisReqEntity);
		return abisReqEntity;
	}

	public AbisResponseEntity addAbisResponse(AbisRequestEntity abisReqEntity) {
		AbisResponseEntity abisRespEntity = new AbisResponseEntity();
		AbisResponsePKEntity id = new AbisResponsePKEntity();
		id.setId(util.getRandomUuid());
		abisRespEntity.setAbisRequest(abisReqEntity.getId().getId());
		abisRespEntity.setId(id);
		abisRespEntity.setCrBy("SYSTEM");
		abisRespEntity.setRespDtimes(util.getCurrLocalDateTime());
		abisRespEntity.setStatusCode("SUCCESS");
		abisRespEntity.setStatusComment("PT volume creation data");
		abisRespEntity.setLangCode("eng");
		abisRespEntity.setCrDtimes(util.getCurrLocalDateTime());
		abisRespEntity.setUpdBy("SYSTEM");
		abisRespEntity.setUpdDtimes(util.getCurrLocalDateTime());
		abisRespEntity.setIsDeleted(false);
		abisRespEntity.setDelDtimes(util.getCurrLocalDateTime());
		abisResponseRepo.save(abisRespEntity);
		// System.out.println("ABIS response created");
		return abisRespEntity;
	}

	public void addAbisResponseDet(AbisRequestEntity abisReqEntity, AbisResponseEntity abisRespEntity) {

		AbisResponseDetEntity abisResponseDetEntity = new AbisResponseDetEntity();
		AbisResponseDetPKEntity id = new AbisResponseDetPKEntity();
		id.setAbisRespId(abisRespEntity.getId().getId());
		id.setMatchedBioRefId(abisReqEntity.getBioRefId());
		abisResponseDetEntity.setId(id);
		abisResponseDetEntity.setIsDeleted(false);
		abisResponseDetEntity.setCrBy("SYSTEM");
		abisResponseDetEntity.setCrDtimes(util.getCurrLocalDateTime());
		abisResponseDetEntity.setScore(95);
		abisResponseDetEntity.setUpdBy("SYSTEM");
		abisResponseDetEntity.setUpdDtimes(util.getCurrLocalDateTime());
		abisResponseDetEntity.setIsDeleted(false);
		abisResponseDetEntity.setDelDtimes(util.getCurrLocalDateTime());
		AbisResponseDetRepo.save(abisResponseDetEntity);
		// System.out.println("ABIS response det created");
	}

}
