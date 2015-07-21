package fx.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import fx.services.AbstractServiceClient;


/**
 * Generic class representing a service client. 
 * The class uses reflection to generate the client 
 * and to specialise it to the particular service client  
 * @author sgerasimou
 */

public class ServiceClient extends AbstractServiceClient{

	private Class<?> cls;
	
	//stub
	private Object stubReflection;
	
	//run handles
	private Class<?> runClass;
	private Constructor<?> runConstructor;
	private Object runInstance;
	private Method setParam;
	private Method runStubMethod;
	private Class<?> runResponseClass;
	private Method getReturn;
	
	
	/**
	 * Class constructor: Initialises a ServiceClient instance and instantiates the stubs for the given service class
	 * @param ID
	 * @param reliability
	 * @param cost
	 * @param responseTime
	 * @param failureTimePatter
	 * @param failureDegradationPattern
	 * @param cls
	 * @throws RemoteException
	 */
	public ServiceClient(String ID, double reliability, double cost, double responseTime, 
							  String failureTimePatter, String failureDegradationPattern, Class cls) throws RemoteException{
		super(ID, reliability, cost, responseTime);
		
		initReflection(ID, reliability, cost, responseTime, failureTimePatter, failureDegradationPattern, cls);
		initRunReflection(cls);
		this.cls = cls;
		System.out.println(this.toString());
	}	
	
	
	/**
	 * Initialises reflection parameters
	 * @param ID
	 * @param reliability
	 * @param cost
	 * @param responseTime
	 * @param failureTimePattern
	 * @param failureDegradationPattern
	 * @param cls
	 */
	private void initReflection(String ID, double reliability, double cost, double responseTime, 
					  String failureTimePattern, String failureDegradationPattern, Class cls){
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
			setFailurePatternTime.invoke(initialiseServiceInstance, failureTimePattern);
			
			//Find and invoke setFailurePatternDegradation method
			Method setFailurePatternDegradation = initialiseServiceClass.getMethod("setFailurePatternDegradation", String.class);
			setFailurePatternDegradation.invoke(initialiseServiceInstance, failureDegradationPattern);
			
			//Find and invoke setID method
			Method setID = initialiseServiceClass.getMethod("setID", String.class);
			setID.invoke(initialiseServiceInstance, ID);
			
			//Find initialiseService stub method
			Method initialiseServiceStubMethod = cls.getMethod("initialiseService", initialiseServiceClass);
			initialiseServiceStubMethod.invoke(stubReflection, initialiseServiceInstance);
			
//			System.out.println(initialiseServiceInstance.toString() + "\tinit()");

		} catch (InstantiationException | IllegalAccessException | SecurityException | ClassNotFoundException |
				 IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialises the run service method using reflection
	 * @param cls
	 */
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
			
		} catch (InstantiationException | IllegalAccessException | SecurityException | ClassNotFoundException |
				 IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Main client service that executes the functionality provided by this function. 
	 */
	@Override
	public void execute(){
		try {		
			//invoke setParam() to set the parameter for the run function
			setParam.invoke(runInstance, "");
			
			//invoke  run() and get the response
			Object response = runStubMethod.invoke(stubReflection, runInstance);
			
			//parse the response and instantiate the result
			Object result = getReturn.invoke(response);
			
			//print the result
//			System.out.println(runInstance.toString().substring(runInstance.toString().lastIndexOf('.')+1) +"\tOutput: "+ result.toString());

			//update reliability indicators
			timesInvoked++;
			if (result.toString().contains("RUN"))
				timesSucceeded++;
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * Invokes the service and requests its nominal reliability 
	 * @return the nominal reliability fort this service
	 */
	public double getNominalReliability(){
		try{
			//Find Run inner class
			Class<?> reliabilityClass = Class.forName(cls.getName()+"$GetNominalReliability");
	
			//Find InitialiseService inner class constructor
			Constructor<?> reliabilityConstructor = reliabilityClass.getDeclaredConstructor();
			
			//Create initialiseService instance
			Object reliabilityInstance = reliabilityConstructor.newInstance();
	
	//		Find and invoke setParam method
//			Method getReliability = reliabilityClass.getMethod("getNominalReliability");			
	
			//Find initialiseService stub method
			Method gerReliabilityStubMethod = cls.getMethod("getNominalReliability", reliabilityClass);
	
			//Find RunResponse inner class
			Class<?> reliabilityResponseClass = Class.forName(cls.getName()+"$GetNominalReliabilityResponse");
	
			//Find and invoke setParam method
			Method geReliabilitytReturn = reliabilityResponseClass.getMethod("get_return");
			
			Object response = gerReliabilityStubMethod.invoke(stubReflection, reliabilityInstance);
			
			Object result = geReliabilitytReturn.invoke(response);
			
			return (double)result;
//			System.out.println(runInstance.toString() +"\tOutput: "+ result.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.exit(-1);
		return -1;
	}
}
