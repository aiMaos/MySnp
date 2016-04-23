package MySnp.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import MySnp.MySnpConf;
import MySnp.Db.Comm.SnpDatabase;

public class SnpDbDerby extends SnpDatabase{
	private static SnpDbDerby instance = null;
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String protocol = "jdbc:derby:";
	private static String dbName = "D:\\SnpDb";

	public static SnpDbDerby getInstance() {
		if (instance == null) {
			instance = new SnpDbDerby();
		}
		return instance;
	}
	
	private SnpDbDerby() {
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


