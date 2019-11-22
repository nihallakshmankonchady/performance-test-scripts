/**
 * 
 */
package mosip.perf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import mosip.perf.service.RegistrationService;

/**
 * @author M1030608
 *
 */
@Component
@Scope("prototype")
public class Client extends Thread {

	@Autowired
	RegistrationService service;

	@Override
	public void run() {

		service.addRegistrationEntity();

	}

//	public void clientProcess() {
//		service.addRegistrationEntity();
//	}

}
