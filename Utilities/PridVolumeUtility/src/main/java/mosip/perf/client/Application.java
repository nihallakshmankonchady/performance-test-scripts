package mosip.perf.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import mosip.perf.service.PridService;


@SpringBootApplication
@ComponentScan(basePackages = { "mosip.perf" })
@EntityScan(basePackages = { "mosip.perf.entity" })
@EnableJpaRepositories(basePackages = "mosip.perf.repository")
public class Application implements CommandLineRunner {

	@Autowired
	Client clientMain;

	@Autowired
	PridService service;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		int THREAD_COUNT = 120;
		int ITERATIONS = 500000;
		List<Thread> threads = new ArrayList<Thread>(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			threads.add(new Thread(clientMain));
		}

		threads.forEach(thread -> {
			thread.start();
		});

		threads.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

	}

}
