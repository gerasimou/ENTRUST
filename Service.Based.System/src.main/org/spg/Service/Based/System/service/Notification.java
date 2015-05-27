package org.spg.Service.Based.System.service;

public class Notification extends AbstractService {

	public Notification(double reliability, double costPerInvocation,double timePerInvocation, 
				 String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern, failureDegradation, ID);
	}

	public Notification(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}

	@Override
	public String run() {
		String result = "Notification (" + id + "): ";
		if (isServiceOK()){
			result += "SENT";
		}
		else{
			result += "NULL";
		}
		return result;
	}

}
