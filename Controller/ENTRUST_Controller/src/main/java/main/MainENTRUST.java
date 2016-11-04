package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import managedSystem.fx.SBS;
import managingSystem.fx.Knowledge;
import managingSystem.fx.ManagingSystemFX;

public class MainENTRUST {
	
	private static String configFile = "resources/configFX2.properties";
	public static  String event		 = "E-1";

	public static void main(String[] args) {
		try {
//			ManagingSystemUUV managingSystemUUV = new ManagingSystemUUV();
//			ManagingSystemUUV2 managingSystemUUV = new ManagingSystemUUV2();
//			managingSystemUUV.startListening();
			
			runFXscenario();
			
			Thread.sleep(3000);
			killAllThreads();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.exit(-1);
		}
	}
	
	
	private static void runFXscenario() throws FileNotFoundException, IOException{
		SBS sbsFX							= new SBS();
		ManagingSystemFX managingSystemFX 	= new ManagingSystemFX(sbsFX);
		
		//Scenario
		//E1:Normal
		event = "E1";
		System.out.println("E1:Normal");
		managingSystemFX.runOnce();
		while(!managingSystemFX.carryOn.get());	

		//E2:MW1.reliability(0.05);
		event = "E2";
		System.out.println("E2:MW1.reliability(0.9)");
		Knowledge.updateServiceReliability("MW1", 0.9);
		managingSystemFX.runOnce();
		while(!managingSystemFX.carryOn.get());	
		
		//E3:FA2.responseTime(1.2);
		event = "E3";
		System.out.println("E3:FA2.responseTime(1.2)");
		Knowledge.updateServiceReliability("MW1", 0.901);
		Knowledge.updateServiceResponseTime("FA2", 1.2);
		managingSystemFX.runOnce();	
		while(!managingSystemFX.carryOn.get());	
		
		//E4:MW1.reliability(0.976);
		event = "E4";
		System.out.println("E4:MW1.reliability(0.976)");
		Knowledge.updateServiceReliability("MW1", 0.976);
		managingSystemFX.runOnce();	
		while(!managingSystemFX.carryOn.get());	

		//E5:TA1.reliability(0.985) & NOT1.time(1.0");
		event = "E5";
		System.out.println("E5:TA1.reliability(0.985) & NOT1.time(1.0");
		Knowledge.updateServiceReliability("TA1", 0.98);
		Knowledge.updateServiceResponseTime("NOT1", 1);
		managingSystemFX.runOnce();	
		while(!managingSystemFX.carryOn.get());	

		//E6:OR1.reliability(0.95) & TA1.reliability(0.998)
		event = "E6";
		System.out.println("E6:OR1.reliability(0.95) & TA1.reliability(0.998)") ;
		Knowledge.updateServiceReliability("OR1", 0.9);
		Knowledge.updateServiceReliability("TA1",0.998);
		managingSystemFX.runOnce();	
		while(!managingSystemFX.carryOn.get());	
	
		//E7:Normal
		event = "E7";
		System.out.println("E7:Normal") ;
		Knowledge.updateServiceResponseTime("FA2", 0.7);
		Knowledge.updateServiceResponseTime("NOT1", 1.8);
		Knowledge.updateServiceReliability("OR1", 0.995);
		managingSystemFX.runOnce();	
		while(!managingSystemFX.carryOn.get());	
}
	
	
	private static void killAllThreads(){
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for (Thread thread : threadSet){
			thread.interrupt();
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

	public static String getConfigFile(){
		return configFile;
	}

}
