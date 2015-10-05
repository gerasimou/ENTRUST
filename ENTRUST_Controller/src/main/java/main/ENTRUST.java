package main;

import managedSystem.fx.SBS;
import managingSystem.fx.ManagingSystemFX;
import managingSystem.uuv.ManagingSystemUUV2;

public class ENTRUST {

	public static void main(String[] args) {
		try {
//			ManagingSystemUUV managingSystemUUV = new ManagingSystemUUV();
			ManagingSystemUUV2 managingSystemUUV = new ManagingSystemUUV2();
			managingSystemUUV.startListening();
			
//			SBS 			 sbsFX				= new SBS();
//			ManagingSystemFX managingSystemFX 	= new ManagingSystemFX(sbsFX);
			
//			for (int run=0; run<2; run++){
//				managingSystemFX.runOnce();
//				while(!managingSystemFX.runCarryOn.get());
//				Knowledge.updateService("MW1", 0.05);
//			}
//			System.exit(-1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** jar FX*/
//	public static void main(String[] args) {
//		try {
//			//init systems
//			SBS  			sbsFX				= new SBS();
//			ManagingSystemFX managingSystemFX 	= new ManagingSystemFX(sbsFX);
//			
//			//update knowledge
//			if (args.length > 2){
//				String input[] = Arrays.copyOfRange(args, 1, args.length);
//				System.out.println(Arrays.toString(input));			
//				Knowledge.updateServices(input);
//			}
////			Knowledge.updateServices(new String[]{"MW3", "0.1", "MW1", "0.1"});
//			
//			managingSystemFX.runOnce();
//			while(!managingSystemFX.runCarryOn.get());
//			System.exit(0);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	/** jar UUV*/
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
