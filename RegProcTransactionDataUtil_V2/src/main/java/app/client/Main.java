/**
 * 
 */
package app.client;

import org.hibernate.Session;

import app.dbaccess.DBUtil;
import app.service.DataProcessor;
import app.util.PropertiesUtil;

/**
 * @author Gaurav Sharan
 *
 */
public class Main {

	private static DataProcessor dataProcessor;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		dataProcessor = new DataProcessor();
		String configFile = "config.properties";
		new PropertiesUtil().loadProperties(configFile);
		Session session = DBUtil.obtainSession();
		dataProcessor.processData(session);

	}

}
