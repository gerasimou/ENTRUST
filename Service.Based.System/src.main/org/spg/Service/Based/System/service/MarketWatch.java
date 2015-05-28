package org.spg.Service.Based.System.service;

public class MarketWatch extends AbstractService {

	public MarketWatch(double reliability, double costPerInvocation,double timePerInvocation, 
					   String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern,failureDegradation, ID);
		
	}
	
	public MarketWatch(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}


	@Override
	public String run() {
		String result = "Market Watch (" + id + "): ";
		timesInvoked++;
		if (isServiceOK()){
			result += "RUN";//rand.nextDouble() * 100;
			timesSucceeded++;
		}
		else{
			result += "FAIL";
		}
		return result;
	}

}
