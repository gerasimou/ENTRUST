package org.spg.Service.Based.System;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.spg.Service.Based.System.service.AbstractService;
import org.spg.Service.Based.System.service.ServiceFactory;


/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBS
{
	private static Properties 			prop   = new Properties();
	private List<AbstractService> 		mwList; //market watch
	private List<AbstractService>		taList; //technical analysis
	private List<AbstractService>		faList; //fundamental analysis
	private List<AbstractService>		alList; //alarm
	private List<AbstractService> 		orList; //order
	private List<AbstractService>		noList; //notification
	private List<List<AbstractService>> srvList;//service list 
	private long 						timeNow;
	private Random						rand;
	private SBSController				controller;
	
	
    /**
     * Read the config.properties file and initialise the services
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws Exception 
     */
    public SBS () throws FileNotFoundException, IOException{
    	mwList = new ArrayList<AbstractService>();
    	taList = new ArrayList<AbstractService>();
    	faList = new ArrayList<AbstractService>();
    	alList = new ArrayList<AbstractService>();
    	orList = new ArrayList<AbstractService>();
    	noList = new ArrayList<AbstractService>();
    	srvList= new ArrayList<List<AbstractService>>();
    	srvList.add(mwList);	srvList.add(taList);
    	srvList.add(faList);	srvList.add(alList);
    	srvList.add(orList);	srvList.add(noList);
    	
    	prop.load(new FileInputStream("config.properties"));
    	
    	timeNow = System.currentTimeMillis();
    	rand 	= new Random(timeNow);
    	    	
    	init();
    	
    	controller = new SBSController(srvList);

    }
    

    private void init(){
    	Set<Entry<Object, Object>> entrySet = prop.entrySet();
    	Iterator<Entry<Object, Object>> it = entrySet.iterator();
    	while (it.hasNext()){
    		Entry<Object, Object> entry = it.next();
    		String serviceType 			= entry.getKey().toString();
    		String serviceProperties	= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
    		
    		if (serviceType.contains("SRV")){
    			ServiceFactory.createService(serviceType, serviceProperties, srvList);
    		}
    	}
    }
    
    
    
    public void printServices(){
		for (List<AbstractService> aList : srvList){
			System.out.println(Arrays.toString(aList.toArray()).toString());
		}
    }

    
    public void run(int seconds){
    	long timeNow 	= System.currentTimeMillis();
    	long stopTime	= timeNow + seconds * 1000;
    	
    	System.err.println(System.currentTimeMillis()/1000.0);
		try {
	    	while (stopTime >= System.currentTimeMillis()){
	    		for (List<AbstractService> aList : srvList){
	    			for (AbstractService aService : aList){
	    				System.out.println(aService.run());
	    			}
	    		}
				Thread.sleep(1000);
	    	}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.err.println(System.currentTimeMillis()/1000.0);
    }
    
}
