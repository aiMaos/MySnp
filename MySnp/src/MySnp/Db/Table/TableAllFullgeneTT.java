package MySnp.Db.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

import MySnp.Data.SnpObject;
import MySnp.Db.SnpDbDerby;
import MySnp.Db.SnpDbMySql;
import MySnp.Db.Comm.SnpDatabase;
import MySnp.Func.FuncConvert;

public class TableAllFullgeneTT {
	public static String TABLE_ALL_FULLGENE_TT = "SNP_ALL_FULLGENE_TT";
	public static String COLUMN_CHR = "CHR";
	public static String COLUMN_POS = "POS";
	public static String COLUMN_RSID = "RSID";
	public static String COLUMN_REF = "REF";
	public static String COLUMN_ALT = "ALT";
	public static String COLUMN_GENOTYPE = "GENOTYPE";
	public static String COLUMN_COMMENT = "COMMENT";
			
	public static void insertRecord(SnpObject snpSrc) {
		SnpObject snp = new SnpObject(snpSrc);
		if ((snp.ref != null) && (snp.ref.length()>300)) {
			snp.ref = ".";
		}
		if ((snp.alt != null) && (snp.alt.length()>300)) {
			snp.alt = ".";
		}
		String sql = "insert into "+TABLE_ALL_FULLGENE_TT
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
	
	public static SnpObject getSnpByRsid(String rsid) {
		SnpObject snp = null;
		String sql = "select * from "+ TABLE_ALL_FULLGENE_TT + " where (rsid='"+rsid+"')";
		ResultSet rs = getDbInstance().executeQuery(sql);
        try {
			if (rs.next()) {
				snp = getSnpFromResultSet(rs);
			} else {
				System.out.println(rsid + " is not in " + TABLE_ALL_FULLGENE_TT);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return snp;

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
			} catch (SQLException e) {
				e.printStackTrace();
				snp = null;
			}
		}
		return snp;
	}
	
	public static long getRecordCount() {
		long count = 0;
		
		String sql = "select count(*) CNT from "+ TABLE_ALL_FULLGENE_TT;
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
		return SnpDbMySql.getInstance();
	}	
}
