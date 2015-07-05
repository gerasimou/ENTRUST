package sbs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import soar.ws.fx.services.AbstractServiceClient;
import soar.ws.fx.services.ServiceFactory;
import soar.ws.fx.services.WM.MarketWatchClient1;
import soar.ws.fx.services.WM.WatchMarketService1ExceptionException;

/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBS {

	private static Properties 					prop   = new Properties();
	private List<AbstractServiceClient> 		mwList; //market watch
	private List<AbstractServiceClient>			taList; //technical analysis
	private List<AbstractServiceClient>			faList; //fundamental analysis
	private List<AbstractServiceClient>			alList; //alarm
	private List<AbstractServiceClient> 		orList; //order
	private List<AbstractServiceClient>			noList; //notification
	private List<List<AbstractServiceClient>>	srvList;//service list 

	
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
    	
    	//load properties
    	prop.load(new FileInputStream("resources/config.properties"));

    	//make initialisations
    	init();
	}
	
	
    /**
     * set up & init services
     */
    private void init(){
    	Set<Entry<Object, Object>> entrySet = prop.entrySet();
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
	
    
    
    public void run(int seconds){
       	long startTime 	= System.currentTimeMillis();
    	long stopTime	= startTime + seconds * 1000;
    	
		long timeNow 	= startTime;
		while (stopTime >= timeNow){
			((MarketWatchClient1)srvList.get(0).get(0)).runReflection();
			timeNow = System.currentTimeMillis();
		} 
     }
    
}
