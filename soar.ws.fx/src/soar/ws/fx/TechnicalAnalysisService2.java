package soar.ws.fx;

public class TechnicalAnalysisService2 extends AbstractService{

	public String run(String param) throws Exception {
		String str = "["+TechnicalAnalysisService2.class.getName() + ":" + id +"] - " + param;
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
