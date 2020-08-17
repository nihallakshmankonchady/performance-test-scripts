package io.mosip.registrationProcessor.perf.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import io.mosip.dbentity.TokenGenerationEntity;
import io.mosip.dbentity.UsernamePwdTokenEntity;
import io.mosip.registrationProcessor.perf.service.SyncRequestCreater;
import io.mosip.registrationProcessor.perf.service.TestDataGenerator;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;
import io.mosip.registrationProcessor.perf.util.TokenGeneration;
import io.mosip.resgistrationProcessor.perf.dbaccess.DBUtil;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);

	public Main() {
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Receives only one parameter");
			System.out.println("packet_gen - To generate packets");
			System.out.println("sync_data - To generate sync data(test data to reg proc sync API)");
			System.out.println("test_data - To generate test data for idrepo create identity API");
		} else {
			String mode = args[0];
			switch (mode) {
			case "packet_gen":
				/*
				 * Generates packets and outputs packet file path and checksum to a file. It
				 * will also generates a file having a list of registration IDs
				 */
				try {
					createPackets();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;

			case "sync_data":
				/*
				 * Generates a test data file to be input to apache jmeter script to sync
				 * regsitration packets
				 */
				generateSyncData();
				break;
			case "test_data": // To generate test data for ID Repo create API this data is used for IDRepo and
								// IDA
				try {
					generateTestData();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Receives either of below parameters:");
				System.out.println("packet_gen: To generate packets");
				System.out.println(
						"sync_data: To generate sync data in a CSV file by using the checksums.txt file of generated packets");
				System.out.println("test_data: To generate test data in a CSV file");
			}

		}

	}

	private static void generateTestData() throws InterruptedException {

		TestDataClient testDataClient = new TestDataClient();

		String CONFIG_FILE = "config.properties";
		PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);
		List<Thread> listOfThreadsForTestData = new ArrayList<>();

		for (int i = 0; i < Integer.parseInt(prop.NUMBER_OF_THREADS_TEST_DATA); i++) {
			listOfThreadsForTestData.add(new Thread(testDataClient));
		}
		logger.info("Starting the threads");
		System.out.println("start");
		listOfThreadsForTestData.forEach(thread -> {
			thread.start();
		});
		logger.info("Terminating the threads");
		listOfThreadsForTestData.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	private static void generateSyncData() {

		SyncRequestCreater syncRequest = new SyncRequestCreater();

		String CONFIG_FILE = "config.properties";
		PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);
		syncRequest.createSyncRequestMaster(prop);
	}

	private static void createPackets() throws InterruptedException {
		System.out.println("main invoked");
		String authToken = "";
		try {
			authToken = generateCommonAuthTokenForAllThreads();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("authToken:- " + authToken);
		PacketCreationClient packetCreationClient = new PacketCreationClient(authToken);
		System.out.println("Threads objects creating");
		String CONFIG_FILE = "config.properties";
		PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);
		List<Thread> listOfThreadsForPacketCreation = new ArrayList<>();

		for (int i = 0; i < Integer.parseInt(prop.NUMBER_OF_THREADS_PACKET_CREATION); i++) {
			listOfThreadsForPacketCreation.add(new Thread(packetCreationClient));
		}
		logger.info("Starting the threads");
		listOfThreadsForPacketCreation.forEach(thread -> {
			thread.start();
		});
		logger.info("Terminating the threads");
		listOfThreadsForPacketCreation.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	private static String generateCommonAuthTokenForAllThreads() throws IOException {
		String CONFIG_FILE = "config.properties";

		PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);
		TokenGeneration generateToken = new TokenGeneration();

		String TOKEN_TYPE = "syncTokenGenerationFilePath";
		String tokenGenerationFilePath = generateToken.readPropertyFile(TOKEN_TYPE);
		String token = "";
		if (prop.AUTH_TYPE_CLIENTID_SECRETKEY) {
			TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
			tokenEntity = generateToken.createTokenGeneratorDtoForClientIdSecretKey(tokenGenerationFilePath);
			token = generateToken.getAuthTokenForClientIdSecretKey(tokenEntity, prop);
		} else {
			UsernamePwdTokenEntity tokenEntity1 = generateToken
					.createTokenGeneratorDtoForUserIdPassword(tokenGenerationFilePath);
			token = generateToken.getAuthTokenForUsernamePassword(tokenEntity1, prop);

		}

		return token;
	}

}
