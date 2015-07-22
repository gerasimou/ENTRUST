package managingSystem;

import java.util.ArrayList;
import java.util.List;

import auxiliary.Utility;
import fx.services.AbstractServiceClient;
import prism.PrismAPI;
import sbs.SBS;

public class ManagingSystem implements Runnable{
	
	/** PRISM instance */
	PrismAPI prism;
	
	/** Name of model file */
	String modelFileName;
	
	/** Name of properties file */
	String propertiesFileName;
	
	/** List that keep the services per operation */
	List<List<AbstractServiceClient>> operationsList;
		
	/** Model string*/
	String modelAsString;
	
	/** Instance of the service-based system */
	SBS sbs;
	
	/** time for running a scenario*/
	private final long SIMULATION_TIME;
	
	/** time between successive managing system invocations*/
	private final long TIME_WINDOW;
	
	/** keeps the results of QoS properties for all possible system configurations */
	private List<List<Double>> configurationResults;
	
	/** System reliability constraint*/
	private final double RELIABILITY_THRESHOLD = 0.9;
	
	public ManagingSystem(SBS theSystem){
		this.sbs 			= theSystem;
		this.operationsList = this.sbs.getOperationsList();
		
		//Read  model and properties parameters
		this.modelFileName 		= Utility.getProperty("MODEL_FILE");
		this.propertiesFileName	= Utility.getProperty("PROPERTIES_FILE");
		
		//Read the model
		this.modelAsString = Utility.readFile(modelFileName);		
		
		//initialise simulation time
    	this.SIMULATION_TIME = Long.parseLong(Utility.getProperty("SIMULATION_TIME"));
		
		//initialise time window
		this.TIME_WINDOW		= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
		
		//initialise configuration results list
		this.configurationResults = new ArrayList<List<Double>>();
		
		//initialise PRISM instance
		this.prism = new PrismAPI();
		prism.setPropertiesFile(propertiesFileName);
	}

	
	/** Runs the managing system */
	@Override
	public void run(){
       	long startTime 				= System.currentTimeMillis(); 	//time that the simulation started. i.e., now
    	long stopTime				= startTime + SIMULATION_TIME; 	//time for ending the simulation, i.e., now + SIMULATION_TIME 
    	long managingSystemCallTime	= 0;							//time for the latest managing system invocation
		long timeNow 	= startTime;
		
		try {
			//run initial configuration (managed system setup)
			System.err.println("{t: "+ ((timeNow-startTime)/1000.0) +"} ManagingSystem.run()");
			managingSystemCallTime = timeNow + TIME_WINDOW; //update the time, i.e., to invoke the managing system again +TIME_WINDOW from now
			sbs.setActiveServicesList(new int[]{0,1,0,0,0,0});
			runQV();
			sbs.printActiveServices();
			

			//wake up (start) SBS
			synchronized(sbs){
	    		sbs.notify();
	    	}
			
			while (true){
			
				if (timeNow >=  managingSystemCallTime){
					System.err.println("{t: "+ ((timeNow-startTime)/1000.0) +"} ManagingSystem.run()");
					managingSystemCallTime = timeNow + TIME_WINDOW; //update the time, i.e., to invoke the managing system again +TIME_WINDOW from now
					
					//carry out quantitative verification
					runQV();
    				
					//select the best configuration considering system QoS requirements
					int bestConfiguration = analyse();
										
					//find which services comprise this configuration
					int[] servicesConfiguration = configurationToServices(bestConfiguration);
					
					//active the new set of services
					sbs.setActiveServicesList(servicesConfiguration);

					sbs.printActiveServices();
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
	
	
    /** Set up the  system configuration considering nominal values for the services*/
    private void runQV(){
    	//get the cartesian product
    	int counter = 0;
    	
    	//clear up previous configuration results
    	configurationResults.clear();
    	
    	//calculate services reliability
    	for (int operation=0; operation<operationsList.size(); operation++){
    		for (int service=0; service<operationsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = operationsList.get(operation).get(service);
    			serviceClient.calculateReliability();
    		}
    	}
    	
    	for (int indexList0=0; indexList0<operationsList.get(0).size(); indexList0++){
    		AbstractServiceClient srv0 = operationsList.get(0).get(indexList0);

    		for (int indexList1=0; indexList1<operationsList.get(1).size(); indexList1++){
    			AbstractServiceClient srv1  = operationsList.get(1).get(indexList1);
    			
        		for (int indexList2=0; indexList2<operationsList.get(2).size(); indexList2++){
        			AbstractServiceClient srv2 = operationsList.get(2).get(indexList2); 
        			
            		for (int indexList3=0; indexList3 < operationsList.get(3).size(); indexList3++){
            			AbstractServiceClient srv3 = operationsList.get(3).get(indexList3);
            					
                		for (int indexList4=0; indexList4<operationsList.get(4).size(); indexList4++){
                			AbstractServiceClient srv4 = operationsList.get(4).get(indexList4);

                			for (int indexList5=0; indexList5 < operationsList.get(5).size(); indexList5++){
                				AbstractServiceClient srv5  = operationsList.get(5).get(indexList5);
                			        
                				double[][] srvFeatures = new double[][]{srv0.getFeatures(), srv1.getFeatures(), srv2.getFeatures(),
                														srv3.getFeatures(), srv4.getFeatures(), srv5.getFeatures()};
                				//Generate a correct PRISM model									
                				String modelString = realiseProbabilisticModel(srvFeatures);
                   				                		
                				//load the PRISM model
                				prism.loadModel(modelString);

                				//run PRISM
                				List<Double> prismResult = prism.runPrism();
                				configurationResults.add(prismResult);
                				
                				String srvsID = "[" + srv0.getID() +","+ srv1.getID() +","+ srv2.getID() +","+
                									  srv3.getID() +","+ srv4.getID() +","+ srv5.getID() +"]\t";
                				
//                				System.out.println(counter++ +"), "+ Arrays.toString(prismResult.toArray()));                				                				
                			}                			
                		}    			            			
            		}    			
        		}    			
    		}
    	}
    }
    
    
    /**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double[][] srvFeatures){
    	StringBuilder model = new StringBuilder(modelAsString);
    	for (int index=0; index<srvFeatures.length; index++){
    		int serviceindex = index+1;
    		model.append("const double op"+ serviceindex + "Fail = " + (1-srvFeatures[index][0]) +";\n");
    		model.append("const double op"+ serviceindex + "Cost = " + srvFeatures[index][1] +";\n");
    		model.append("const double op"+ serviceindex + "Time = " + srvFeatures[index][2] +";\n\n");;
    	}    	
    	return model.toString();
    }
	

    /**
     * Checks the possible system configurations and determines which configuration 
     * satisfies system QoS requirements and optimises a fitness function
     * 	min(w_1*cost + w_2	*responseTime)
     * @return best configuration
     */
    private int analyse(){
    	int bestConfiguration	= -1;
    	double bestResult		= Double.MAX_VALUE;
    	double w1 				= 2;
    	double w2 				= 8;
    	
    	double[] configResultsArray = new double[configurationResults.size()];
    	
    	double result;
    	for (int configuration=0; configuration<configurationResults.size(); configuration++){
    		List<Double> resultList = configurationResults.get(configuration); 
    		double reliability 	= resultList.get(0);
    		double cost			= resultList.get(1);
    		double time			= resultList.get(2);			
    		if (reliability < RELIABILITY_THRESHOLD){
    			result = Double.MAX_VALUE;
    		}
    		else{
    			result = w1 * cost + w2 * time;
    			if (result < bestResult){ //minimisation is the goal
    				bestResult 			= result;
    				bestConfiguration	= configuration;
    			}
    		}
    		configResultsArray[configuration] = result;
    	}
    	return bestConfiguration;
    }
    
    
    private int[] configurationToServices(int bestConfiguration){    	
    	final int OPERATIONS = operationsList.size();
    	final int SERVICES   = operationsList.get(0).size();
    	
    	int[] servicesConfiguration = new int[OPERATIONS];
    	
    	//generate the divisors
    	int[] divisor = new int[OPERATIONS];//{243, 81, 27, 9, 3, 1}; //{#srv^(#op-1), #srv^(#op-2), ..., #srv^(0)}
    	for (int i=0; i<OPERATIONS; i++){
    		divisor[OPERATIONS-1-i] = (int) Math.pow(SERVICES, i);
    	}
//    	System.out.println(Arrays.toString(divisor));
    	
    	int x, num;
    	System.out.printf("%d)\t", bestConfiguration);
    	for (int i=0; i<divisor.length; i++){
        	x = bestConfiguration / divisor[i];
        	
        	System.out.print(x +" ");
        	servicesConfiguration[i] = x;
        	bestConfiguration -= (divisor[i] * x);
    	}//for    	
    	
    	return servicesConfiguration;
    }
    
    
}
