package soar.ws.fx.services.WM;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import soar.ws.fx.services.AbstractServiceClient;

public class MarketWatchClient1 extends AbstractServiceClient{

	//stub
	WatchMarketService1Stub stub;

	Object stubReflection;
	
	//run handles
	Class<?> runClass;
	Constructor<?> runConstructor;
	Object runInstance;
	Method setParam;
	Method runStubMethod;
	Class<?> runResponseClass;
	Method getReturn;
	
	public MarketWatchClient1(String ID, double reliability, double cost, double responseTime, 
							  String failureTimePatter, String failureDegradationPattern, Class cls) throws RemoteException{
		super(ID, reliability, cost, responseTime);
		
		init(ID, reliability, cost, responseTime, failureTimePatter, failureDegradationPattern, cls);
		initRunReflection(cls);
		for (int i=0; i<1000; i++){
			runReflection();
		}
		System.out.println("TEST");
		
		//create Stub
//		stub = new WatchMarketService1Stub();
		//init
//		initialise(ID, reliability, cost, responseTime);
	}	
	
	
	private void init(String ID, double reliability, double cost, double responseTime, 
					  String failureTimePatter, String failureDegradationPattern, Class cls){
		try {
			//create stub instance
			stubReflection  				= cls.newInstance();
			
			//Find InitialiseService inner class
			Class<?> initialiseServiceClass = Class.forName(cls.getName()+"$InitialiseService");

			//Find InitialiseService inner class constructor
			Constructor<?> initialiseServiceConstructor = initialiseServiceClass.getDeclaredConstructor();
			
			//Create initialiseService instance
			Object initialiseServiceInstance = initialiseServiceConstructor.newInstance();
			
			//Find and invoke setReliability method
			Method setReliabilityMethod = initialiseServiceClass.getMethod("setReliability", Double.TYPE);
			setReliabilityMethod.invoke(initialiseServiceInstance, reliability);
			
			//Find and invoke setInvocationCost method
			Method setInvocationCostMethod = initialiseServiceClass.getMethod("setInvocationCost", Double.TYPE);
			setInvocationCostMethod.invoke(initialiseServiceInstance, cost);

			//Find and invoke setInvocationTime method
			Method setInvocationTime = initialiseServiceClass.getMethod("setInvocationTime", Double.TYPE);
			setInvocationTime.invoke(initialiseServiceInstance, responseTime);

			//Find and invoke setFailurePatternTime method
			Method setFailurePatternTime = initialiseServiceClass.getMethod("setFailurePatternTime", String.class);
			setFailurePatternTime.invoke(initialiseServiceInstance, failureTimePatter);
			
			//Find and invoke setFailurePatternDegradation method
			Method setFailurePatternDegradation = initialiseServiceClass.getMethod("setFailurePatternDegradation", String.class);
			setFailurePatternDegradation.invoke(initialiseServiceInstance, failureDegradationPattern);
			
			//Find and invoke setID method
			Method setID = initialiseServiceClass.getMethod("setID", String.class);
			setID.invoke(initialiseServiceInstance, ID);
			
			//Find initialiseService stub method
			Method initialiseServiceStubMethod = cls.getMethod("initialiseService", initialiseServiceClass);
			initialiseServiceStubMethod.invoke(stubReflection, initialiseServiceInstance);
			
			System.out.println(initialiseServiceInstance.toString() + "\tinit()");

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
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
	
	
	private void initRunReflection(Class<?> cls){
		try{
			//Find Run inner class
			runClass = Class.forName(cls.getName()+"$Run");

			//Find InitialiseService inner class constructor
			runConstructor = runClass.getDeclaredConstructor();
			
			//Create initialiseService instance
			runInstance = runConstructor.newInstance();

			//Find and invoke setParam method
			setParam = runClass.getMethod("setParam", String.class);			

			//Find initialiseService stub method
			runStubMethod = cls.getMethod("run", runClass);

			//Find RunResponse inner class
			runResponseClass = Class.forName(cls.getName()+"$RunResponse");

			//Find and invoke setParam method
			getReturn = runResponseClass.getMethod("get_return");
		} catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runReflection(){
		try {
			setParam.invoke(runInstance, "Simos");
			
			Object response = runStubMethod.invoke(stubReflection, runInstance);
			
			Object result = getReturn.invoke(response);
			
			System.out.println(runInstance.toString() +"\tOutput: "+ result.toString());

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	public void runReflection(Class<?> cls){
		try{
			//Find Run inner class
			Class<?> runClass = Class.forName(cls.getName()+"$Run");
	
			//Find InitialiseService inner class constructor
			Constructor<?> runConstructor = runClass.getDeclaredConstructor();
			
			//Create initialiseService instance
			Object runInstance = runConstructor.newInstance();
			
			//Find and invoke setParam method
			Method setParam = runClass.getMethod("setParam", String.class);
			setParam.invoke(runInstance, "Simos");
			
			//Find RunResponse inner class
			Class<?> runResponseClass = Class.forName(cls.getName()+"$RunResponse");
			
			//Find initialiseService stub method
			Method runStubMethod = cls.getMethod("run", runClass);
			Object response = runStubMethod.invoke(stubReflection, runInstance);
			
			//Find and invoke setParam method
			Method getReturn = runResponseClass.getMethod("get_return");
			Object result = getReturn.invoke(response);

			
			System.out.println(runInstance.toString() +"\tOutput: "+ result.toString());


			
		} catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
