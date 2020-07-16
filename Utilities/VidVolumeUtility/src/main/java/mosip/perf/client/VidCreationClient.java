package mosip.perf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mosip.perf.service.VidService;

@Component
public class VidCreationClient extends Thread {

	@Autowired
	private VidService vidService;

	public VidCreationClient() {

	}

	public void run() {
		System.out.println("Thread is running......................");
		vidService.sayHello();
		try {
			Thread.sleep(1900);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 10001; i++)
			vidService.saveVidEntity();
	}

}
