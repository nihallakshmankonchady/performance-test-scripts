package io.mosip.registration.processor.perf.packet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegPacketSyncDto {

	private String regId;
	private String syncData;
	private String packetPath;
	private String referenceId;

}
