//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- Sensor class										                                    //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//

package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Sensor extends Synchronizer {
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Signal(s)*/
	private int sensorSignal1;
	
	/** Communication handles*/
    private ServerSocket serverSocket;
    private Socket clientSocket;		
    private PrintWriter out;			
    private BufferedReader in;


    
    /**
     * Constructor: create a new sensor
     * @param engine
     * @param mainX
     */
	public Sensor(ActivFORMSEngine engine){
    	//assign handles
		this.engine = engine;

		//get signal(s) ID
		sensorSignal1 = engine.getChannel("sensorSignal1");
    }
    
	
	
    /**
    * Function that assembles data in a format expected by the ENTRUST controller 
    * (as defined in the verifiable ENTRUST controller models) and sends this data 
    * to the monitor
    * @param arguments: variables affecting managed system behaviour
    */
    public void sendDataToMonitor(Object ... arguments) {
    	//create a String array to store the data expected by monitor
		String [] dataExpectedByMonitor = null;
		
		//fill in the String array
		//TODO...

		//send the data to the monitor
		engine.send(sensorSignal1,this, dataExpectedByMonitor);
    }



    /**
     * Function run when Sensor receives a signal; not used in ENTRUST
     */
	@Override
	@SuppressWarnings("unused")
	public void receive(int arg0, HashMap<String, Object> arg1) {
	}
	
	
	
	/**
	 * Listen for a message from the managed system
	 * @throws IOException
	 */
    public void startListening(int port){
    	int portNumber = port;
		 try{
			 serverSocket 	= new ServerSocket(portNumber);
			 System.out.println("Managing system ready - awaiting requests\n");
	
			 clientSocket	= serverSocket.accept();
			 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
			 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 System.out.println("Connection established");
	
			 while (true){
				 String input = in.readLine();
					if (input.toLowerCase().equals("STOP"))
						break;
				 
					 //process the input
					 //TODO...
					
					 //send data to monitor
					 sendDataToMonitor();
				 }
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 finally{
			 out.println("Done");
			 System.exit(0);
		 }
    }
}
