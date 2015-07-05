package soar.ws.fx.services.WM;

import java.rmi.RemoteException;

import soar.ws.fx.services.AbstractServiceClient;

public class MarketWatchClient1 extends AbstractServiceClient{

	//stub
	WatchMarketService1Stub stub;

	public MarketWatchClient1(String ID, double reliability, double cost, double responseTime, Class cls) throws RemoteException{
		super(ID, reliability, cost, responseTime);
		
		init(cls);
		
		//init Stub
		stub = new WatchMarketService1Stub();
		
		initialise(ID, reliability, cost, responseTime);
	}	
	
	
	private void init(Class cls){
		try {
			Object instance = cls.newInstance();
//			(cls).
//			((cls)MarketWatchInterface).InitialiseService test = new ((cls)MarketWatchInterface).InitialiseService();
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final void initialise(String ID, double reliability, double cost, double responseTime) throws RemoteException{
		//Initialise
		WatchMarketService1Stub.InitialiseService initFunction = new WatchMarketService1Stub.InitialiseService();
		initFunction.setReliability(reliability);
		initFunction.setInvocationCost(cost);
		initFunction.setInvocationTime(responseTime);
		initFunction.setFailurePatternTime(null);
		initFunction.setFailurePatternDegradation(null);
		initFunction.setID(this.getClass().getName());
		stub.initialiseService(initFunction);	
	}		
	
	
	public void run() throws RemoteException, WatchMarketService1ExceptionException{
		//Run
		WatchMarketService1Stub.Run runFunction = new WatchMarketService1Stub.Run();
		runFunction.setParam("Simos");		
		WatchMarketService1Stub.RunResponse runResponse = stub.run(runFunction);
		System.out.println(runResponse.get_return());
	}
}
