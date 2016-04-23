package MySnp.Db.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

import MySnp.Data.SnpList;
import MySnp.Data.SnpObject;
import MySnp.Db.SnpDbDerby;
import MySnp.Db.SnpDbMySql;
import MySnp.Db.SnpDbTest;
import MySnp.Db.Comm.SnpDatabase;
import MySnp.Func.FuncConvert;

public class TableAllAltDbsnp {
	public static String TABLE_ALL_ALT_DBSNP = "SNP_ALL_ALT_DBSNP";
	public static String COLUMN_CHR = "CHR";
	public static String COLUMN_POS = "POS";
	public static String COLUMN_RSID = "RSID";
	public static String COLUMN_REF = "REF";
	public static String COLUMN_ALT = "ALT";
	public static String COLUMN_COMMENT = "COMMENT";
		
	public static boolean insertRecord(SnpObject snpSrc) {
		SnpObject snp = new SnpObject(snpSrc);
		if ((snp.ref != null) && (snp.ref.length()>300)) {
			snp.ref = ".";
		}
		if ((snp.alt != null) && (snp.alt.length()>300)) {
			snp.alt = ".";
		}
		String sql = "insert into "+TABLE_ALL_ALT_DBSNP
				+"("
				+COLUMN_CHR+","
				+COLUMN_POS+","
				+COLUMN_RSID+","
				+COLUMN_REF+","
				+COLUMN_ALT
				+ ")"
				+ " values ("
				+ "'"+snp.chr+"',"
				+ "'"+snp.pos+"',"
				+ "'"+snp.rsid+"',"
				+ "'"+snp.ref+"',"
				+ "'"+snp.alt
				+"')";
		return getDbInstance().executeUpdate(sql);
	}
	
	public static SnpObject getSnpByChrAndPos(String chr, String pos) {
		SnpObject snp = null;
		String sql = "select * from "+ TABLE_ALL_ALT_DBSNP + " where (chr = '"+chr+"') AND (pos='"+pos+"')";
		ResultSet rs = getDbInstance().executeQuery(sql);
        try {
			if (rs.next()) {
				snp = getSnpFromResultSet(rs);
			} 
			if (rs.next()) {
				System.out.println("abnormal in getSnpByChrAndPos, "+TABLE_ALL_ALT_DBSNP);
			} else {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if (snp == null) {
			System.out.println(chr+","+pos + " is not in " + TABLE_ALL_ALT_DBSNP);
        }
		return snp;
	}
	
	public static SnpList getSnpByRsid(String rsid) {
		SnpList snpList = new SnpList();
		String sql = "select * from "+ TABLE_ALL_ALT_DBSNP + " where (rsid = '"+rsid+"')";
		ResultSet rs = getDbInstance().executeQuery(sql);
        try {
			while (rs.next()) {
				SnpObject snp = getSnpFromResultSet(rs);
				String[] rsList = snp.rsid.split(",");
				for (int i=0; i<rsList.length; i++) {
					if (rsid.equalsIgnoreCase(rsList[i])) {
						snpList.add(snp);
					}
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if (snpList.size() == 0) {
			System.out.println(rsid + " is not in " + TABLE_ALL_ALT_DBSNP);
        }
		return snpList;

	}
	
	public static SnpObject getSnpFromResultSet(ResultSet rs) {
		SnpObject snp = null;
		if (rs != null) {
			snp = new SnpObject();
			try {
				snp.chr = rs.getString(COLUMN_CHR);
				snp.pos = rs.getString(COLUMN_POS);
				snp.rsid = rs.getString(COLUMN_RSID);
				snp.ref = rs.getString(COLUMN_REF);
				snp.alt = rs.getString(COLUMN_ALT);
				snp.genotypeAsIndex = "00";	
				snp.genotypeAsString = FuncConvert.convertGenotypeFromIndexToString(snp.ref, snp.alt, snp.genotypeAsIndex);
			} catch (SQLException e) {
				e.printStackTrace();
				snp = null;
			}
		}
		return snp;
	}
	
	public static long getRecordCount() {
		long count = 0;
		
		String sql = "select count(*) CNT from "+ TABLE_ALL_ALT_DBSNP;
		ResultSet rs = getDbInstance().executeQuery(sql);
        try {
			if (rs.next()) {
				count = rs.getLong("CNT");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}

        return count;
	}
	
	public static SnpDatabase getDbInstance() {
		return SnpDbTest.getInstance();
//		return SnpDbDerby.getInstance();
	}
	
}
