package soar.ws.fx.services.WM;

import java.rmi.RemoteException;

public class MarketWatchClient2 {

	//stub
	WatchMarketService2Stub stub;

	public MarketWatchClient2() throws RemoteException{		
		//init Stub
		stub = new WatchMarketService2Stub();
	}
	
	public void initialise(String test) throws RemoteException{
		//Initialise
		WatchMarketService2Stub.InitialiseService initFunction = new WatchMarketService2Stub.InitialiseService();
		initFunction.setReliability(0.1);
		initFunction.setInvocationCost(1);
		initFunction.setInvocationTime(0.5);
		initFunction.setFailurePatternTime(null);
		initFunction.setFailurePatternDegradation(null);
		initFunction.setID(this.getClass().getName());
		stub.initialiseService(initFunction);	
	}		
	
	
	public void run() throws RemoteException, WatchMarketService2ExceptionException{
		for (int i=0; i<10; i++){		
			//Run
			WatchMarketService2Stub.Run runFunction = new WatchMarketService2Stub.Run();
			runFunction.setParam("Simos");		
			WatchMarketService2Stub.RunResponse runResponse = stub.run(runFunction);
			System.out.println(runResponse.get_return());
		}
	}
}
