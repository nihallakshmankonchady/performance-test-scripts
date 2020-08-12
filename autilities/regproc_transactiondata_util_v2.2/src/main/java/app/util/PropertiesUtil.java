package app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static String REGID_LOG_FILE;
	public static String EXCEL_FILE;
	public static String ENVIRONMENT;

	public void loadProperties(String configFile) {

		Properties properties = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ENVIRONMENT = properties.getProperty("ENVIRONMENT");
		REGID_LOG_FILE = properties.getProperty("REGID_LOG_FILE");
		EXCEL_FILE = properties.getProperty("EXCEL_FILE");
	}

}
