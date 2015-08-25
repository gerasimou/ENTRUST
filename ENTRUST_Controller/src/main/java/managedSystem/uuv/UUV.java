package managedSystem.uuv;

public class UUV {
	static String sensorRates[] = { "5,4,4", "5,4,1" 	
			 			   };	
	
	public static void main(String[] args) {	
		try{
			TCPClient tcpClient = new TCPClient("127.0.0.1", 56567);
			
			for (String rates : sensorRates){
				Thread.sleep(5000);
				tcpClient.send(rates);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
