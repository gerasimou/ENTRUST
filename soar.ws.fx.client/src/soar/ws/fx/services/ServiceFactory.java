package soar.ws.fx.services;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import soar.ws.fx.services.WM.MarketWatchClient1;
import soar.ws.fx.services.WM.MarketWatchInterface;

public class ServiceFactory {

	private ServiceFactory() {}
	
	//Enumeration mapping service name to ID
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
	
	//Enumeration mapping service name + ID to stub
	private enum STUB{
		MARKET_WATCH_1 ("WatchMarketService1Stub"),
		MARKET_WATCH_2 ("WatchMarketService2Stub"),
		MARKET_WATCH_3 ("WatchMarketService3Stub"),
    	UNKNOWN("");
		
    	private final String code;

		private STUB(String code){
			this.code = code;
		}
		
    	protected String getCode(){
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
    
    
    //SRV_SERVICE_NAME_NUMBER
    private static STUB getSTUB(String key){
    	int from  		= key.indexOf('_');
    	String keyNew 	= key.substring(from+1, key.length());
    	STUB stub		= STUB.valueOf(keyNew.toUpperCase(Locale.ENGLISH));
    	return stub;
    }

    
    public static void createService (String key, String properties, List<List<AbstractServiceClient>> srvList){
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
        		case MARKET_WATCH: {
        						STUB stub = getSTUB(key);
        						System.out.println("soar.ws.fx.services.WM."+stub.getCode());
        						Class<?> cls = Class.forName("soar.ws.fx.services.WM." + stub.getCode());
//        						Object instance = cls.newInstance();
        						srvList.get(0).add(new MarketWatchClient1(id, reliability, costPerInvocation, timePerInvocation, 
        																  failureTimePattern, failureDegradationPattern, cls));        						
        						break;}
				default:{}
			}
    	}
    	catch (IllegalArgumentException iae){
    		System.err.println("IllegalArgumentException: Abstract service not found: " + key);
    		System.err.println(iae.getLocalizedMessage());
    		iae.printStackTrace();
    		System.exit(0);
    	}
    	catch (RemoteException rme){
    		System.err.println("RemoteException: stub error");
    		System.err.println(rme.getLocalizedMessage());
    		System.exit(0);
    	} 
    	catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
    }

}
