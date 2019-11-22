package dbtests;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.dbaccess.DBAccessImpl;
import app.dbaccess.DBUtil;
import app.entity.TransactionEntity;
import app.util.PropertiesUtil;

public class DBTest {

	private static DBAccessImpl dbAccess;
	private static Session session;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String configFile = "config.properties";
		new PropertiesUtil().loadProperties(configFile);
		session = DBUtil.obtainSession();
		dbAccess = new DBAccessImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRegTransactionForRegId() {
		String regid = "10002100323430620190705092118";
		List<TransactionEntity> list = dbAccess.getRegTransactionForRegId(session, regid);
		assertTrue(list.size() > 0);
		for (TransactionEntity te : list) {
			System.out.println(te.getTrntypecode() + " " + te.getCreateDateTime());
		}

	}

}
