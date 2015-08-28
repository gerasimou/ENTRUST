package managedSystem.uuv;

public class UUV {
	
	/** 3 Sensors */
//	static String sensorRates[] = { "5,4,4",	"5,4,4",	"5,4,4",
//									"5,4,2",	"5,4,2",	"5,4,2",
//									"5,4,4", 	"5,4,4",	"5,4,4",
//									"5,2,4",	"5,2,4",	"5,2,4",  
//									"5,4,4", 	"5,4,4",	"5,4,4",
//									"5,4,3",	"5,4,3",	"5,4,3",									
//									"5,4,4", 	"5,4,4",	"5,4,4",
//									"5,1,1",	"5,1,1",	"5,1,1",
//									"5,4,4", 	"5,4,4",	"5,4,4",
//								  };	
	
	/** 4 Sensors */
//	static String sensorRates[] = { "5,4,4,4",	"5,4,4,4",	"5,4,4,4", 
//									"5,4,2,4",	"5,4,2,4",	"5,4,2,4",
//									"5,4,4,4",	"5,4,4,4",	"5,4,4,4", 
//									"5,2,4,4",	"5,2,4,4",	"5,2,4,4",	
//									"5,4,4,4",	"5,4,4,4",	"5,4,4,4", 
//									"5,4,3,4",	"5,4,3,4",	"5,4,3,4",								
//									"5,4,4,4",	"5,4,4,4",	"5,4,4,4", 
//									"5,1,1,4",	"5,1,1,4",	"5,1,1,4",	
//									"5,4,4,4",	"5,4,4,4",	"5,4,4,4", 
//								  };

	/** 5 Sensors */
//	static String sensorRates[] = { "5,4,4,4,4",	"5,4,4,4,4",	"5,4,4,4,4",
//									"5,4,2,4,4,4",	"5,4,2,4,4,4",	"5,4,2,4,4,4",
//									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
//									"5,2,4,4,4,4",	"5,2,4,4,4,4",	"5,2,4,4,4,4",		
//									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
//									"5,4,3,4,4,4",	"5,4,3,4,4,4",	"5,4,3,4,4,4",									
//									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
//									"5,1,1,4,4,4",	"5,1,1,4,4,4",	"5,1,1,4,4,4",	
//									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
//								  };
	
	
	/** 6 Sensors */
	static String sensorRates[] = { "5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4",
									"5,4,2,4,4,4",	"5,4,2,4,4,4",	"5,4,2,4,4,4",
									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
									"5,2,4,4,4,4",	"5,2,4,4,4,4",	"5,2,4,4,4,4",		
									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
									"5,4,3,4,4,4",	"5,4,3,4,4,4",	"5,4,3,4,4,4",									
									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
									"5,1,1,4,4,4",	"5,1,1,4,4,4",	"5,1,1,4,4,4",	
									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
								  };

	/** 8 Sensors */
//	static String sensorRates[] = { "5,4,4,4,4,4,3,3",	"5,4,4,4,4,4,3,3",	"5,4,4,4,4,4,3,3",
////									"5,4,2,4,4,4",	"5,4,2,4,4,4",	"5,4,2,4,4,4",
////									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
////									"5,2,4,4,4,4",	"5,2,4,4,4,4",	"5,2,4,4,4,4",		
////									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
////									"5,4,3,4,4,4",	"5,4,3,4,4,4",	"5,4,3,4,4,4",									
////									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
////									"5,1,1,4,4,4",	"5,1,1,4,4,4",	"5,1,1,4,4,4",	
////									"5,4,4,4,4,4",	"5,4,4,4,4,4",	"5,4,4,4,4,4", 
//								  };

	
	public static void main(String[] args) {	
		try{
			long startTime = System.currentTimeMillis();
			long now;
			long timeStamp;
			int messagesSent = 0;
			
			TCPClient tcpClient = new TCPClient("127.0.0.1", 56567);
			
			for (int run=0; run<10; run++){
				for (String rates : sensorRates){
					now		  =	System.currentTimeMillis(); 
					timeStamp = System.currentTimeMillis() - startTime; 
					System.out.print((timeStamp/1000.0) +"("+messagesSent++ +")\tRates:\t" + rates);	
	
					String configuration = tcpClient.send(rates);				
					System.out.println("\t\tConfiguration:\t" + configuration);	
					Thread.sleep(5000);
				}
				
				Thread.sleep(20000);
			}			
//			String lastRate = sensorRates[sensorRates.length-1];
//			
//			while (true){
//				Thread.sleep(5000);
//				tcpClient.send(lastRate);
//			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
