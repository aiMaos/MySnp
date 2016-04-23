package MySnp.Func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import MySnp.Data.SnpList;
import MySnp.Data.SnpObject;
import MySnp.Db.SnpDbDerby;
import MySnp.Util.ExtensionFileFilter;

public class FuncOthers {
	
	public static SnpList splitSnpByRsid(SnpObject snp) {
		SnpList snpList = new SnpList();
		String[] rsList = snp.rsid.split(",");
		
		for (int i=0; i<rsList.length; i++) {
			SnpObject snp_new = new SnpObject(snp);
			snp_new.rsid = rsList[i];
			snpList.add(snp_new);
		}
 		
		return snpList;
	}
	
	public static SnpList splitSnpByRsid(SnpList snpListSrc) {
		SnpList snpListDst = new SnpList();
		
		for (int i=0; i<snpListSrc.size(); i++) {
			SnpList snpListTmp = splitSnpByRsid(snpListSrc.get(i));
			snpListDst.addAll(snpListTmp);
		}
		return snpListDst;
	}
	
	public static void convertESP6500To23andme(String sFilePathFrom, String sFilePathTo) {
		try {	
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePathTo));
			writer.write("# rsid	chromosome	position	Alleles\n");
			String sAvoidDuplicate = "";
			
			File file = new File(sFilePathFrom);
			File[] files = file.listFiles(new ExtensionFileFilter("txt"));
			
			for (int i=0;i<files.length;i++) {
				String sFile = files[i].getPath();
				System.out.println(sFile);
				BufferedReader reader = new BufferedReader(new FileReader(sFile));

				// read ESP6500 exome
				do {
					String sLine = reader.readLine();
					if (sLine == null) {
						break;
					} else if (sLine.startsWith("#")) {
						continue;
					}		
					
					// 17:5994 rs375149461 dbSNP_138 G>A
					String[] sLines = sLine.split(" ");	
					SnpObject snp = new SnpObject();
					snp.chr = sLines[0].split(":")[0];
					snp.pos = sLines[0].split(":")[1];
					snp.rsid = sLines[1];
					snp.ref = sLines[3];
					if (sAvoidDuplicate.equals(snp.pos)) {
						// skip this record
					} else {
						sAvoidDuplicate = snp.pos;
						writer.write(snp.rsid+"\t"+snp.chr+"\t"+snp.pos+"\t"+snp.ref+"\n");						
					}
				} while (true);
				
				reader.close();				
			}
			
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("convertESP6500To23andme done");
		
	}
	
	private static SnpObject getExomeRecord(String sRsid) {
		SnpObject snp = new SnpObject();
		snp.rsid = sRsid;
		snp.chr = "/";
		snp.pos = "/";
		snp.genotypeAsString = "/";
		snp.ref = "/";
		return snp;
	}
	
//	private static void generateExomeDb(String sFilePath) throws Exception {
//		BufferedReader file1 = new BufferedReader(
//				new FileReader(sFilePath));
//		SnpDbDerby db = SnpDbDerby.getInstance();
//
//		int counter =0;
//		
//		do {
//			String sLine = file1.readLine();
//			if (sLine == null) {
//				break;
//			} else if (sLine.startsWith("#")) {
//				continue;
//			}		
//			// rs140739101	1	69428	T>G
//			String[] sLines = sLine.split("\t");	
//			SnpObject snp = new SnpObject();
//			snp.rsid = sLines[0];
//			snp.chr = sLines[1];
//			snp.pos = sLines[2];
//			snp.ref = sLines[3];
//			db.insert2TableExome(snp);
//			counter ++;
//			if (counter%1000 == 0) {
//				System.out.println(counter);
//			}
//		} while (true);
//		file1.close();
//		db.close();
//	}

