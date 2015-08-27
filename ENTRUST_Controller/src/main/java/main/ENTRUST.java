package main;

import java.io.IOException;

import managingSystem.uuv.ManagingSystemUUV;
import managingSystem.uuv.ManagingSystemUUV2;

public class ENTRUST {

	public static void main(String[] args) {
		try {
			ManagingSystemUUV managingSystemUUV = new ManagingSystemUUV();
//			ManagingSystemUUV2 managingSystemUUV = new ManagingSystemUUV2();
			managingSystemUUV.startListening();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/** jar*/
//	public static void main(String[] args) {
//		try {
//			String input = "";
//			for (int i=1; i<args.length-1; i++ ){
//				input += args[i] + "," ;
//			}
//			input += args[args.length-1];
//			
//			System.out.println(input);
//			ManagingSystemUUV managingSystemUUV = new ManagingSystemUUV();
//			managingSystemUUV.run(input);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


}
