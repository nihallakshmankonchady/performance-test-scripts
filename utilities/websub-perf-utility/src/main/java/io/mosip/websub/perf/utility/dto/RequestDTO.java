package io.mosip.websub.perf.utility.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

	@NotBlank
	private String timestamp;
	
	@NotBlank
	private String messageString;
	

}