//	public static void getAlleleFromExomeRef(String sFileFrom, String sFileTo) {
//		try {	
//			SnpDbDerby db = SnpDbDerby.getInstance();
//
//			BufferedReader fileRs = new BufferedReader(
//					new FileReader(sFileFrom));
//			BufferedWriter writer = new BufferedWriter(new FileWriter(sFileTo));
//			
//			writer.write("rsid	allele\n");
//			// read source file
//			do {
//				String rsid = fileRs.readLine();
//				if (rsid == null) {
//					break;
//				} 
//				String allele = db.getColumnByRsid(SnpDbDerby.TABLE_EXOME_REF, SnpDbDerby.COLUMN_ALLELE, rsid);
//				writer.write(rsid + "/t" + allele);
//			} while (true);
//
//			fileRs.close();
//			writer.close();
//			db.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("done of getGenotypeFromRs");
//	}

	private static SnpList getSnpListFromFile(String sFilePath) throws Exception {
		BufferedReader file1 = new BufferedReader(
				new FileReader(sFilePath));
		SnpList SnpList1 = new SnpList();
		do {
			String sLine = file1.readLine();
			if (sLine == null) {
				break;
			} else if (sLine.startsWith("#")) {
				continue;
			}		
			// rs75062661	1	69511	GG
			try {
				sLine.split("\t");
			} catch (Exception e) {
				System.out.println(sFilePath);
				System.out.println(sLine);
			}
			String[] sLines = sLine.split("\t");	
			SnpObject snp = new SnpObject();
			snp.rsid = sLines[0];
			snp.chr = sLines[1];
			snp.pos = sLines[2];
			snp.genotypeAsString = sLines[3];
			if (snp.genotypeAsString.length() == 2) {
				char char1 = snp.genotypeAsString.charAt(0);
				char char2 = snp.genotypeAsString.charAt(1);
				if (char1 > char2) {
					snp.genotypeAsString = String.valueOf(char2) + String.valueOf(char1);
				}
			}
			if (snp.genotypeAsString.equals("--")) {
				// not called by 23andme
				continue;
			}
			SnpList1.add(snp);
		} while (true);
		file1.close();
		return SnpList1;
	}
	
	
//	private static void getGenotypeFromRsForTT(String sFileFrom, String sFileTo) {
//		try {	
//			BufferedReader fileRs = new BufferedReader(
//					new FileReader(sFileFrom));
//			BufferedWriter writer = new BufferedWriter(new FileWriter(sFileTo));
//			SnpDbDerby db = SnpDbDerby.getInstance();
////			db.init();
//						
//			writer.write("rsid	allele	fullgene	23andme\n");
//			// read source file
//			do {
//				String rsid = fileRs.readLine();
//				if (rsid == null) {
//					break;
//				} 
//				String sLineOut = rsid;
//				int i;
//				
//				rsid = rsid.toLowerCase();
//				rsid = rsid.replaceAll(" ", "");
//				
//				String allele = db.getColumnByRsid(SnpDbDerby.TABLE_EXOME_REF, SnpDbDerby.COLUMN_ALLELE, rsid);
//				sLineOut += "\t" + allele;
//				
//				String result_fullgene = db.getColumnByRsid(SnpDbDerby.TABLE_FULLGENE_TT, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				sLineOut += "\t" + result_fullgene;
//
//				String result_23andme = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_TT, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				sLineOut += "\t" + result_23andme;
//				
//				writer.write(sLineOut + "\n");
//			} while (true);
//
//			fileRs.close();
//			writer.close();
//			db.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("done of getGenotypeFromRsForTT");
//	}

//	private static void getGenotypeFromRsForFamily(String sFileFrom, String sFileTo) {
//		try {	
//			BufferedReader fileRs = new BufferedReader(
//					new FileReader(sFileFrom));
//			BufferedWriter writer = new BufferedWriter(new FileWriter(sFileTo));
//			SnpDbDerby db = SnpDbDerby.getInstance();
//						
//			writer.write("rsid	TT	ZWH	GJ	ZZ	GLY\n");
//			// read source file
//			do {
//				String rsid = fileRs.readLine();
//				if (rsid == null) {
//					break;
//				} 
//				String sLineOut = rsid;
//				int i;
//				
//				rsid = rsid.toLowerCase();
//				rsid = rsid.replaceAll(" ", "");
//				
////				String allele = db.getRecordByRsid(SnpDb.TABLE_EXOME_REF, rsid);
////				sLineOut += "\t" + allele;
//				
//				String tt_genotype = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_TT, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				String zwh_genotype = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_ZWH, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				String gj_genotype = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_GJ, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				String zz_genotype = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_ZZ, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//				String gly_genotype = db.getColumnByRsid(SnpDbDerby.TABLE_23ANDME_GLY, SnpDbDerby.COLUMN_GENOTYPE, rsid);
//
//				sLineOut += "\t" + tt_genotype;
//				sLineOut += "\t" + zwh_genotype;
//				sLineOut += "\t" + gj_genotype;
//				sLineOut += "\t" + zz_genotype;
//				sLineOut += "\t" + gly_genotype;
//
//				writer.write(sLineOut + "\n");
//			} while (true);
//
//			fileRs.close();
//			writer.close();
//			db.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("done of getGenotypeFromRsForFamily");
//	}

	private static void convertHg19To23andme(String sFilePathVCF, String sFilePathTo) {
		try {	
			BufferedReader vcf = new BufferedReader(
					new FileReader(sFilePathVCF));
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePathTo));
//				String sLineSrc = null;
			
			// read VCF
			SnpList vcfSnpList = new SnpList();
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}		
				
