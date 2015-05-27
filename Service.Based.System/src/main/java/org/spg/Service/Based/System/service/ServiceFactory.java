package org.spg.Service.Based.System.service;

import java.util.List;
import java.util.Locale;

public class ServiceFactory {

	private ServiceFactory() {}
	
	private enum SERVICE{
    	MARKET_WATCH ("MARKET_WATCH"), 
    	TECHNICAL_ANALYSIS ("TECHNICAL_ANALYSIS"), 
    	FUNDAMENTAL_ANALYSIS ("FUNDAMENTAL_ANALYSIS"), 
    	ORDER ("ORDER"), 
    	ALARM ("ALARM"), 
    	NOTIFICATION ("NOTIFICATION"),
    	UNKNOWN("");
    	
    	private String code;
    	
    	private SERVICE(String code){
    		this.code = code;
    	}
    	
    	private String getCode(){
    		return this.code;
    	}	
	}
    
    //SRV_SERVICE_NAME_NUMBER
    protected static SERVICE getService (String key){
    	int from = key.indexOf('_');
    	int to   = key.lastIndexOf('_');
    	String keyNew = key.substring(from+1, to);
    	SERVICE service = SERVICE.valueOf(keyNew.toUpperCase(Locale.ENGLISH));
    	return service;        
    }	
    
    
    public static void createService (String key, String properties, List<List<AbstractService>> srvList){
    	try{
        	SERVICE service = getService(key);
        	String[] propertiesArray = properties.split(",");
        	String id 						 = propertiesArray[0];
        	double reliability 				 = Double.parseDouble(propertiesArray[1]);
        	double costPerInvocation 		 = Double.parseDouble(propertiesArray[2]);
        	double timePerInvocation 		 = Double.parseDouble(propertiesArray[3]);
        	String failureTimePattern 		 = propertiesArray[4];
        	String failureDegradationPattern = propertiesArray[5];
        	switch (service){
				case MARKET_WATCH:{ 
							srvList.get(0).add(new MarketWatch(reliability, costPerInvocation, timePerInvocation, 
											       failureTimePattern, failureDegradationPattern, id));
						    break;}
				case TECHNICAL_ANALYSIS:{ 
							srvList.get(1).add(new TechnicalAnalysis(reliability, costPerInvocation, timePerInvocation, 
					  							   failureTimePattern, failureDegradationPattern, id));								
							break;}
				case FUNDAMENTAL_ANALYSIS:{ 
							srvList.get(2).add(new FundamentalAnalysis(reliability, costPerInvocation, timePerInvocation, 
													failureTimePattern, failureDegradationPattern, id));					
							break;}
				case ALARM:{
							srvList.get(3).add(new Alarm(reliability, costPerInvocation, timePerInvocation, 
													failureTimePattern, failureDegradationPattern, id));					
							break;}
				case ORDER:{ 		
							srvList.get(4).add(new Order(reliability, costPerInvocation, timePerInvocation, 
													failureTimePattern, failureDegradationPattern, id));					
							break;}
				case NOTIFICATION:{ 
							srvList.get(5).add(new Notification(reliability, costPerInvocation, timePerInvocation, 
													failureTimePattern, failureDegradationPattern, id));					
							break;}
				default:{}
			}
    	}
    	catch (IllegalArgumentException iae){
    		System.err.println("IllegalArgumentException: Abstract service not found: " + key);
    		System.err.println(iae.getLocalizedMessage());
    		System.exit(0);
    	}
    }

}
