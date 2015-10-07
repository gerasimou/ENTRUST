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
import java.util.List;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import fx.AbstractServiceClient;

public class Sensor extends Synchronizer {
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Managing system handler*/
    private ENTRUST entrust;

    /** Signal(s)*/
	private int setAverageRates;
	
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
	public Sensor(ActivFORMSEngine engine, ENTRUST entrust){
    	//assign handles
		this.engine 	= engine;
		this.entrust	= entrust;

		//get signal(s) ID
		setAverageRates = engine.getChannel("setAverageRates");
    }
    
	
	
    /**
    * Function that assembles data in a format expected by the ENTRUST controller 
    * (as defined in the verifiable ENTRUST controller models) and sends this data 
    * to the monitor
    * @param arguments: variables affecting managed system behaviour
    */
    public void sendDataToMonitor(Object[][] arguments) {
    	//create a String array to store the data expected by monitor
		String [] dataExpectedByMonitor = new String[ENTRUST.NUM_OF_OPERATIONS * ENTRUST.NUM_OF_SERVICES];// 6x3
		
		//fill in the String array
		int index = 0;
		for(int operation = 0; operation < ENTRUST.NUM_OF_OPERATIONS ; operation++){
		    for (int service = 0; service < ENTRUST.NUM_OF_SERVICES; service++){
		    	dataExpectedByMonitor[index++] = "currentAvgFRates[" + (operation+1) + "][" + (service+1) + "]=" + (int)arguments[operation][service];
		    }	
		}

		//send the data to the monitor
		engine.send(setAverageRates,this, dataExpectedByMonitor);
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
    public void startListening(int portNumber){
		 try{
			 serverSocket 	= new ServerSocket(portNumber);
			 System.out.println("ENTRUST controller ready - awaiting requests\n");
	
			 clientSocket	= serverSocket.accept();
			 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
			 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 
			 //assign output stream
			 entrust.assignOutputStream(out);
			 
			 System.out.println("Connection established");
	
			 while (true){
				 String input = in.readLine();
				 if (input.toUpperCase().equals("STOP"))
					break;
			 
				 //process the input
				 Object inputs[] = input.split(",");
				
				 //send data to monitor
				 sendDataToMonitor(getServicesReliability());
			 }
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 finally{
			 out.println("Done");
			 System.out.println("\n=============\nDone\n=============");
			 System.exit(0);
		 }
    }
    
    
    /** Gets service reliability before sending this information to  Analyser*/
	private Object[][] getServicesReliability(){
		Object[][] servicesReliability = new Object [ENTRUST.NUM_OF_OPERATIONS][ENTRUST.NUM_OF_SERVICES];
		
    	//get the characteristics of services from the knowledge
    	List<List<AbstractServiceClient>> operationsList = Knowledge.getOperationsList();

    	//calculate services reliability
    	for (int operation=0; operation<operationsList.size(); operation++){
    		for (int service=0; service<operationsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
//    			serviceClient.calculateReliability();
    			servicesReliability[operation][service] = (int)(serviceClient.getReliability() * ENTRUST.MULTIPLIER_RELIABILITY);
    		}
    	}		
    	return servicesReliability;
	}
    
    
    /**
     * Return output stream for use by the effector
     * @return
     */
    protected PrintWriter getOutputStream(){
    	return this.out;
    }
}
