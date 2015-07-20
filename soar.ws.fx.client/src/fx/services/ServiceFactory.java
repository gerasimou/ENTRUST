package fx.services;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

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
		MARKET_WATCH_1 			("MW.MarketWatchService1Stub"),
		MARKET_WATCH_2 			("MW.MarketWatchService2Stub"),
		MARKET_WATCH_3 			("MW.MarketWatchService3Stub"),
		TECHNICAL_ANALYSIS_1 	("TA.TechnicalAnalysisService1Stub"),
		TECHNICAL_ANALYSIS_2 	("TA.TechnicalAnalysisService2Stub"),
		TECHNICAL_ANALYSIS_3 	("TA.TechnicalAnalysisService3Stub"),
		FUNDAMENTAL_ANALYSIS_1 	("FA.FundamentalAnalysisService1Stub"),
		FUNDAMENTAL_ANALYSIS_2 	("FA.FundamentalAnalysisService2Stub"),
		FUNDAMENTAL_ANALYSIS_3 	("FA.FundamentalAnalysisService3Stub"),
		ORDER_1 				("OR.OrderService1Stub"),
		ORDER_2 				("OR.OrderService2Stub"),
		ORDER_3 				("OR.OrderService3Stub"),
		ALARM_1 				("AL.AlarmService1Stub"),
		ALARM_2 				("AL.AlarmService2Stub"),
		ALARM_3 				("AL.AlarmService3Stub"),
		NOTIFICATION_1 			("NOT.NotificationService1Stub"),
		NOTIFICATION_2 			("NOT.NotificationService2Stub"),
		NOTIFICATION_3 			("NOT.NotificationService3Stub");
		
    	private final String code;
    	private final int index;

		private STUB(String code){
			this.code = code;
			if (code.contains("MW")){
				index = 0;
			}
			else if (code.contains("TA")){
				index = 1;
			}
			else if (code.contains("FA")){				
				index = 2;
			}
			else if (code.contains("AL")){
				index = 3;
			}
			else if (code.contains("OR")){
				index = 4;
			}
			else if (code.contains("NOT")){
				index = 5;
			}
			else
				throw new IllegalArgumentException();
		}
		
    	protected String getCode(){
    		return this.code;
    	}
    	
    	protected int getIndex(){
    		return this.index;
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
        	String failureTimePattern 		 ;
        	String failureDegradationPattern ;
        	try{
        		failureTimePattern 			= propertiesArray[4];
        		failureDegradationPattern 	= propertiesArray[5];
        	}
        	catch (ArrayIndexOutOfBoundsException ex){
        		failureTimePattern 			= "";
        		failureDegradationPattern 	= "";        		
        	}
        	
        	//get the stub for the service
			STUB stub = getSTUB(key);
			//find its stub class
			Class<?> cls = Class.forName("fx.services." + stub.getCode());
			
			//create the client
			ServiceClient srvClient = new ServiceClient(id, reliability, costPerInvocation, timePerInvocation, 
														failureTimePattern, failureDegradationPattern, cls);

			//add the client object to the appropriate service list
			srvList.get(stub.getIndex()).add(srvClient);
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
