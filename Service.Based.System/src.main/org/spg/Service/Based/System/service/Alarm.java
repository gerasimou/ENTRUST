package org.spg.Service.Based.System.service;

public class Alarm extends AbstractService {

	public Alarm(double reliability, double costPerInvocation,double timePerInvocation, 
				 String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern, failureDegradation, ID);
	}

	public Alarm(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}

	@Override
	public String run() {
		String result = "Alarm (" + id + "): ";
		if (isServiceOK()){
			result += "BEEEEEEEP";
		}
		else{
			result += "NULL";
		}
		return result;
	}

}
