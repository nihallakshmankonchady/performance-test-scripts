package io.mosip.registrationProcessor.perf.util;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

	public int NUMBER_OF_TEST_DATA;
	public String REGID_LOG_FILE;
	public String PACKET_UPLOAD_TIME;
	public String TEST_DATA_CSV_FILE_PATH;
	public String NEW_PACKET_FOLDER_PATH;
	public String VALID_PACKET_PATH_FOR_PACKET_GENERATION;
	public String CHECKSUM_LOGFILE_PATH;
	public String SYNCDATA__FILE_PATH;
	public int NUMBER_OF_TEST_PACKETS;
	public boolean USE_PROXY;
	public String ENVIRONMENT;
	public String BASE_URL;
	public String ID_JSON_FILE;
	public String NUMBER_OF_THREADS_PACKET_CREATION;
	public String NUMBER_OF_THREADS_TEST_DATA;
	public boolean AUTH_TYPE_CLIENTID_SECRETKEY;
	public boolean DECRYPTION_NEEDED;
	public String AUTH_TOKEN;

	/*
	 * public void loadProperties(String configFile) {
	 * 
	 * Properties properties = new Properties(); InputStream inputStream =
	 * this.getClass().getClassLoader().getResourceAsStream(configFile); try {
	 * properties.load(inputStream); } catch (IOException e) { e.printStackTrace();
	 * }
	 * 
	 * TEST_DATA_CSV_FILE_PATH = properties.getProperty("TEST_DATA_CSV_FILE_PATH");
	 * NEW_PACKET_FOLDER_PATH = properties.getProperty("NEW_PACKET_FOLDER_PATH");
	 * VALID_PACKET_PATH_FOR_PACKET_GENERATION =
	 * properties.getProperty("PATH_FOR_VALID_REG_PACKETS"); CHECKSUM_LOGFILE_PATH =
	 * properties.getProperty("CHECKSUM_LOGFILE_PATH"); NUMBER_OF_TEST_PACKETS =
	 * Integer.parseInt(properties.getProperty("NUMBER_OF_TEST_PACKETS")); USE_PROXY
	 * = Boolean.parseBoolean(properties.getProperty("USE_PROXY"));
	 * SYNCDATA__FILE_PATH = properties.getProperty("SYNCDATA__FILE_PATH");
	 * ENVIRONMENT = properties.getProperty("ENVIRONMENT"); BASE_URL =
	 * properties.getProperty("BASE_URL"); PACKET_UPLOAD_TIME =
	 * properties.getProperty("PACKET_UPLOAD_TIME"); }
	 */
	public PropertiesUtil(String configFile) {
		Properties properties = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		REGID_LOG_FILE = properties.getProperty("REGID_LOG_FILE");
		TEST_DATA_CSV_FILE_PATH = properties.getProperty("TEST_DATA_CSV_FILE_PATH");
		NEW_PACKET_FOLDER_PATH = properties.getProperty("NEW_PACKET_FOLDER_PATH");
		VALID_PACKET_PATH_FOR_PACKET_GENERATION = properties.getProperty("PATH_FOR_VALID_REG_PACKETS");
		CHECKSUM_LOGFILE_PATH = properties.getProperty("CHECKSUM_LOGFILE_PATH");
		NUMBER_OF_TEST_PACKETS = Integer.parseInt(properties.getProperty("NUMBER_OF_TEST_PACKETS"));
		USE_PROXY = Boolean.parseBoolean(properties.getProperty("USE_PROXY"));
		SYNCDATA__FILE_PATH = properties.getProperty("SYNCDATA__FILE_PATH");
		ENVIRONMENT = properties.getProperty("ENVIRONMENT");
		BASE_URL = properties.getProperty("BASE_URL");
		PACKET_UPLOAD_TIME = properties.getProperty("PACKET_UPLOAD_TIME");
		ID_JSON_FILE = properties.getProperty("ID_JSON_FILE");
		NUMBER_OF_THREADS_PACKET_CREATION = properties.getProperty("NUMBER_OF_THREADS_PACKET_CREATION");
		NUMBER_OF_THREADS_TEST_DATA = properties.getProperty("NUMBER_OF_THREADS_TEST_DATA");
		AUTH_TOKEN = properties.getProperty("AUTH_TOKEN");
		AUTH_TYPE_CLIENTID_SECRETKEY = Boolean.parseBoolean(properties.getProperty("AUTH_TYPE_CLIENTID_SECRETKEY"));
		DECRYPTION_NEEDED = Boolean.parseBoolean(properties.getProperty("DECRYPTION_NEEDED"));
		NUMBER_OF_TEST_DATA = Integer.parseInt(properties.getProperty("NUMBER_OF_TEST_DATA"));
	}
}
