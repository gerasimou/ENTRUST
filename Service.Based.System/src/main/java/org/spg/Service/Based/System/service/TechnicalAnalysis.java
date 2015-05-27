package org.spg.Service.Based.System.service;

public class TechnicalAnalysis extends AbstractService {

	public TechnicalAnalysis(double reliability, double costPerInvocation, double timePerInvocation, 
							 String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern, failureDegradation, ID);
	}

	public TechnicalAnalysis(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}

	
	@Override
	public String run() {
		String result = "Technical Analysis (" + id + "): ";
		if (isServiceOK()){
			result += rand.nextDouble() * 100;
		}
		else{
			result += "NULL";
		}
		return result;
	}

}
