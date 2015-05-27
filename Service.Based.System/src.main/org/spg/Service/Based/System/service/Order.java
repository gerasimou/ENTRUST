package org.spg.Service.Based.System.service;

public class Order extends AbstractService {

	public Order(double reliability, double costPerInvocation, double timePerInvocation, 
							 String failurePattern, String failureDegradation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, failurePattern, failureDegradation, ID);
	}

	public Order(double reliability, double costPerInvocation, double timePerInvocation, String ID) {
		super(reliability, costPerInvocation, timePerInvocation, ID);
	}

	
	@Override
	public String run() {
		String result = "Order (" + id + "): ";
		if (isServiceOK()){
			result +="DONE";
		}
		else{
			result += "NULL";
		}
		return result;
	}

}
