package fx;

public class FX implements Runnable{
	int portNumber = -1;
	
	public FX(int portNumber){
		this.portNumber = portNumber;
	}
	
	private static String[] fxData = {"data1", "data2" };

	
	public static void main(String[] args) {	
		new FX(-1).run();
	}
	
	
	public void run(){
		try{
			long startTime = System.currentTimeMillis();
			long timeStamp;
			int messagesSent = 0;
			
			TCPClient tcpClient = new TCPClient("127.0.0.1", portNumber);

			for (String data : fxData){
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
