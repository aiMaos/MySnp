package MySnp.Func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

import MySnp.Data.SnpList;
import MySnp.Data.SnpObject;
import MySnp.Data.StringList;
import MySnp.Data.Vcf.VcfRecord;
import MySnp.Db.SnpDbDerby;
import MySnp.Db.Table.TableAllAltDbsnp;
import MySnp.Db.Table.TableAllAltTT;
import MySnp.Db.Table.TableAllRsTT;

public class FuncSearch {

	public static void searchKeyworkFromTxt(String sFilePathSrc, StringList keywords) {
		long pos = 0;
		long filesize = new File(sFilePathSrc).length();
		String filename = new File(sFilePathSrc).getName();

		try {
			BufferedReader txt = new BufferedReader(
					new FileReader(sFilePathSrc));
	
			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			// read txt
			do {
				String sLine = txt.readLine();

				if (sLine == null) {
					break;
				} 
				pos += sLine.length();
				
				boolean found = true;
				for (int i=0; i<keywords.size(); i++) {
					if (sLine.indexOf(keywords.get(i)) < 0) {
						found = false;
					}
				}
				if (found) {
					System.out.println(sLine);									
				}
				
				counter ++;
				if (counter%1000000 == 0) {
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
				}
	
			} while (true);
		
			txt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done of searchKeyworkFromTxt - "+sFilePathSrc);

	}

	public static void searchCaseof2rsidFromVcf(String sFilePathSrc) {
		long pos = 0;
		long filesize = new File(sFilePathSrc).length();
		String filename = new File(sFilePathSrc).getName();

		try {
			BufferedReader txt = new BufferedReader(
					new FileReader(sFilePathSrc));
	
			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			
			// read txt
			do {
				String sLine = txt.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				} 
				pos += sLine.length();
				
				String[] sLines = sLine.split("\t");	
				if (sLines[2].split(",").length>1) {
					System.out.println(sLine);
				}
				
				counter ++;
				if (counter%1000000 == 0) {
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
				}
	
			} while (true);
		
			txt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done of searchCaseof2rsidFromVcf - "+sFilePathSrc);

	}
		
	public static void searchRsFromTable(String sTable, String sFilePathTo) {
		try {	
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePathTo));
			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			ResultSet rs = SnpDbDerby.getInstance().readTable(sTable);
			writer.write("# rsid	chromosome	position	genotype\n");

			while(rs.next()){
				counter ++;
				if (counter%1000000 == 0) {
					System.out.println(counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000);
				}
				SnpObject snp = null;
				if (TableAllRsTT.TABLE_ALL_RS_TT.equals(sTable)) {
					snp = TableAllRsTT.getSnpFromResultSet(rs);
				} else if ((TableAllAltDbsnp.TABLE_ALL_ALT_DBSNP.equals(sTable))) {
					snp = TableAllAltDbsnp.getSnpFromResultSet(rs);
				} else {
					System.out.println("unknown table: " + sTable);
				}

				if (snp.rsid.length()>1) {
					writer.write(snp.getStringAs23andme()+"\n");
				} else {
					continue;
				}
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of searchRsFromTable");
	}

	public static void searchGenotypeByRsForTT(String sFileFrom, String sFileTo) {
		try {	
			BufferedReader fileRs = new BufferedReader(
					new FileReader(sFileFrom));
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFileTo));
						
			writer.write("rsid	ref	alt	result\n");
			// read source file
			do {
				String rsid = fileRs.readLine();
				if (rsid == null) {
					break;
				} 
				
				rsid = rsid.toLowerCase();
				rsid = rsid.replaceAll(" ", "");
				
				if (rsid.startsWith("rs") == false) {
					System.out.println("skip line:" + rsid);
					continue;
				}
				
				SnpObject snpRef = TableAllAltDbsnp.getSnpByRsid(rsid).get(0);
				String sLineOut = rsid;
				sLineOut += "\t" + snpRef.chr + "\t" + snpRef.pos;
				// + "\t" + snpRef.ref + ">" + snpRef.alt
				SnpObject snp = TableAllAltTT.getSnpByChrAndPos(snpRef.chr, snpRef.pos);
				if (snp == null) {
					sLineOut += "\t" + snpRef.ref + snpRef.ref;
				} else {
					sLineOut += "\t" + snp.genotypeAsString;
					sLineOut += "\tdbsnp" + snpRef.getStringAsVcf();
					sLineOut += "\tzht" + snp.getStringAsVcf();
				}
//				SnpList snpList = TableAllAltTT.getSnpByRsid(rsid);
//				for (int i=0; i<snpList.size(); i++) {
//					SnpObject snp = snpList.get(i);
//					sLineOut += "\tmysnp\t" + snp.chr + "\t" + snp.pos + "\t" + snp.ref + ">" + snp.alt;
//					sLineOut += "\t" + snp.genotypeAsString;					
//				}
				writer.write(sLineOut + "\n");					
					
			} while (true);

			fileRs.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of searchGenotypeByRsForTT");
	}

	public static SnpList searchSnpByRs(String sTable, String rs) {
		SnpList snpList = new SnpList();
		
		if (TableAllRsTT.TABLE_ALL_RS_TT.equals(sTable)) {
			snpList = TableAllRsTT.getSnpByRsid(rs);
		} else if ((TableAllAltDbsnp.TABLE_ALL_ALT_DBSNP.equals(sTable))) {
			snpList = TableAllAltDbsnp.getSnpByRsid(rs);
		} else {
			System.out.println("unknown table: " + sTable);
		}
		
		System.out.println("done of searchSnpByRs");
		return snpList;
	}

	public static SnpObject searchSnpByChrAndPos(String sTable, String chr, String pos) {
		SnpObject snp = null;
		
		if (TableAllRsTT.TABLE_ALL_RS_TT.equals(sTable)) {
			snp = TableAllRsTT.getSnpByChrAndPos(chr, pos);
		} else if ((TableAllAltDbsnp.TABLE_ALL_ALT_DBSNP.equals(sTable))) {
			snp = TableAllAltDbsnp.getSnpByChrAndPos(chr, pos);
		} else {
			System.out.println("unknown table: " + sTable);
		}
		
		System.out.println("done of searchSnpByChrAndPos");
		return snp;
	}
}
