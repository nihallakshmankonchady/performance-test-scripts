/**
 * 
 */
package mosip.perf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mosip.perf.service.UinService;
import mosip.perf.util.Config;

/**
 * @author M1030608
 *
 */
@Component
//@Scope("prototype")
public class Client implements Runnable {

	@Autowired
	UinService service;

	@Override
	public void run() {

		for (int i = 0; i < Config.ITERATIONS_COUNT; i++) {
			service.saveUinEntity();
			System.out.println("Iteration " + i + " over, for thread " + Thread.currentThread().getName());
		}

	}

}
