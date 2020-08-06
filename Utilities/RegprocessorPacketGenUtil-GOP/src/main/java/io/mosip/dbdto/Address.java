package io.mosip.dbdto;

import java.util.List;

import io.mosip.registrationProcessor.perf.regPacket.dto.FieldData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	List<FieldData> addressLine1;
	List<FieldData> addressLine2;
	List<FieldData> addressLine3;
	List<FieldData> addressLine4;
	List<FieldData> country;
	List<FieldData> province;
	List<FieldData> city;
	List<FieldData> region;
}
