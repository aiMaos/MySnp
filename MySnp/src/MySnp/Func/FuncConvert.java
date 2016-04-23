package MySnp.Func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;

import MySnp.Data.SnpObject;
import MySnp.Data.Vcf.VcfRecord;
import MySnp.Db.SnpDbDerby;
import MySnp.Db.Table.TableAllAltDbsnp;
import MySnp.Db.Table.TableAllAltTT;
import MySnp.Db.Table.TableAllFullgeneTT;
import MySnp.Db.Table.TableAllRsTT;

public class FuncConvert {
	
	private final static boolean CONVERT_VCF_TO_DB_ONLY_FOR_RSID = false;

	public static String convertGenotypeFromIndexToString(String sRef, String sAlt, String sIndex) {
		String[] sAlts = sAlt.split(",");
		int iGenotypeCount = sAlts.length+1;
		String sGenotype[] = new String[iGenotypeCount];
		String sMyGenoRsult = "";
		sGenotype[0] = sRef;
		
		for (int i=1;i<iGenotypeCount;i++) {
			sGenotype[i] = sAlts[i-1];
		}
		
		for (int i=0;i<sIndex.length();i++) {
			if (sIndex.substring(i, i+1).equals(".")) {				
				sMyGenoRsult += ".";
			} else {
				int iIndex = Integer.parseInt(sIndex.substring(i, i+1));
				sMyGenoRsult += sGenotype[iIndex];				
			}
		}
		
		return sMyGenoRsult;
	}

	public static void convertVcfToDbAllFullgeneTT(String sVcfPath) {
		try {	
			long pos = 0;
			long filesize = new File(sVcfPath).length();
			String filename = new File(sVcfPath).getName();
			long currentTableCount = TableAllFullgeneTT.getRecordCount();
			System.out.println("Current TableAllAltDbsnp record count="+currentTableCount);
					
			BufferedReader vcf = new BufferedReader(
					new FileReader(sVcfPath));
			TableAllFullgeneTT.getDbInstance().setAutoCommit(false);
			
			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			// read VCF
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}
				pos += sLine.length();
				counter ++;

				if (counter > currentTableCount) {
					// 1	900340	rs749745144	C	G	.	.	RS=749745144;RSPOS=900340;dbSNPBuildID=144;SSR=0;SAO=0;VP=0x0500000a0005000002000100;WGT=1;VC=SNV;INT;R5;ASP
					SnpObject snp = new VcfRecord(sLine).getSnpObject();
					TableAllFullgeneTT.insertRecord(snp);										
				}
				
				if (counter%100000 == 0) {
					TableAllFullgeneTT.getDbInstance().commit();
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
				}

			} while (true);
			
			vcf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of convertVcfToDbAllFullgeneTT - "+sVcfPath);
	
	}
	
	public static void convertVcfToDbAllRsTT(String sVcfPath) {
		try {	
			long pos = 0;
			long filesize = new File(sVcfPath).length();
			String filename = new File(sVcfPath).getName();
					
			BufferedReader vcf = new BufferedReader(
					new FileReader(sVcfPath));

			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			// read VCF
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}
				pos += sLine.length();
				// chr10	93945	.	G	A	3181.78	.	AC=1;AF=0.500;AN=2;BaseQRankSum=-0.091;ClippingRankSum=1.748;DP=339;FS=13.699;MLEAC=1;MLEAF=0.500;MQ=42.01;MQRankSum=-0.667;QD=9.39;ReadPosRankSum=1.567;SOR=0.645	GT:AD:DP:GQ:PL	0/1:213,126:339:99:3210,0,6429
				SnpObject snp = new VcfRecord(sLine).getSnpObject();
				if (snp.rsid.equals(".")) {
					// skip this record
				} else {
					TableAllRsTT.insertRecord(snp);					
				}
				
				counter ++;
				if (counter%1000000 == 0) {
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
				}

			} while (true);
			
			vcf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of convertVcfToDbAllRsTT - "+sVcfPath);
	}
	
	public static void convertVcfToDbAllAltDbsnp(String sVcfPath) {
		try {	
			long pos = 0;
			long filesize = new File(sVcfPath).length();
			String filename = new File(sVcfPath).getName();
			long currentTableCount = TableAllAltDbsnp.getRecordCount();
			System.out.println("Current TableAllAltDbsnp record count="+currentTableCount);

			BufferedReader vcf = new BufferedReader(
					new FileReader(sVcfPath));
			TableAllAltDbsnp.getDbInstance().setAutoCommit(false);

			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			// read VCF
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}
				pos += sLine.length();
				counter ++;

				if (counter > currentTableCount) {
					// 1	900340	rs749745144	C	G	.	.	RS=749745144;RSPOS=900340;dbSNPBuildID=144;SSR=0;SAO=0;VP=0x0500000a0005000002000100;WGT=1;VC=SNV;INT;R5;ASP
					SnpObject snp = new VcfRecord(sLine).getSnpObject();
					TableAllAltDbsnp.insertRecord(snp);					
				}
				
				if (counter%100000 == 0) {
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
					TableAllAltDbsnp.getDbInstance().commit();
				}

			} while (true);
			
			TableAllAltDbsnp.getDbInstance().commit();
			vcf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of convertVcfToDbAllAltDbsnp - "+sVcfPath);
	}
	
	public static void convertVcfToDbAllAltTT(String sVcfPath) {
		try {	
			long pos = 0;
			long filesize = new File(sVcfPath).length();
			String filename = new File(sVcfPath).getName();
					
			BufferedReader vcf = new BufferedReader(
					new FileReader(sVcfPath));

			int counter =0;
			long timer_startpoint = System.currentTimeMillis();
			
			// read VCF
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}
				pos += sLine.length();
				// chr10	93945	.	G	A	3181.78	.	AC=1;AF=0.500;AN=2;BaseQRankSum=-0.091;ClippingRankSum=1.748;DP=339;FS=13.699;MLEAC=1;MLEAF=0.500;MQ=42.01;MQRankSum=-0.667;QD=9.39;ReadPosRankSum=1.567;SOR=0.645	GT:AD:DP:GQ:PL	0/1:213,126:339:99:3210,0,6429
				SnpObject snp = new VcfRecord(sLine).getSnpObject();
				if (snp.alt.equals(".")) {
					// skip this record
				} else {
					TableAllAltTT.insertRecord(snp);					
				}
				
				counter ++;
				if (counter%1000000 == 0) {
					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
				}

			} while (true);
			
			vcf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done of convertVcfToDbAllAltTT - "+sVcfPath);
	}

	public static void convertDbTo23andme(String sTable, String sFilePathTo) {
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
		System.out.println("done of convertDbTo23andme");
	}


	
}
