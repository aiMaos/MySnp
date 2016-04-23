package MySnp.Func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FuncCompare {
//	public static void compareVcf2Table(String sVcfPath, String sTable) {
//		long pos = 0;
//		long filesize = new File(sVcfPath).length();
//		String filename = new File(sVcfPath).getName();
//
//		try {
//			BufferedReader reader = new BufferedReader(
//					new FileReader(sVcfPath));
//	
//			int counter =0;
//			long timer_startpoint = System.currentTimeMillis();
//			
//			
//			// read VCF
//			do {
//				String sLine = reader.readLine();
//				if (sLine == null) {
//					break;
//				} 
//				pos += sLine.length();
//				
//				if (sLine.indexOf(keyword) >= 0) {
//					System.out.println(sLine);
//				}
//				
//				counter ++;
//				if (counter%1000000 == 0) {
//					System.out.println(filename+",line:"+counter+",time:"+(System.currentTimeMillis()-timer_startpoint)/1000+",progress:"+pos*100/filesize+"%");
//				}
//	
//			} while (true);
//		
//			reader.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println("done of compareVcf2Table - "+sVcfPath+","+sTable);
//
//	}
}
