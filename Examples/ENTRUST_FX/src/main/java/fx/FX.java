package fx;

public class FX {
	
	private static String[] fxData = {"data1", "data2" };

	
	public static void main(String[] args) {	
		try{
			long startTime = System.currentTimeMillis();
			long now;
			long timeStamp;
			int messagesSent = 0;
			
			TCPClient tcpClient = new TCPClient("127.0.0.1", 56567);

			for (String data : fxData){
				now		  =	System.currentTimeMillis(); 
				timeStamp = System.currentTimeMillis() - startTime; 
				System.out.print((timeStamp/1000.0) +"("+messagesSent++ +")\tData:\t" + data);	

				String configuration = tcpClient.send(data);				
				System.out.println("\t\tConfiguration:\t" + configuration);	
				Thread.sleep(1000);
			}
			
			tcpClient.send("STOP");
							
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