//				System.out.println(sLine);
				// 1	10177	rs367896724	A	AC
				try {
					sLine.split("\t");
				} catch (Exception e) {
					System.out.println(sLine);
				}
				String[] sLines = sLine.split("\t");	
				SnpObject snp = new SnpObject();
				snp.chr = sLines[0];
				snp.pos = sLines[1];
				snp.rsid = sLines[2];
				snp.genotypeAsString = sLines[3];
				if (Character.isDigit(snp.chr.charAt(0))){
					snp.genotypeAsString = snp.genotypeAsString+snp.genotypeAsString;
				}
				snp.ref = sLines[4];
				vcfSnpList.add(snp);
			} while (true);
			
			vcf.close();				
			
			writer.write("# rsid	chromosome	position	genotype	genetype_ref\n");
			for (int i=0; i<vcfSnpList.size(); i++) {
				SnpObject snp = vcfSnpList.get(i);

				if (snp.rsid.equals(".")) {
					System.out.println("no rsid for " + snp.pos);
				} else {
					writer.write(snp.getStringAs23andme()+"\n");
				}
				
			}
			
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("convertHg19To23andme done");
		
	}

	private static void convertWegeneTo23andme(String sFilePathVCF, String sFilePathTo) {
		try {	
			BufferedReader vcf = new BufferedReader(
					new FileReader(sFilePathVCF));
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePathTo));
//				String sLineSrc = null;
			
			// read VCF
			SnpList vcfSnpList = new SnpList();
			do {
				String sLine = vcf.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}		
				
//				System.out.println(sLine);
				// chr10	93945	.	G	A	3181.78	.	AC=1;AF=0.500;AN=2;BaseQRankSum=-0.091;ClippingRankSum=1.748;DP=339;FS=13.699;MLEAC=1;MLEAF=0.500;MQ=42.01;MQRankSum=-0.667;QD=9.39;ReadPosRankSum=1.567;SOR=0.645	GT:AD:DP:GQ:PL	0/1:213,126:339:99:3210,0,6429
				String[] sLines = sLine.split("\t");	
				String sGenoType = "";
				try {
					sGenoType = sLines[9].split(":")[0];					
					sGenoType = sGenoType.replaceAll("/", "");
					sGenoType = sGenoType.replaceAll("0", sLines[3]);
					sGenoType = sGenoType.replaceAll("1", sLines[4]);
				} catch (Exception e) {
					System.out.println(sLine);
					break;
				}
				
				SnpObject snp = new SnpObject();
				snp.chr = sLines[0].substring(3);
				snp.pos = sLines[1];
				snp.genotypeAsString = sGenoType;
				snp.rsid = sLines[2];
				vcfSnpList.add(snp);
