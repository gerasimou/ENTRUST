package managedSystem.fx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;

/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBS implements Runnable{

	/** Lists that keep instances of services for each operation */
	private List<AbstractServiceClient> 		mwList; //market watch
	private List<AbstractServiceClient>			taList; //technical analysis
	private List<AbstractServiceClient>			faList; //fundamental analysis
	private List<AbstractServiceClient>			alList; //alarm
	private List<AbstractServiceClient> 		orList; //order
	private List<AbstractServiceClient>			notList; //notification

	/** List of lists that maintains the services for each operation */
	private List<List<AbstractServiceClient>>	operationsList;//service list of lists
	
	/** List that stores the currently active lists for each operation*/
	private List<AbstractServiceClient> activeServicesList; 
	private int[] activeServicesArray;

	/** time for running a scenario*/
	private final long SIMULATION_TIME;
	
	/** time between successive managing system invocations*/
	private final long TIME_WINDOW;
	
	/** Managing system handler*/
//	private ManagingSystemOld managingSystem;
	
	
	
	/**
	 * Constructor: Create the SBS system 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SBS() throws FileNotFoundException, IOException{
    	//initialise operations lists
    	mwList = new ArrayList<AbstractServiceClient>();
    	taList = new ArrayList<AbstractServiceClient>();
    	faList = new ArrayList<AbstractServiceClient>();
    	alList = new ArrayList<AbstractServiceClient>();
    	orList = new ArrayList<AbstractServiceClient>();
    	notList = new ArrayList<AbstractServiceClient>();
    	operationsList= new ArrayList<List<AbstractServiceClient>>();
    	operationsList.add(mwList);	operationsList.add(taList);
    	operationsList.add(faList);	operationsList.add(alList);
    	operationsList.add(orList);	operationsList.add(notList);
    	
    	activeServicesList = new ArrayList<AbstractServiceClient>();
    	activeServicesArray = new int[operationsList.size()];
    	
    	SIMULATION_TIME = Long.parseLong(Utility.getProperty("SIMULATION_TIME"));
    	TIME_WINDOW		= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
    	
//    	managingSystem = new ManagingSystemOld(this);

    	//make initialisations
    	initServiceClients();
	}
	
	
    /** set up & init services */
    private void initServiceClients(){
    	Set<Entry<Object, Object>> entrySet = Utility.getPropertiesEntrySet();
    	
    	Iterator<Entry<Object, Object>> it = entrySet.iterator();
    	while (it.hasNext()){
    		Entry<Object, Object> entry = it.next();
    		String entryKey 	= entry.getKey().toString();
    		String entryDetails	= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
    		
    		//if this entry is a service, create it
    		if (entryKey.contains("SRV")){
    			ServiceFactory.createService(entryKey, entryDetails, operationsList);
    		}
    	}
    	
    	//check whether there is at least one service for each operation
    	for (List<AbstractServiceClient> aList : operationsList){
    		if (aList.size()==0)
    			throw new IllegalArgumentException("There is an operation with no services (no implementation exists)");
    	}
    }

    
    /**
     * Updates the list of active services
     * @param activeSrvList
     */
    public void setActiveServicesList(List<AbstractServiceClient> activeSrvList){
    	this.activeServicesList = activeSrvList;
    }

    
    /**
     * Updates the array keeping the list of active services
     * @param activeServices
     */
    public void setActiveServicesList(int[] activeServices){
    	for (int serviceIndex=0; serviceIndex<activeServicesArray.length; serviceIndex++){
    		activeServicesArray[serviceIndex] = activeServices[serviceIndex];
    	}
    }
    
    
    /**
     * Retrieves the list of system operations
     * @return
     */
    public List<List<AbstractServiceClient>> getOperationsList(){
    	return this.operationsList;
    }
    
    
    //TODO SBS workflow
    //TODO Connect PRISM executor
    //TODO Integrate ActivForms
    /** Run function that simulates the workflow of the FX system*/
    public void run(){
       	long startTime 				= System.currentTimeMillis(); 	//time that the simulation started. i.e., now
    	long stopTime				= startTime + SIMULATION_TIME; 	//time for ending the simulation, i.e., now + SIMULATION_TIME 
    	long managingSystemCallTime	= 0;							//time for the latest managing system invocation
		long timeNow 	= startTime;
		try {			

			//wait to be notified by the managing system when to start executing
			synchronized (this){ 
				this.wait();				
			}
			
			
			while (stopTime >= timeNow){
				
				for (int index=0; index< activeServicesArray.length; index++){
					int activeServiceIndex = activeServicesArray[index];
					AbstractServiceClient serviceClient = (AbstractServiceClient)operationsList.get(index).get(activeServiceIndex);
					serviceClient.execute();
				}//for

				timeNow = System.currentTimeMillis();
			}//while
			
			throw new Exception("Check me - Simulation purposes");
			//printActiveServicesFeatures();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
     }
    
    
    /** Print the features of the currently active services*/
    /*private void printActiveServicesFeatures(){
		System.out.println("\nFinal active services reliability \n-------------------------------------- \nID\tREP/REQ\t\tRel. vs Nominal Rel.");
		for (int index=0; index< activeServicesArray.length; index++){
			int activeServiceIndex = activeServicesArray[index];
			AbstractServiceClient serviceClient = (AbstractServiceClient)operationsList.get(index).get(activeServiceIndex);
			System.out.println(serviceClient.getID() +"\t"+ serviceClient.getReliabilityAsString() +"\t\t["+ 
					   serviceClient.getReliabilityAsString() +" vs "+ ((ServiceClient)serviceClient).getNominalReliability() +"]");
		}
    }*/
    
    
    /** Print the IDs of active services */
    public void printActiveServices(){
    	StringBuilder activeServices = new StringBuilder("[");
		for (int index=0; index< activeServicesArray.length; index++){
			int activeServiceIndex = activeServicesArray[index];
			AbstractServiceClient serviceClient = (AbstractServiceClient)operationsList.get(index).get(activeServiceIndex);
			activeServices.append(serviceClient.getID() + ",");
		}
		activeServices.append("]");
    	System.err.printf("Active Services:\t %s\n\n", activeServices.toString());
    }

}
