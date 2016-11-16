package controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import activforms.engine.ActivFORMSEngine;
import auxiliary.Utility;
import fx.AbstractServiceClient;
import fx.SBS;

public class ManagingSystemFX{

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER_RELIABILITY = 1000;
	public static final double MULTIPLIER 			  = 100;
	
	/** Sensor handle*/
	private Sensor sensor;
 
	/** Effector handle*/
	private Effector effector;
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
	
	/** PRISM plugin*/
	private VerificationEngine verificationEngine;
    
    /** configurationd data*/
	int [] newConfiguration = new int[4];

	/** List that keep the services per operation */
	List<List<AbstractServiceClient>> operationsList;
	
	/** Instance of the service-based system */
	private SBS sbs;

	/** time between successive managing system invocations*/
	private final long TIME_WINDOW;

    /** System characteristics*/
    public static int NUM_OF_OPERATIONS;
    public static int NUM_OF_SERVICES;	

    /** flag for run loop to carry on*/
     public AtomicBoolean carryOn = new AtomicBoolean(false);
    
	
	/**
	 * Class constructor
	 */
	public ManagingSystemFX(){
	    //initialise time window
		this.TIME_WINDOW		= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
		
		//init system characteristics
	    //TODO initialised programmatically
	    this.NUM_OF_OPERATIONS		= 6;
	    this.NUM_OF_SERVICES		= 2;
	    
	    int portNumber 				= 56567;

		try {
			//initialise the SBS system
			this.sbs 			= new SBS();
			this.operationsList = this.sbs.getOperationsList();

			//initialise ActiveFORMS engine
		    this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9999);
		    this.engine.setRealTimeUnit(1000);

		    //init probe
		    this.sensor = new Sensor(engine, portNumber);
		    
		    //init effector
		    this.effector = new Effector(engine, sensor.getOutputStream());
		    
		    //init PRISM plugin
		    this.verificationEngine = new VerificationEngine(engine);
		    
		    //start the engine
		    engine.start();
		    
		    //and start listening
		    Knowledge.initKnowledge(operationsList);

		} catch (Exception e) {
		    e.printStackTrace();
		    System.exit(-1);
		}
	}
    
    
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
					Object[][] servicesReliability = getServicesReliability();
					carryOn.set(false);
					sensor.sendDataToMonitor(servicesReliability);
					
					 //wait until the first configuration is established
					 while (!carryOn.get());
					 
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
	
	
	
	public void runOnce() {
		carryOn.set(false);
//		System.err.println("{t:0 \tManagingSystem.run()");
		Object[][] servicesReliability = getServicesReliability();
		sensor.sendDataToMonitor(servicesReliability);			
	}
	
	
	/** Gets service reliability before sending this information to the Analyser*/
	private Integer[][] getServicesReliability(){
		Integer[][] servicesReliability = new Integer [NUM_OF_OPERATIONS][NUM_OF_SERVICES];
		
    	//calculate services reliability
    	for (int operation=0; operation<operationsList.size(); operation++){
    		for (int service=0; service<operationsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
//    			serviceClient.calculateReliability();
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
    	carryOn.set(true);
    }    
    
    
    
    
}

