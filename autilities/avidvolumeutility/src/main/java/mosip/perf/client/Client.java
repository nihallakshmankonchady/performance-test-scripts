/**
 * 
 */
package mosip.perf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import mosip.perf.service.VidService;
import mosip.perf.util.Config;

/**
 * @author M1030608
 *
 */
@Component
//@Scope("prototype")
public class Client implements Runnable {

	@Autowired
	VidService service;

	@Override
	public void run() {

		for(int i=0; i < Config.ITERATIONS_COUNT; i++)
		service.saveVidEntity();

	}

}
