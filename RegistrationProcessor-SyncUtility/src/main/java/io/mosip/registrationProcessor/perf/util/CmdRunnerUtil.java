package io.mosip.registrationProcessor.perf.util;

import java.io.*;

import org.springframework.stereotype.Component;

@Component
public class CmdRunnerUtil {

	public CmdRunnerUtil() {

	}

	public void executeCommand(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
