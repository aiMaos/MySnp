package MySnp.Data;

public class SnpObject {
	public String rsid = "";
	public String chr = "";
	public String pos = "";
	public String ref = "";
	public String alt = "";
	public String genotypeAsIndex = "";
	public String genotypeAsString = "";

	public SnpObject(SnpObject src) {
		this.rsid = src.rsid;
		this.chr = src.chr;
		this.pos = src.pos;
		this.ref = src.ref;
		this.alt = src.alt;
		this.genotypeAsIndex = src.genotypeAsIndex;
		this.genotypeAsString = src.genotypeAsString;
	}
	
	public SnpObject() {
		this.rsid = "";
		this.chr = "";
		this.pos = "";
		this.ref = "";
		this.alt = "";
		this.genotypeAsIndex = "";
		this.genotypeAsString = "";
	}
	
	public String getString() {
		return (rsid+","+chr+","+pos+","+genotypeAsString);
	}
	
	public String getStringAsVcf() {
		String sChrFormat = chr;
		if (sChrFormat.startsWith("chr")) {
			sChrFormat = sChrFormat.replaceAll("chr", "");
		}
		if (sChrFormat.equalsIgnoreCase("M")) {
			sChrFormat = "MT";
		}

		return rsid + "\t" + sChrFormat + "\t" + pos + "\t" + ref + ">" + alt + "\t" + genotypeAsIndex;
	}
	
	public String getStringAs23andme() {
		String sChrFormat = chr;
		if (sChrFormat.startsWith("chr")) {
			sChrFormat = sChrFormat.replaceAll("chr", "");
		}
		if (sChrFormat.equalsIgnoreCase("M")) {
			sChrFormat = "MT";
		}

		return rsid + "\t" + sChrFormat + "\t" + pos + "\t" + genotypeAsString;
	}
	
}
