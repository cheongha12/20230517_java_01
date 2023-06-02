package kh.lclass.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestFileIO {

	public void testFile() /*throws Exception*/{
//		File f1= new File("D:/data1/aaa.txt");
//		f1.mkdir();
		String path = "D:/data2/test";
		String filename = "aaa.txt";
		
		new File(path).mkdirs();	//디렉토리 생성
		File f2 = new File(path,filename);
//		f1.mkdir();	//디렉토리 생성
//		File f2 = new File("D:/data2/test/aaa.txt");
		FileWriter fw = null;
		try {
			f2.createNewFile();	//파일생성
			
			fw =new FileWriter(f2);
			fw.write("abcdefg");
			fw.flush();
			
			
			
			
		}catch(IOException e) {
			System.out.println("file 생성 못함");
			e.printStackTrace();
		}finally {
		try {
			if(fw !=null) fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
}