//				System.out.println(sGenoType);
//					break;
				
			} while (true);
			
			vcf.close();				
			
			writer.write("# rsid	chromosome	position	genotype\n");
			for (int i=0; i<vcfSnpList.size(); i++) {
				SnpObject snp = vcfSnpList.get(i);

				if (snp.rsid.equals(".")) {
					System.out.println("no rsid for " + snp.pos);
				} else {
					writer.write(snp.rsid+"\t"+snp.chr+"\t"+snp.pos+"\t"+snp.genotypeAsString+"\n");
				}
				
			}
			
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
	
	
//	private static void convertVcfToDb(String sFilePathVCF, String sTable) {
//		try {	
//			BufferedReader vcf = new BufferedReader(
//					new FileReader(sFilePathVCF));
//			SnpDbDerby db = SnpDbDerby.getInstance();
//			int counter =0;
//			
//			// read VCF
//			do {
//				String sLine = vcf.readLine();
//				if (sLine == null) {
//					break;
//				} else if (sLine.startsWith("#")) {
//					continue;
//				}		
//				// chr10	93945	.	G	A	3181.78	.	AC=1;AF=0.500;AN=2;BaseQRankSum=-0.091;ClippingRankSum=1.748;DP=339;FS=13.699;MLEAC=1;MLEAF=0.500;MQ=42.01;MQRankSum=-0.667;QD=9.39;ReadPosRankSum=1.567;SOR=0.645	GT:AD:DP:GQ:PL	0/1:213,126:339:99:3210,0,6429
//				String[] sLines = sLine.split("\t");	
//				String sGenoType = "";
//				try {
//					String allele0 = sLines[3];
//					String allele1 = "";
//					String allele2 = "";
//					if (sLines[4].indexOf(",")>0) {
//						allele1 = sLines[4].split(",")[0];
//						allele2 = sLines[4].split(",")[1];
//					} else {
//						allele1 = sLines[4];
//					}
//					
//					sGenoType = sLines[9].split(":")[0];					
//					sGenoType = sGenoType.replaceAll("/", "");
//					sGenoType = sGenoType.replaceAll("0", allele0);
//					sGenoType = sGenoType.replaceAll("1", allele1);
//					sGenoType = sGenoType.replaceAll("2", allele2);
//					if (sGenoType.length()>50) {
//						sGenoType = "--";
//					}
//				} catch (Exception e) {
//					System.out.println(sLine);
//					break;
//				}
//				
//				SnpObject snp = new SnpObject();
//				snp.rsid = sLines[2];
//				snp.chr = sLines[0].substring(3);
//				snp.pos = sLines[1];
//				snp.genotype = sGenoType;
//				for (int i=0;i<sLines[2].split(",").length;i++) {
//					snp.rsid = sLines[2].split(",")[i];
//					db.insertSnp2Table(sTable, snp);
//				}
//				counter ++;
//				if (counter%1000 == 0) {
//					System.out.println(counter);
//				}
////				snp.print();
////				if(counter > 10) {
////					break;
////				}
//
//			} while (true);
//			
//			vcf.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("done of convertVcfToDb");
//	}
	
	
	private static void splitVCF(String sFilePathFrom) {
		try {
//			String sFilePathFrom = "D:\\\\gatk.variation.vcf";
			String sVcfHead = "#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	15B0053674";

			BufferedReader reader = new BufferedReader(
					new FileReader(sFilePathFrom));
			BufferedWriter writerChr[] =  new BufferedWriter[25];
			for (int i=1; i<=22; i++) {
				writerChr[i] = new BufferedWriter(new FileWriter("D:\\\\chr"+i+".vcf"));
				writerChr[i].write(sVcfHead);
			}
			BufferedWriter writerChrM = new BufferedWriter(new FileWriter("D:\\\\chrM.vcf"));
			BufferedWriter writerChrX = new BufferedWriter(new FileWriter("D:\\\\chrX.vcf"));
			BufferedWriter writerChrY = new BufferedWriter(new FileWriter("D:\\\\chrY.vcf"));
			writerChrM.write(sVcfHead);
			writerChrX.write(sVcfHead);
			writerChrY.write(sVcfHead);

			String sLineSrc = null;
			
			do {
				sLineSrc = reader.readLine();
				if (sLineSrc == null) {
					break;
				} 
				if (!sLineSrc.startsWith("#")) {
					String[] sLines = sLineSrc.split("\t");
//					for (int j=0; j<sLines.length; j++) {
//						System.out.println(sLines[j]);						
//					}
					BufferedWriter writer = null;
					if (sLines[0].length()>5) {
						System.out.println(sLineSrc);
						continue;		
					} else if ((sLines[0].equals("chrX")) || (sLines[0].equals("X"))) {
						writer = writerChrX;
					} else if ((sLines[0].equals("chrY")) || (sLines[0].equals("Y"))) {
						writer = writerChrY;
					} else if ((sLines[0].equals("chrM")) || (sLines[0].equals("M"))) {
						writer = writerChrM;
					} else {
						int chr = 0;
						if (sLines[0].startsWith("chr")) {
							chr = Integer.parseInt(sLines[0].substring(3));							
						} else if (Character.isDigit(sLines[0].charAt(0))) {
							chr = Integer.parseInt(sLines[0]);
						} else {
							System.out.println(sLineSrc);							
						}
						if ((chr<=22) && (chr>=1)) {
							writer = writerChr[chr];							
						} else {
							System.out.println(sLineSrc);
							continue;
						}
					}
					writer.write(sLineSrc + "\n");
				}
				
			} while (true);
			
			reader.close();
			
			for (int i=1; i<=22; i++) {
				writerChr[i].close();
			}
			writerChrM.close();			
			writerChrX.close();
			writerChrY.close();
			System.out.println("splitVCF done");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void removeDuplicateRecord(String sFilePathFrom, String sFilePathTo) {
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(sFilePathFrom));
			BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePathTo));
			String sLineSrc = "";
			String sKey = "";
			
			do {
				sLineSrc = reader.readLine();
				if (sLineSrc == null) {
					break;
				} 
				if (sLineSrc.length() > 0) {
					String[] sLines = sLineSrc.split("\t");
					if (sKey.equals(sLines[0])) {
						// skip this line
					} else {
						sKey = sLines[0];
						writer.write(sLineSrc + "\n");
					}
				}
				
			} while (true);
			
			reader.close();
			writer.close();
			System.out.println("removeDuplicateRecord done");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void merge() {
		try {
			String sFilePathFrom = "D:\\\\gatk.variation.vcf";
			String sVcfHead = "#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	15B0053674";

			BufferedReader reader = new BufferedReader(
					new FileReader(sFilePathFrom));
			BufferedWriter writerChr[] =  new BufferedWriter[25];
			for (int i=1; i<=22; i++) {
				writerChr[i] = new BufferedWriter(new FileWriter("D:\\\\chr"+i+".vcf"));
				writerChr[i].write(sVcfHead);
			}
			BufferedWriter writerChrM = new BufferedWriter(new FileWriter("D:\\\\chrM.vcf"));
			BufferedWriter writerChrX = new BufferedWriter(new FileWriter("D:\\\\chrX.vcf"));
			BufferedWriter writerChrY = new BufferedWriter(new FileWriter("D:\\\\chrX.vcf"));
			writerChrM.write(sVcfHead);
			writerChrX.write(sVcfHead);
			writerChrY.write(sVcfHead);

			String sLineSrc = null;
			
			do {
				sLineSrc = reader.readLine();
				if (sLineSrc == null) {
					break;
				} 
				if (sLineSrc.startsWith("chr")) {
					String[] sLines = sLineSrc.split("\t");
//					for (int j=0; j<sLines.length; j++) {
//						System.out.println(sLines[j]);						
//					}
					BufferedWriter writer = null;
					if (sLines[0].equals("chrX")) {
						writer = writerChrX;
					} else if (sLines[0].equals("chrY")) {
						writer = writerChrY;
					} else if (sLines[0].equals("chrM")) {
						writer = writerChrM;
					} else {
						int chr = Integer.parseInt(sLines[0].substring(3));
						writer = writerChr[chr];
					}
					writer.write(sLineSrc + "\n");
				}
				
			} while (true);
			
			reader.close();
			
			for (int i=1; i<=22; i++) {
				writerChr[i].close();
			}
			writerChrX.close();
			writerChrY.close();
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	private static void compareHdAnd23(){
		try {	
			BufferedReader file_hd = new BufferedReader(
					new FileReader("D:\\\\0snp_hd.txt"));
			BufferedReader file_23andme = new BufferedReader(
					new FileReader("D:\\\\0snp_23andme.txt"));
			
			// read hd
			SnpList SnpListHd = new SnpList();
			do {
				String sLine = file_hd.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}		
				// rs75062661	1	69511	GG
				String[] sLines = sLine.split("\t");	
				SnpObject snp = new SnpObject();
				snp.rsid = sLines[0];
				snp.chr = sLines[1];
				snp.pos = sLines[2];
				snp.genotypeAsString = sLines[3];
				if (snp.genotypeAsString.length() == 2) {
					char char1 = snp.genotypeAsString.charAt(0);
					char char2 = snp.genotypeAsString.charAt(1);
					if (char1 > char2) {
						System.out.println("old " + snp.genotypeAsString);
						snp.genotypeAsString = String.valueOf(char2) + String.valueOf(char1);
						System.out.println("new " + snp.genotypeAsString);
					}
				}
				SnpListHd.add(snp);
			} while (true);
			file_hd.close();
			
			// read 23andme
			SnpList SnpList23 = new SnpList();
			do {
				String sLine = file_23andme.readLine();
				if (sLine == null) {
					break;
				} else if (sLine.startsWith("#")) {
					continue;
				}		
				// rs75062661	1	69511	GG
				String[] sLines = sLine.split("\t");	
				SnpObject snp = new SnpObject();
				snp.rsid = sLines[0];
				snp.chr = sLines[1];
				snp.pos = sLines[2];
				snp.genotypeAsString = sLines[3];
				SnpList23.add(snp);
			} while (true);
			file_23andme.close();
			
			// compare
			for (int i=0; i<SnpList23.size(); i++) {
				for (int j=0; j<SnpListHd.size(); j++) {
					if (SnpList23.get(i).rsid.equals(SnpListHd.get(j).rsid)) {
						// check genotype
						if (SnpList23.get(i).genotypeAsString.equals(SnpListHd.get(j).genotypeAsString)) {

						} else {
							System.out.println(SnpList23.get(i).rsid + " " + SnpList23.get(i).genotypeAsString + " " + SnpListHd.get(j).genotypeAsString);
						}
						SnpListHd.remove(j);
						break;
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("compareHdAnd23 done");
	}

}
