package MySnp.Db.Comm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SnpDatabase {
	protected Connection conn = null;
	protected Statement st = null;
	
	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void execute(String sql) {
		try {
			if (sql != null) {
				st.execute(sql);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public boolean executeUpdate(String sql) {
		boolean ret = false;
		try {
			if (sql != null) {
				st.executeUpdate(sql);
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
		return ret;
	}
	
	public ResultSet executeQuery(String sql) {
		ResultSet result = null;
		try {
			if (sql != null) {
				result = st.executeQuery(sql);				
			}
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}	
		return result;
	}
		
	public ResultSet readTable(String table) {
		ResultSet rs = null;
		try {
			rs = st.executeQuery("select * from "+table);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return rs;
	}
		
	public void setAutoCommit(boolean isAutoCommit) {
		try {
			conn.setAutoCommit(isAutoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void commit() {
		try {
			long time = System.currentTimeMillis();
			conn.commit();
			time = (System.currentTimeMillis()-time);
			System.out.println("commit: "+ time+" ms");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
