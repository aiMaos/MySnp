package MySnp.Data.Vcf;

import MySnp.Data.SnpObject;
import MySnp.Func.FuncConvert;

public class VcfRecord {
//	#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	WGS4_ZhangHanTao
//	chrM	2	.	A	.	.	.	AN=2;DP=551	GT:DP:RGQ	0/0:551:99
	
//	#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	WGS4_ZhangHanTao
//	chrM	73	rs3087742	A	G	98717.77	VQSRTrancheSNP99.90to100.00	AC=2;AF=1.00;AN=2;BaseQRankSum=0.693;ClippingRankSum=1.66;DB;DP=2935;FS=0.000;MLEAC=2;MLEAF=1.00;MQ=58.25;MQRankSum=-1.195e+00;QD=33.80;ReadPosRankSum=1.05;SOR=0.374;VQSLOD=-1.100e+04;culprit=DP;ANN=G|upstream_gene_variant|MODIFIER|MT-TF|ENSG00000210049|transcript|ENST00000387314|Mt_tRNA||n.-1A>G|||||504|,G|upstream_gene_variant|MODIFIER|MT-RNR1|ENSG00000211459|transcript|ENST00000389680|Mt_rRNA||n.-1A>G|||||575|,G|upstream_gene_variant|MODIFIER|MT-TV|ENSG00000210077|transcript|ENST00000387342|Mt_tRNA||n.-1A>G|||||1529|,G|upstream_gene_variant|MODIFIER|MT-RNR2|ENSG00000210082|transcript|ENST00000387347|Mt_rRNA||n.-1A>G|||||1598|,G|upstream_gene_variant|MODIFIER|MT-TL1|ENSG00000209082|transcript|ENST00000386347|Mt_tRNA||n.-1A>G|||||3157|,G|upstream_gene_variant|MODIFIER|MT-ND1|ENSG00000198888|transcript|ENST00000361390|protein_coding||c.-1A>G|||||3234|WARNING_TRANSCRIPT_MULTIPLE_STOP_CODONS,G|upstream_gene_variant|MODIFIER|MT-TI|ENSG00000210100|transcript|ENST00000387365|Mt_tRNA||n.-1A>G|||||4190|,G|upstream_gene_variant|MODIFIER|MT-TM|ENSG00000210112|transcript|ENST00000387377|Mt_tRNA||n.-1A>G|||||4329|,G|upstream_gene_variant|MODIFIER|MT-ND2|ENSG00000198763|transcript|ENST00000361453|protein_coding||c.-1A>G|||||4397|WARNING_TRANSCRIPT_MULTIPLE_STOP_CODONS,G|downstream_gene_variant|MODIFIER|MT-TQ|ENSG00000210107|transcript|ENST00000387372|Mt_tRNA||n.*72T>C|||||4256|,G|intergenic_region|MODIFIER|MT-TF|ENSG00000210049|intergenic_region|ENSG00000210049|||||||||;dbSNP_ASP=true;dbSNP_GNO=true;dbSNP_R5=true;dbSNP_RS=3087742;dbSNP_RSPOS=73;dbSNP_RV=true;dbSNP_SAO=0;dbSNP_SLO=true;dbSNP_SSR=0;dbSNP_VC=SNV;dbSNP_VP=0x050100020005000102000100;dbSNP_WGT=1;dbSNP_dbSNPBuildID=102;IndbSNP;GTStr=WGS4_ZhangHanTao|MutHomozygous	GT:AD:DP:GQ:PL	1/1:1,2920:2923:99:98746,8742,0

	public String chr = "";
	public String pos = "";
	public String rsid = "";
	public String ref = "";
	public String alt = "";
	public String qual = "";
	public String filter = "";
	public String info = "";
	public String format = "";
	public String genetypeIndex = "";

	public VcfRecord(String vcfLine) {
		
		// chr10	93945	.	G	A	3181.78	.	AC=1;AF=0.500;AN=2;BaseQRankSum=-0.091;ClippingRankSum=1.748;DP=339;FS=13.699;MLEAC=1;MLEAF=0.500;MQ=42.01;MQRankSum=-0.667;QD=9.39;ReadPosRankSum=1.567;SOR=0.645	GT:AD:DP:GQ:PL	0/1:213,126:339:99:3210,0,6429
		String[] sLines = vcfLine.split("\t");	
		
		this.chr = sLines[0].replaceAll("chr", "");
		this.pos = sLines[1];
		this.rsid = sLines[2];
		this.ref = sLines[3];
		this.alt = sLines[4];
		this.qual = sLines[5];
		this.filter = sLines[6];
		this.info = sLines[7];
		if (sLines.length > 8) {
			this.format = sLines[8];			
		} else {
			this.format = "";
		}
		this.genetypeIndex = "";

		if (sLines.length > 9) {
			String sGenoTypeIndex = "";
			try {			
				sGenoTypeIndex = sLines[9].split(":")[0];					
				sGenoTypeIndex = sGenoTypeIndex.replaceAll("/", "");
				this.genetypeIndex = sGenoTypeIndex;
			} catch (Exception e) {
				System.out.println(e);
			}		
		} 
		
	}
	
	public SnpObject getSnpObject() {
		SnpObject snp = new SnpObject();
		
		snp.alt = this.alt;
		snp.chr = this.chr;
		snp.genotypeAsIndex = this.genetypeIndex;
		if (this.genetypeIndex.length()>0) {
			snp.genotypeAsString = FuncConvert.convertGenotypeFromIndexToString(snp.ref, snp.alt, snp.genotypeAsIndex);			
		}
		snp.pos = this.pos;
		snp.ref = this.ref;
		snp.rsid = this.rsid;

		return snp;
	}
}
