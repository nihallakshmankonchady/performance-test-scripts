/**
 * 
 */
package io.mosip.registrationProcessor.perf.client;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.mosip.registration.processor.core.code.ModuleName;
import io.mosip.registration.processor.core.code.RegistrationTransactionStatusCode;
import io.mosip.registration.processor.core.code.RegistrationTransactionTypeCode;
import io.mosip.registration.processor.core.status.util.StatusUtil;
import io.mosip.registration.processor.status.code.RegistrationStatusCode;
import io.mosip.registration.processor.status.dto.InternalRegistrationStatusDto;
import io.mosip.registration.processor.status.dto.SyncRegistrationDto;
import io.mosip.registration.processor.status.dto.SyncTypeDto;
import io.mosip.registrationProcessor.perf.util.FileUtil;
import io.mosip.registrationProcessor.perf.util.HashSequenceUtil;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;

/**
 * @author Gaurav
 *
 */
@Component
public class SyncDataClient {

	public List<SyncRegistrationDto> obtainSyncRegistrationDtos(PropertiesUtil prop) {

		FileUtil fileUtil = new FileUtil();
		String CONFIG_FILE = "testdata.properties";
		//PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);

		List<SyncRegistrationDto> list = new ArrayList<SyncRegistrationDto>();

		List<String> checksumLines = new ArrayList<>();
		String fileWithPacketChecksum = prop.CHECKSUM_LOGFILE_PATH;
		System.out.println("Read a property from properties file, " + fileWithPacketChecksum);
		try {
			checksumLines = fileUtil.readLinesOfFile(fileWithPacketChecksum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int count = 0;
		for (String fileLine : checksumLines) {
			if (count <= -1) {
				count++;
			} else {
				String[] literals = fileLine.split(",");
				String regid = literals[0];
				String checksum = literals[1];
				Long fileSize = Long.parseLong(literals[2]);
				try {
					SyncRegistrationDto syncDto = createRegistrationSyncDto(regid, checksum, fileSize, prop);
					list.add(syncDto);

					if (count >= prop.NUMBER_PACKETS_TO_SYNC) {
						break;
					}
					System.out.println(count + "th data added");
					count++;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		return list;
	}

	private SyncRegistrationDto createRegistrationSyncDto(String regId, String checksum, Long fileSize,
			PropertiesUtil prop) {

		SyncRegistrationDto syncRegistrationDto = new SyncRegistrationDto();
		syncRegistrationDto.setRegistrationId(regId);
		// syncRegistrationDto.setSyncType("NEW");
		syncRegistrationDto.setIsActive(true);
		syncRegistrationDto.setIsDeleted(false);
		syncRegistrationDto.setLangCode("eng");
		syncRegistrationDto.setSupervisorComment("APPROVED");
		syncRegistrationDto.setSupervisorStatus("APPROVED");
		syncRegistrationDto.setPacketSize(new BigInteger(fileSize.toString()));
		syncRegistrationDto.setCreateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		syncRegistrationDto.setUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		syncRegistrationDto.setDeletedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		String filePath = prop.NEW_PACKET_FOLDER_PATH + "zipped/" + regId + ".zip";
		File file = new File(filePath);
		// BigInteger.valueOf(file.length())
		syncRegistrationDto.setPacketSize(BigInteger.valueOf(file.length()));
		HashSequenceUtil hashSeqUtil = new HashSequenceUtil();
		if (file.exists()) {
			String packetHashValue = hashSeqUtil.getPacketHashSequence(file);
			syncRegistrationDto.setPacketHashValue(packetHashValue);
		}

		System.out.println("syncRegistrationDto.toString()");
		// System.out.println(syncRegistrationDto.toString());
		System.out.println(new Gson().toJson(syncRegistrationDto));

		return syncRegistrationDto;

	}

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

		System.out.println("InternalRegistrationStatusDto in String form:-");
		System.out.println(new Gson().toJson(dto));

		return dto;
	}

}
