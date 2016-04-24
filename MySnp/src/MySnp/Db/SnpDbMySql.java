package MySnp.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import MySnp.MySnpConf;
import MySnp.Db.Comm.SnpDatabase;

public class SnpDbMySql extends SnpDatabase {
	private static SnpDbMySql instance = null;
    public static final String url = "jdbc:mysql://localhost:3306/snpdb";  
    public static final String driver = "com.mysql.jdbc.Driver";  
    public static final String user = "admin";  
    public static final String password = "admin"; 

	public static SnpDbMySql getInstance() {
		if (instance == null) {
			instance = new SnpDbMySql();
		}
		return instance;
	}
	
	private SnpDbMySql() {
		try {
            Class.forName(driver).newInstance();//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接 
			conn.setAutoCommit(MySnpConf.DB_AUTO_COMMIT);
			st = conn.createStatement();
			System.out.println("connected to " + url);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
