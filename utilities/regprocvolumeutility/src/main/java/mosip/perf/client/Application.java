package mosip.perf.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import mosip.perf.service.RegistrationService;

@SpringBootApplication
@ComponentScan(basePackages = { "mosip.perf" })
@EntityScan(basePackages = { "mosip.perf.entity" })
@EnableJpaRepositories(basePackages = "mosip.perf.repository")
public class Application implements CommandLineRunner {

	@Autowired
	RegistrationService service;

	@Autowired
	Client clientMain;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Client clientMain = new Client();
		// clientMain.clientProcess();

		Thread t1 = new Thread(clientMain);
		

		t1.start();
		

		t1.join();
		
	}

}
