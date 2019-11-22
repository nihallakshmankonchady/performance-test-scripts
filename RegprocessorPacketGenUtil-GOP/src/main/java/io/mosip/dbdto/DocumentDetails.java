package io.mosip.dbdto;

import java.util.List;

import io.mosip.registrationProcessor.perf.regPacket.dto.FieldData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetails {

	List<FieldData> documentType1;
	List<FieldData> documentType2;
	List<FieldData> documentType3;
	List<FieldData> documentNumber1;
	List<FieldData> documentNumber2;
	List<FieldData> documentNumber3;
	
}
