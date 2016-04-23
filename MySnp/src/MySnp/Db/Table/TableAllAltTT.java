package MySnp.Db.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

import MySnp.Data.SnpList;
import MySnp.Data.SnpObject;
import MySnp.Db.SnpDbDerby;
import MySnp.Db.Comm.SnpDatabase;
import MySnp.Func.FuncConvert;

public class TableAllAltTT {
	public static String TABLE_ALL_ALT_TT = "SNP_ALL_ALT_TT";
	public static String COLUMN_CHR = "CHR";
	public static String COLUMN_POS = "POS";
	public static String COLUMN_RSID = "RSID";
	public static String COLUMN_REF = "REF";
	public static String COLUMN_ALT = "ALT";
	public static String COLUMN_GENOTYPE = "GENOTYPE";
	public static String COLUMN_COMMENT = "COMMENT";
		
	public static void insertRecord(SnpObject snp) {

		String sql = "insert into "+TABLE_ALL_ALT_TT
				+"("
				+COLUMN_CHR+","
				+COLUMN_POS+","
				+COLUMN_RSID+","
				+COLUMN_REF+","
				+COLUMN_ALT+","
				+COLUMN_GENOTYPE
				+ ")"
				+ " values ("
				+ "'"+snp.chr+"',"
				+ "'"+snp.pos+"',"
				+ "'"+snp.rsid+"',"
				+ "'"+snp.ref+"',"
				+ "'"+snp.alt+"',"
				+ "'"+snp.genotypeAsIndex
				+"')";
		getDbInstance().executeUpdate(sql);
	}
	
	public static SnpObject getSnpByChrAndPos(String chr, String pos) {
		SnpObject snp = null;
		String sql = "select * from "+ TABLE_ALL_ALT_TT + " where (chr = '"+chr+"') AND (pos='"+pos+"')";
		ResultSet rs = getDbInstance().executeQuery(sql);
        try {
			if (rs.next()) {
				snp = getSnpFromResultSet(rs);
			} 
			if (rs.next()) {
				System.out.println("abnormal in getSnpByChrAndPos, TableAllRsTT");
			} else {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if (snp == null) {
			System.out.println(chr+","+pos + " is not in " + TABLE_ALL_ALT_TT);
        }
		return snp;
	}
	
	public static SnpList getSnpByRsid(String rsid) {
		SnpList snpList = new SnpList();
		String sql = "select * from "+ TABLE_ALL_ALT_TT + " where (rsid like '%"+rsid+"%')";
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
			System.out.println(rsid + " is not in " + TABLE_ALL_ALT_TT);
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
				snp.genotypeAsIndex = rs.getString(COLUMN_GENOTYPE);	
				snp.genotypeAsString = FuncConvert.convertGenotypeFromIndexToString(snp.ref, snp.alt, snp.genotypeAsIndex);
//				snp.genotypeAsString = rs.getString(COLUMN_GENOTYPE);
			} catch (SQLException e) {
				e.printStackTrace();
				snp = null;
			}
		}
		return snp;
	}
	
	public static SnpDatabase getDbInstance() {
		return SnpDbDerby.getInstance();
	}
	
}
