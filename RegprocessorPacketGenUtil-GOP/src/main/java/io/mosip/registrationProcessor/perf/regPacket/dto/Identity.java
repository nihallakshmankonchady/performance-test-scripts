package io.mosip.registrationProcessor.perf.regPacket.dto;

import java.util.List;

import io.mosip.dbdto.Address;
import io.mosip.dbdto.DocumentDetails;
import io.mosip.dbdto.FullName;
import io.mosip.dbdto.PlaceOfBirth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Identity {

	private FullName fullName;
	private FullName introducerFullName;
	
	private DocumentDetails documentDetails;
	
	private Address presentAddress;
	private Address permanentAddress;
	
	private PlaceOfBirth placeOfBirth;

	private String dateOfBirth;
	private Integer age;
	private List<FieldData> gender;
	private List<FieldData> residenceStatus;
	
	private List<FieldData> bloodType;
	
	private DocumentData proofOfRegistration;

	private BiometricData individualBiometrics;
	
	private Float IDSchemaVersion;
	

}
