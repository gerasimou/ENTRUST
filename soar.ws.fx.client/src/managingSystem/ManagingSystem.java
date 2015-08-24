package managingSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import activforms.engine.ActivFORMSEngine;
import auxiliary.Utility;
import fx.services.AbstractServiceClient;
import prism.PrismPlugin;
import sbs.SBS;

public class ManagingSystem implements Runnable{

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER_RELIABILITY = 1000;
	public static final double MULTIPLIER 			  = 10;
	
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
    public final int NUM_OF_OPERATIONS;
    public final int NUM_OF_SERVICES;	

    /** flag for run loop to carry on*/
     AtomicBoolean runCarryOn = new AtomicBoolean(false);
    
	
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
//		    engine.addGoal(new Goal("Requirement1", "sConfig.sConfig > 20", new GoalChecker(), ""));
//		    engine.addGoal(new Goal("Requirement2", "sConfig.req2Result < 120", new GoalChecker(), ""));
		    
		    //start the engine
		    engine.start();
		    
		    //and start listening
//		    startListening();
//		    generateServiceCharacteristics();
		    Knowledge.initKnowledge(operationsList);

		} catch (Exception e) {
		    e.printStackTrace();
		    System.exit(-1);
		}
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
					runCarryOn.set(false);
					probe.sendAverageRates(servicesReliability, NUM_OF_OPERATIONS, NUM_OF_SERVICES);
					
					 //wait until the first configuration is established
					 while (!runCarryOn.get());
					 
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
					engine.stop();
					return;
				}
				
				timeNow = System.currentTimeMillis();
			
			}//while
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** Gets service reliability before sending this information to the Analyser*/
	private int[][] getServicesReliability(){
		int[][] servicesReliability = new int [NUM_OF_OPERATIONS][NUM_OF_SERVICES];
		
    	//calculate services reliability
    	for (int operation=0; operation<operationsList.size(); operation++){
    		for (int service=0; service<operationsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
    			serviceClient.calculateReliability();
    			servicesReliability[operation][service] = (int)(serviceClient.getReliability() * MULTIPLIER_RELIABILITY);
    		}
    	}		
    	return servicesReliability;
	}
    
    
    public void returnResult(int [] newConfiguration){
    	if (newConfiguration != null){
    		sbs.setActiveServicesList(newConfiguration);
    	}
    	sbs.printActiveServices();
    	runCarryOn.set(true);    	
    }    
    
    
    //private class
    class ServiceCharacteristics{
    	/** service actual reliability*/
    	private double reliability;
    	
    	/** cost per invocation*/
    	private double costPerInvocation;
    	
    	/** time required to carry out its functionality per invocation*/
    	private double timePerInvocation;
    	
    	/** Service ID*/
    	private String id;
    	    	
    	public ServiceCharacteristics(String id, double costPerInvocation, double timePerInvocation, double reliability) {
    		this.id 				= id;
    		this.costPerInvocation 	= costPerInvocation;
    		this.timePerInvocation	= timePerInvocation;
    		this.reliability		= reliability;
		}

    	
    	public double getCostPerInvocation(){
    		return this.costPerInvocation;
    	}
    	
    	public double getTimePerInvocation(){
    		return this.timePerInvocation;
    	}
    	
    	public String getID(){
    		return this.id;
    	}
    	
    	public double getReliability(){
    		return this.reliability;
    	}
    	
    	public void setReliability(double reliability){
    		this.reliability = reliability;
    	}
    }
    
    
    
    ///////////////////////////////////////////////
    //DEPRECATED FUNCTIONS
    ///////////////////////////////////////////////
    /**
     * Start listening for incoming messages
     * @throws IOException
     */
    @Deprecated
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

    
    /** Create a list for storing services characteristics that will be used when carrying out RQV*/
  @Deprecated
  	private void generateServiceCharacteristics(){
  	List<List<ServiceCharacteristics>> operationCharacteristicsList = new ArrayList<List<ServiceCharacteristics>>();
  	for (int operation=0; operation<operationsList.size(); operation++){
  		List<ServiceCharacteristics> servicesCharacteristicsList = new ArrayList<ServiceCharacteristics>();

  		for (int service=0; service<operationsList.get(operation).size(); service++){
  			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
  			Object[] serviceCharacteristics = serviceClient. getServiceCharacteristics();
  			servicesCharacteristicsList.add(new ServiceCharacteristics((String)serviceCharacteristics[0], (double)serviceCharacteristics[1], 
  																	   (double)serviceCharacteristics[2], (double)serviceCharacteristics[3]));
  		}//for
  		operationCharacteristicsList.add(servicesCharacteristicsList);

  	}//for
  }//
}

