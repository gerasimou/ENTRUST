package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import activforms.engine.ActivFORMSEngine;
import activforms.goalmanagement.Goal;
import auxiliary.Utility;
import fx.services.AbstractServiceClient;
import managingSystem.Effector;
import managingSystem.Probe;
import prism.PrismPlugin;
import sbs.GoalChecker;
import sbs.SBS;

public class ManagingSystem implements Runnable{

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER = 1000;
	
	/** Probe handle*/
	private Probe probe;
 
	/** Effector handle*/
	private Effector effector;

	/** Communication handles*/
    private ServerSocket serverSocket;		// 	= new ServerSocket(portNumber);
    private Socket clientSocket;			//	= serverSocket.accept();
    private PrintWriter out;				// 	= new PrintWriter(clientSocket.getOutputStream(), true);
    private BufferedReader in;				//	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
	
	/** PRISM plugin*/
	private PrismPlugin prismPlugin;
    
    /** configurationd data*/
	int [] newConfiguration = new int[4];

	/** List that keep the services per operation */
	List<List<AbstractServiceClient>> operationsList;
	
	/** Instance of the service-based system */
	private SBS sbs;

	/** time between successive managing system invocations*/
	private final long TIME_WINDOW;

    /** System characteristics*/
    private final int NUM_OF_OPERATIONS;
    private final int NUM_OF_SERVICES;	

	
	/**
	 * Class constructor
	 */
	public ManagingSystem(SBS theSystem){
	    //initialise time window
		this.TIME_WINDOW		= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
		
		//init system characteristics
	    //TODO initialised programmatically
	    this.NUM_OF_OPERATIONS		= 6;
	    this.NUM_OF_SERVICES		= 2;

		try {
			//initialise the SBS system
			this.sbs 			= theSystem;
			this.operationsList = this.sbs.getOperationsList();

			//initialise ActiveFORMS engine
		    this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9999);
		    this.engine.setRealTimeUnit(1000);

		    //init probe
		    this.probe = new Probe(engine, this);
		    
		    //init effector
		    this.effector = new Effector(engine, this);
		    
		    //init PRISM plugin
		    this.prismPlugin = new PrismPlugin(engine);
		    
		    //setup system QoS requirements
		    //TODO: application specific --> need to be FX compatible
		    engine.addGoal(new Goal("Requirement1", "currentConfiguration.req1Result > 20", new GoalChecker(), ""));
		    engine.addGoal(new Goal("Requirement2", "currentConfiguration.req2Result < 120", new GoalChecker(), ""));
		    
		    //start the engine
		    engine.start();
		    
		    //and start listening
//		    startListening();

		} catch (Exception e) {
		    e.printStackTrace();
		    System.exit(-1);
		}
	}
	
    
    
    /**
     * Start listening for incoming messages
     * @throws IOException
     */
    public void startListening() throws IOException{
		 int portNumber = 56567;
		 serverSocket 	= new ServerSocket(portNumber);
		 System.out.println("Prism(server) ready - awaiting requests\n");

		 clientSocket	= serverSocket.accept();
		 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
		 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 System.out.println("Connection established");
		 String input;
		 
		 while (true){
			 input = in.readLine();
			 try{
				if (input.toLowerCase().equals("done"))
					break;
			 
				 String inputs[] = input.split(",");

				 //TODO: FX
				 probe.sendAverageRates(new int[][]{}, -1 , -1);
//				 returnResult(newConfiguration);
			 }
			 catch (Exception e){
				 e.printStackTrace();
				 System.exit(-1);
			 }
		 }
		 out.println("Done");
		 System.exit(0);
    }
    
    
    
	@Override
	public void run() {
       	long startTime  = System.currentTimeMillis(); 	//time that the simulation started. i.e., now
    	long managingSystemCallTime	= 0;				//time for the latest managing system invocation

		long timeNow 	= startTime;
		
		boolean firstTime = true;
		
		try{
			while (true){
				
				if (timeNow >=  managingSystemCallTime){
					System.err.println("{t: "+ ((timeNow-startTime)/1000.0) +"} ManagingSystem.run()");
					managingSystemCallTime = timeNow + TIME_WINDOW; //update the time, i.e., to invoke the managing system again +TIME_WINDOW from now

					//TODO: FX
					int[][] servicesReliability = getServicesReliability();
					 probe.sendAverageRates(servicesReliability, NUM_OF_OPERATIONS, NUM_OF_SERVICES);
					
					 //wait until the first configuration is established
					 //....
					 
					//wake up (start) SBS
					 if (firstTime){
						 synchronized(sbs){
							 sbs.notify();
						 }
						 firstTime = false;
					 }
					
				}//if

				//when I am interrupted, I should stop
				if (Thread.currentThread().isInterrupted()){
					System.err.println("ManagingSystem exiting");
					return;
				}
				
				timeNow = System.currentTimeMillis();
			
			}//while
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private int[][] getServicesReliability(){
		int[][] servicesReliability = new int [NUM_OF_OPERATIONS][NUM_OF_SERVICES];
		
    	//calculate services reliability
    	for (int operation=0; operation<operationsList.size(); operation++){
    		for (int service=0; service<operationsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
    			serviceClient.calculateReliability();
    			servicesReliability[operation][service] = (int)(serviceClient.getReliability() * MULTIPLIER);
    		}
    	}		
    	return servicesReliability;
	}
	
	
	
	
	
	
	
    public void returnResult(int [] newConfiguration){
    	String output = "";
    	for (int index=0; index<newConfiguration.length; index++){
    		int tempResult = newConfiguration[index];
    		if (index==3 && tempResult!=-1)
    			output += tempResult/100.0;
    		else
    			output += tempResult +",";
    	}
//    	output = "1,0,1,3.0";
    	System.out.println(output);
    	
    	out.println(output);
    	out.flush();
//    	resetNewConfiguration();
    }
    
    
    public void resetNewConfiguration(){
    	Arrays.fill(newConfiguration, -1);
    }

    
}

