package io.mosip.registrationProcessor.perf.client;

import io.mosip.registration.processor.core.code.ModuleName;
import io.mosip.registration.processor.core.code.RegistrationTransactionStatusCode;
import io.mosip.registration.processor.core.code.RegistrationTransactionTypeCode;
import io.mosip.registration.processor.core.status.util.StatusUtil;
import io.mosip.registration.processor.status.code.RegistrationStatusCode;
import io.mosip.registration.processor.status.dto.InternalRegistrationStatusDto;
import io.mosip.registration.processor.status.dto.SyncTypeDto;

public class RegistrationStatusClient {

	public InternalRegistrationStatusDto obtainRegistrationStatusData(String regId) {
		InternalRegistrationStatusDto dto = new InternalRegistrationStatusDto();

		dto.setRegistrationId(regId);
		dto.setLatestTransactionTypeCode(RegistrationTransactionTypeCode.PACKET_RECEIVER.toString());
		dto.setRegistrationStageName(ModuleName.PACKET_RECEIVER.name());
		dto.setRegistrationType(SyncTypeDto.NEW.getValue());
		dto.setReferenceRegistrationId(null);
		dto.setStatusCode(RegistrationStatusCode.PROCESSING.toString());
		dto.setLangCode("eng");
		dto.setStatusComment(StatusUtil.PACKET_RECEIVED.getMessage());
		dto.setSubStatusCode(StatusUtil.PACKET_RECEIVED.getCode());
		dto.setReProcessRetryCount(0);
		dto.setLatestTransactionStatusCode(RegistrationTransactionStatusCode.SUCCESS.toString());
		dto.setIsActive(true);
		dto.setCreatedBy("MOSIP_SYSTEM");
		dto.setIsDeleted(false);

		return dto;
	}

}
