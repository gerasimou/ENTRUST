package sbs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;
import soar.ws.fx.services.AbstractServiceClient;
import soar.ws.fx.services.ServiceClient;
import soar.ws.fx.services.ServiceFactory;

/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBS {

	private List<AbstractServiceClient> 		mwList; //market watch
	private List<AbstractServiceClient>			taList; //technical analysis
	private List<AbstractServiceClient>			faList; //fundamental analysis
	private List<AbstractServiceClient>			alList; //alarm
	private List<AbstractServiceClient> 		orList; //order
	private List<AbstractServiceClient>			noList; //notification
	private List<List<AbstractServiceClient>>	srvList;//service list 

	private final long SIMULATION_TIME;
	
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
    	noList = new ArrayList<AbstractServiceClient>();
    	srvList= new ArrayList<List<AbstractServiceClient>>();
    	srvList.add(mwList);	srvList.add(taList);
    	srvList.add(faList);	srvList.add(alList);
    	srvList.add(orList);	srvList.add(noList);
    	
    	SIMULATION_TIME = Long.parseLong(Utility.getProperty("SIMULATION_TIME"));

    	//make initialisations
    	initClients();
	}
	
	
    /**
     * set up & init services
     */
    private void initClients(){
    	Set<Entry<Object, Object>> entrySet = Utility.getPropertiesEntrySet();
    	
    	Iterator<Entry<Object, Object>> it = entrySet.iterator();
    	while (it.hasNext()){
    		Entry<Object, Object> entry = it.next();
    		String entryKey 	= entry.getKey().toString();
    		String entryDetails	= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
    		
    		//if this entry is a service, create it
    		if (entryKey.contains("SRV")){
    			ServiceFactory.createService(entryKey, entryDetails, srvList);
    		}
    	}
    }
	
    
    
    public void run(){
       	long startTime 		= System.currentTimeMillis();
    	long stopTime		= startTime + SIMULATION_TIME;
    	
		long timeNow 	= startTime;
		try {
			List<AbstractServiceClient> clientList = srvList.get(0);
			
			while (stopTime >= timeNow){	
				
				for (AbstractServiceClient client : clientList)
					((ServiceClient)client).execute();

				timeNow = System.currentTimeMillis();
			}
			
			for (AbstractServiceClient client : clientList){
				ServiceClient serviceClient = (ServiceClient)client;
				System.out.println(serviceClient.getID() +"\t"+ serviceClient.getReliability() +"\t"+ serviceClient.getNominalReliability());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		};
     }
    
}
