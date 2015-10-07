package fx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient{
	private String 	ipAddress = "127.0.0.1";
	private int		port	  = 56567;
	
	private Socket 		socket;
	private PrintWriter 	out ;
	private BufferedReader reader;	
	
//	private int messagesSent = 0;
	
	
	public TCPClient(String ipAddress, int port) throws UnknownHostException, IOException, InterruptedException{
		this.ipAddress 	= ipAddress;
		this.port		= port;
		
	     socket = new Socket (this.ipAddress, this.port);
		 out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true); 					
		 
		 reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		 
	}
	
	
	public String send(String msg) {
		try{
//			System.out.println(messagesSent++ +") Sent:\t" + msg);	
			   	
			 out.println (msg);
			 out.flush();
		    	 	    	 
	    	 String response = "";
		    	 
	    	 if ( (response = reader.readLine()) != null){
	    		 return response;
//	    		 System.out.println("\tResponse: " + response);        
	    	 }
		}
		catch (IOException e){
			e.printStackTrace();
		}
//		finally{
//			return null;
//		}
		return null;
	}
}
