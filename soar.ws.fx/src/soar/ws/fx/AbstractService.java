package soar.ws.fx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractService {

	/** list structure keeping the starting time of failures */
	private static List<Long> 		failureStartList;
	
	/** list structure keeping the stopping time of failures*/
	private static List<Long> 		failureStopList;
	
	/**list structure keeping the failure percentage (i.e degradation percentage from the nominal value)*/
	private static List<Double>	failureDegradationList;
	
	/** String that keeps the time pattern of failures*/
	private static String failureTimePattern;
	
	/** String that keeps the percentage pattern of failures*/
	private static String failureDegradationPattern;
	
	/** Keeps the current index of a failure */
	private int failureIndex;

	/** internal clock*/
	private static Long timeNow;
	
	/** Random variable denoting the probability of failure/success*/
	protected static Random rand;
	
	/** service reliability */
	private static double nominalReliability;
	
	/** cost per invocation */
	private static double costPerInvocation;
	
	/** time required to carry out its functionality per invocation*/
	private static double timePerInvocation;
	
	/**Service ID*/
	protected static String id;
	
	protected static int timesInvoked;
	
	protected static int timesSucceeded;
	
	
	
	/**
	 * Initiates the service
	 * @param reliability
	 * @param invocationCost
	 * @param invocationTime
	 * @param failurePatternTime
	 * @param failurePatternDegradation
	 * @param ID
	 */
	public void initialiseService(double reliability, double invocationCost, double invocationTime, 
			   					  String failurePatternTime, String failurePatternDegradation, String ID){
		nominalReliability 			= reliability;
		costPerInvocation 			= invocationCost;
		timePerInvocation			= invocationTime;
		failureStartList			= new ArrayList<Long>();
		failureStopList				= new ArrayList<Long>();
		failureDegradationList		= new ArrayList<Double>();
		failureIndex				= 0;
		timeNow						= System.currentTimeMillis();
		rand						= new Random(timeNow);
		id							= ID;
		failureTimePattern			= failurePatternTime;
		failureDegradationPattern	= failurePatternDegradation;
		timesInvoked				= 0;
		timesSucceeded				= 0;
		
		if ( (failureTimePattern != null) && (failureDegradationPattern != null)){
			initFailurePattern(failureTimePattern, failureDegradationPattern);
		}
	}
	
	
	/**
 	 * Parses the failure pattern given by the user and initialises the list structures
	 * There is no check if the failure pattern size differs from failure percentage
	 * @param failureTimePattern
	 * @param failureDegradationPattern
	 */
	private void initFailurePattern(String failureTimePattern, String failureDegradationPattern){
		//parse failure pattern and populate failureStop & failureStart list structures

		//check if empty
		if (failureTimePattern.isEmpty() &&  failureDegradationPattern.isEmpty()){
			return;
		}
		//check for errors
		if (failureTimePattern.isEmpty() || failureDegradationPattern.isEmpty()){
			throw new IllegalArgumentException("Missing argument");
		}
		
		//parse failure interval pattern
		failureTimePattern = failureTimePattern.replaceAll("\\s+","");//remove whitespaces
		String[] failureIntervals = failureTimePattern.split("-"); // get failure time intervals
		long now = System.currentTimeMillis();
		for (String failureInterval : failureIntervals){			
			String[] failure = failureInterval.split(":");
			failureStartList.add(now + Long.parseLong(failure[0])*1000);//from seconds to milliseconds
			failureStopList.add(now  + Long.parseLong(failure[1]) *1000);//from seconds to milliseconds
		}

		//parse failure degradation pattern
		failureDegradationPattern = failureDegradationPattern.replaceAll("\\s+","");//remove whitespaces
		String[] failurePercentages = failureDegradationPattern.split("-");
		for (String failure : failurePercentages){
			failureDegradationList.add(Double.parseDouble(failure));
		}

	}
	

	
	/**
	 * Checks if the service is running OK
	 * @return <b>true</b> if the service is OK, <b>false</b> otherwise
	 */
	protected boolean isServiceOK(){
		System.out.println("{Service.isServiceOK}");
		
		timeNow = System.currentTimeMillis();

		Long failureStartTime	= null;
		Long failureStopTime	= null;
		
		if (failureIndex < failureStartList.size()){
			failureStartTime = failureStartList.get(failureIndex);
			failureStopTime  = failureStopList.get(failureIndex);
		}

		//if a failure does not exist or it's earlier than its starting time
		if ( (failureStartTime == null) || (timeNow < failureStartTime) ){
			//if the generated random value is higher than service reliability --> service is NOT OK
			double num = rand.nextDouble() ;
			if ( num > nominalReliability){
				return false;
			}
		}
		//if the service will still suffer from failure & the failure occurs now
		else if ( (timeNow >= failureStartTime) && (timeNow <= failureStopTime) ){
			double newReliability = nominalReliability * (100 - failureDegradationList.get(failureIndex));
			double number = rand.nextDouble() * 100; 
//			System.out.println(number +" ? "+ newReliability);
			if (number > newReliability){
				return false;
			}
		}
		//if the failure has passed, the service should recover
		else if ( (timeNow > failureStopTime) && (failureIndex < failureStopList.size()) ) {
			failureIndex++;
		}		
		return true;
	}
	

	
	
	/**
	 * Returns the reliability associated with this object
	 * @return
	 */
	public double getNominalReliability(){		
		return nominalReliability;
	}
	
	/**
	 * Return the cost associated with this service
	 * @return
	 */
	public double getCostPerInvocation(){
		return costPerInvocation;
	}
	
	
	public double getTimePerInvocation(){
		return timePerInvocation;
	}
		
	/**
	 * Returns failure time pattern as String
	 * @return
	 */
	public String getFailureTimePattern(){
		return failureTimePattern;
	}
	
	
	/**
	 * Return failure percentage pattern as String
	 * @return
	 */
	public String getFailurePercentagePattern(){
		return failureDegradationPattern;
	}
		
	public abstract String run (String param) throws Exception;
}
