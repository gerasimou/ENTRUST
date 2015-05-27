package org.spg.Service.Based.System.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractService {
	
	/**
	 * service reliability
	 */
	private double nominalReliability;
	
	/**
	 * cost per invocation
	 */
	private double costPerInvocation;
	
	/**
	 * time required to carry out its functionality per invocation
	 */
	double timePerInvocation;
	
	/**
	 * list structure keeping the starting time of failures 
	 */
	private List<Long> 		failureStartList;
	
	/**
	 * list structure keeping the stopping time of failures
	 */
	private List<Long> 		failureStopList;

	/**
	 * list structure keeping the failure percentage (i.e degradation percentage from the nominal value)
	 */
	private List<Double> 	failureDegradationList;
	
	/**
	 * Keeps the time pattern of failures
	 */
	private String failureTimePattern;	
	
	/**
	 * Keeps the degradation pattern of failures
	 */
	private String failureDegradationPattern;

	/**
	 * Keeps the current index of a failure
	 */
	private int failureIndex;
	
	/**
	 * internal clock
	 */
	private Long timeNow;
	
	/**
	 * Random variable denoting the probability of failure/success
	 */
	protected Random rand;
	
	/**
	 * Service ID
	 */
	protected String id;
	
	protected int timesInvoked;
	
	protected int timesSucceeded;


	/**
	 * Constructor: failure patterns are given
	 * @param reliability
	 * @param costPerInvocation
	 * @param timePerInvocation
	 * @param failureTimePattern
	 * @param failureDegradationPattern
	 */
	public AbstractService(double reliability, double costPerInvocation, double timePerInvocation, 
						   String failureTimePattern, String failureDegradationPattern, String ID){
		this.nominalReliability 				= reliability;
		this.costPerInvocation			= costPerInvocation;
		this.timePerInvocation			= timePerInvocation;
		this.failureStartList			= new ArrayList<Long>();
		this.failureStopList			= new ArrayList<Long>();
		this.failureDegradationList		= new ArrayList<Double>();
		this.failureIndex				= 0;
		this.timeNow					= System.currentTimeMillis();
		this.rand						= new Random(timeNow);
		this.id							= ID;
		this.failureTimePattern			= failureTimePattern;
		this.failureDegradationPattern	= failureDegradationPattern;
		this.timesInvoked				= 0;
		this.timesSucceeded				= 0;
		
		if ( (failureTimePattern != null) && (failureDegradationPattern != null)){
			initFailurePattern(failureTimePattern, failureDegradationPattern);
		}
	}
	
	
	/**
	 * Constructor: no failure pattern
	 * @param reliability
	 * @param costPerInvocation
	 * @param timePerInvocation
	 */
	public AbstractService(double reliability, double costPerInvocation, double timePerInvocation, String ID){
		this(reliability, costPerInvocation, timePerInvocation, null, null, ID);
	}
	
	
	private void initFailurePattern(String failureTimePattern, String failureDegradationPattern){
		//parse failure pattern and populate failureStop & failureStart list structures

		//check for errors
		if (failureTimePattern.isEmpty() || failureDegradationPattern.isEmpty()){
			return;
		}
		
		//parse failure time pattern
		failureTimePattern = failureTimePattern.replaceAll("\\s+","");//remove whitespaces
		String[] failureIntervals = failureTimePattern.split("-");
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
	
	
	protected boolean isServiceOK(){
		timeNow = System.currentTimeMillis();

		Long failureStartTime	= failureStartList.get(failureIndex);
		Long failureStopTime	= failureStopList.get(failureIndex);

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
			if (rand.nextDouble() > newReliability){
				return false;
			}
		}
		//if the failure has passed, the service should recover
		else if (timeNow > failureStopTime){
			failureIndex++;
		}		
		return true;
	}
	
	
	/**
	 * Get cost per invocation
	 * @return
	 */
	protected double getCostPerIncovation(){
		return this.costPerInvocation;
	}
	
	
	/**
	 * Get time required to carry out its functionality per invocation
	 * @return
	 */
	protected double getTimePerInvocation(){
		return this.timePerInvocation;
	}
	
	
	/**
	 * Get service reliability
	 * @return
	 */
	protected double getReliability(){
		return this.nominalReliability;
	}
	
	public String toString(){
		return "[" + id +"$ r:"+ nominalReliability +", t:"+ timePerInvocation +", c:" + costPerInvocation 
				+", "+ failureTimePattern +","+ failureDegradationPattern + "]"; 
	}
	
	public abstract String run();
}
