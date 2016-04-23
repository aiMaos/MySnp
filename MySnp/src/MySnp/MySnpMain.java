package MySnp;

import MySnp.Func.FuncConvert;
import MySnp.Func.FuncOthers;
import MySnp.Func.FuncSearch;

public class MySnpMain {

	public static void main(String[] args) {
//		splitVCF("D:\\\\hg19.vcf");

//		String sFileFrom = "D:\\\\0rs_src.txt";
//		String sFileTo = "D:\\\\0rs_out.txt";
//		ExomeRef.getAlleleFromExomeRef(sFileFrom, sFileTo);
		
		try {
			String sFileFrom;
			String sFileTo;

//			SnpObject snp = FuncSearch.searchSnpByChrAndPos(TableAllVarTT.TABLE_ALL_VAR_TT, chr, pos)
//			SnpList snpList = FuncSearch.searchSnpByRs(TableAllVarTT.TABLE_ALL_VAR_TT, "rs386419992");
//			SnpList snpList2 = FuncOthers.splitSnpByRsid(snpList);
//			for (int i=0;i<snpList2.size(); i++) {
//				System.out.println(snpList2.get(i).getStringAs23andme());
//			}
			
//			sFileFrom = "E:\\\\dbSNP144_20150605.vcf";
//			FuncConvert.convertVcfToDbAllAltDbsnp(sFileFrom);
			
//			sFileTo = "D:\\\\0rs_out.txt";
//			Convert.convertDbTo23andme(TableAllRsTT.TABLE_ALL_RS_TT, sFileTo);

//			sFileFrom = "E:\\\\dbSNP144_20150605.vcf";
//			StringList keywords = new StringList();
//			keywords.add("\t14889\t");	
//			FuncSearch.searchKeyworkFromTxt(sFileFrom, keywords);
			
//			sFileFrom = "D:\\WGS4_ZhangHanTao.AllChr.final.anno.vcf";
//			Search.searchCaseof2rsidFromVcf(sFileFrom);
			
			sFileFrom = "D:\\\\0rs_src.txt";
			sFileTo = "D:\\\\0rs_out.txt";
			FuncSearch.searchGenotypeByRsForTT(sFileFrom, sFileTo);
			
//			SnpDb.test();
//			convertVcfToDb(sFileFrom, SnpDb.TABLE_FULLGENE_TT);
//			getGenotypeFromRsForTT(sFileFrom,sFileTo);
//			convertDbTo23andme(SnpDb.TABLE_FULLGENE_TT, sFileTo);
//			getGenotypeFromRsForFamily(sFileFrom,sFileTo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		SnpDb.test();
		
//		ExomeRef.convertESP6500To23andme("D:\\ESP6500SI-V2-SSA137.GRCh38-liftover.snps_indels.txt", "D:\\ESP6500NoDup.txt");

		System.out.println("done of main");
	}
	

}





