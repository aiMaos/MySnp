package MySnp.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import MySnp.MySnpConf;
import MySnp.Db.Comm.SnpDatabase;

public class SnpDbTest extends SnpDatabase{
	private static SnpDbTest instance = null;
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String protocol = "jdbc:derby:";
	private static String dbName = "D:\\SnpDbTest";

	public static SnpDbTest getInstance() {
		if (instance == null) {
			instance = new SnpDbTest();
		}
		return instance;
	}
	
	private SnpDbTest() {
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(protocol + dbName
					+ ";create=true");
			conn.setAutoCommit(MySnpConf.DB_AUTO_COMMIT);
			st = conn.createStatement();
			System.out.println("connected to " + dbName);

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	

	
}


