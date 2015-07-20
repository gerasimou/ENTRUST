package fx.services.TA;

import java.rmi.RemoteException;

public class TechnicalAnalysisClient {

	//stub
	TechnicalAnalysisService1Stub stub;

	public TechnicalAnalysisClient() throws RemoteException{		
		//init Stub
		stub = new TechnicalAnalysisService1Stub();
	}
	
	public void initialise(String test) throws RemoteException{
		//Initialise
		TechnicalAnalysisService1Stub.InitialiseService initFunction = new TechnicalAnalysisService1Stub.InitialiseService();
		initFunction.setReliability(0.0);
		initFunction.setInvocationCost(1);
		initFunction.setInvocationTime(0.5);
		initFunction.setFailurePatternTime(null);
		initFunction.setFailurePatternDegradation(null);
		initFunction.setID("TA1");
		stub.initialiseService(initFunction);	
	}		
	
	
	public void run() throws RemoteException, TechnicalAnalysisService1ExceptionException{
		for (int i=0; i<10; i++){		
			//Run
			TechnicalAnalysisService1Stub.Run runFunction = new TechnicalAnalysisService1Stub.Run();
			runFunction.setParam("Simos");		
			TechnicalAnalysisService1Stub.RunResponse runResponse = stub.run(runFunction);
			System.out.println(runResponse.get_return());
		}
	}
}
