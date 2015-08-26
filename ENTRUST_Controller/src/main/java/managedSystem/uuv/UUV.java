package managedSystem.uuv;

public class UUV {
	static String sensorRates[] = { "5,4,4",	"5,4,4",	"5,4,4", 
									"5,4,3",	"5,4,3",	"5,4,3",
									"5,4,4", 	"5,4,4",	"5,4,4",
									"5,2,4",	"5,2,4",	"5,2,4",  
									"5,4,4", 	"5,4,4",	"5,4,4",
									"5,1,1",	"5,1,1",	"5,1,1",
									"5,4,4", 	"5,4,4",									
								  };	
	
	public static void main(String[] args) {	
		try{
//			Thread.sleep(10000);
			long startTime = System.currentTimeMillis();
			long now;
			long timeStamp;
			int messagesSent = 0;
			
			TCPClient tcpClient = new TCPClient("127.0.0.1", 56567);
			
			for (String rates : sensorRates){
				Thread.sleep(3000);
				now		  =	System.currentTimeMillis(); 
				timeStamp = System.currentTimeMillis() - startTime; 
				System.out.print((timeStamp/1000.0) +"("+messagesSent++ +")\tRates:\t" + rates);	

				String configuration = tcpClient.send(rates);				
				System.out.println("\t\tConfiguration:\t" + configuration);	
			}
			
			String lastRate = sensorRates[sensorRates.length-1];
			
			while (true){
				Thread.sleep(5000);
				tcpClient.send(lastRate);
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
