package soar.ws.fx;

public class WatchWatchService2 extends AbstractService{

	@Override
	public String run(String param) throws Exception {
		String str = "["+WatchWatchService2.class.getName() + ":" + id +"] - " + param;
		timesInvoked++;
		if (isServiceOK()){
			str += " - RUN";
			timesSucceeded++;
		}
		else{
			str += " - FAIL";
		}
		str += "(" + timesSucceeded +"/"+ timesInvoked +")";
		System.out.println(str);
		return str;
	}
	
	
	@Override
	/**
	 * 
	 */
	public void initialiseService(double reliability, double invocationCost, double invocationTime, 
				  String failurePatternTime, String failurePatternDegradation, String ID){
		super.initialiseService(reliability, invocationCost, invocationTime, failurePatternTime, failurePatternDegradation, ID);
	}
	
}
