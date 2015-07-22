package fx.services.MW;

import java.rmi.RemoteException;

public class MarketWatchClient2 {

	//stub
	MarketWatchService2Stub stub;

	public MarketWatchClient2() throws RemoteException{		
		//init Stub
		stub = new MarketWatchService2Stub();
	}
	
	public void initialise(String test) throws RemoteException{
		//Initialise
		MarketWatchService2Stub.InitialiseService initFunction = new MarketWatchService2Stub.InitialiseService();
		initFunction.setReliability(0.9);
		initFunction.setInvocationCost(1);
		initFunction.setInvocationTime(0.5);
		initFunction.setFailurePatternTime(null);
		initFunction.setFailurePatternDegradation(null);
		initFunction.setID(this.getClass().getName());
		stub.initialiseService(initFunction);	
	}		
	
	
	public void run() throws RemoteException, MarketWatchService2ExceptionException{
		for (int i=0; i<10; i++){		
			//Run
			MarketWatchService2Stub.Run runFunction = new MarketWatchService2Stub.Run();
			runFunction.setParam("Simos");		
			MarketWatchService2Stub.RunResponse runResponse = stub.run(runFunction);
			System.out.println(runResponse.get_return());
		}
	}
	
	public double getNominalReliability(){
		try {
			MarketWatchService2Stub.GetNominalReliability reliabilityFunction = new MarketWatchService2Stub.GetNominalReliability();
			MarketWatchService2Stub.GetNominalReliabilityResponse relResponse = stub.getNominalReliability(reliabilityFunction);
			return (relResponse.get_return());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
