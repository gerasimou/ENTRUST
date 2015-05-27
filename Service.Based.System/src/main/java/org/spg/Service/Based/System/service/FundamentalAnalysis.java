package org.spg.Service.Based.System.service;

public class FundamentalAnalysis extends AbstractService {

	public FundamentalAnalysis(double reliability, double costPerInvocation, double timePerInvocation, 
							 String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern, failureDegradation, ID);
	}

	public FundamentalAnalysis(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}

	
	@Override
	public String run() {
		String result = "Fundamental Analysis (" + id + "): ";
		if (isServiceOK()){
			result += rand.nextDouble() * 100;
		}
		else{
			result += "NULL";
		}
		return result;
	}

}
