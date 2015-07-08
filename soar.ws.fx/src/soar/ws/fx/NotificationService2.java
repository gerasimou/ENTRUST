package soar.ws.fx;

import javax.jws.WebParam;

public class NotificationService2 extends AbstractService{
	
	public String run(@WebParam(name="param") String param) throws Exception {
		String str = "["+ id +"] - " + param;
		timesInvoked++;
		if (isServiceOK()){
			timesSucceeded++;
			str += " - RUN";
		}
		else{
			str += " - FAIL";
		}
		str += "(" + timesSucceeded +"/"+ timesInvoked +")";
		System.out.println(str);
		return str;
	}

	
	/**
	 * Initialise service
	 * @param reliability
	 * @param invocationCost
	 * @param invocationTime
	 * @param failurePatternTime
	 * @param failurePatternDegradation
	 * @param ID
	 */
	@Override
	public void initialiseService(double reliability, double invocationCost, double invocationTime, 
				  String failurePatternTime, String failurePatternDegradation, String ID){
		super.initialiseService(reliability, invocationCost, invocationTime, failurePatternTime, failurePatternDegradation, ID);
	}
	
	
	@Override
	public double getNominalReliability(){
		return super.getNominalReliability();
	}

}
