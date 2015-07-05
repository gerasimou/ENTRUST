package soar.ws.fx;

import javax.jws.WebParam;

public class TechnicalAnalysisService1 extends AbstractService{

	
	/**
	 * 
	 * @param reliability
	 * @param invocationCost
	 * @param invocationTime
	 * @param failurePatternTime
	 * @param failurePatternDegradation
	 * @param ID
	 */
	public void initialiseService (double reliability, double invocationCost, double invocationTime,
			  String failurePatternTime, String failurePatternDegradation, String ID){
		super.initialiseService(reliability, invocationCost, invocationTime, failurePatternTime, failurePatternDegradation, ID);
	}

	
	public String run(@WebParam(name="param") String param) throws Exception {
		String str = "["+TechnicalAnalysisService1.class.getName() + ":" + id +"] - " + param;
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
}
