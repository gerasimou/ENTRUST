package managingSystem;

import java.util.Arrays;

import main.ManagingSystem;


//class that represents a data structure for storing an RQVResult
public class RQVResult{
	
	/** 6 service types*/
	int 	sConfig[];
	
	/** expected reliability*/
	int 	req1Result;

	/** expected cost per workflow execution*/
	int 	req2Result;
	
	/** expected response time per workflow execution*/
	int 	req3Result;
	
	
	/**
	 * Default constructor disabled
	 */
	@SuppressWarnings({"unused" })
	private RQVResult(){
		sConfig = new int[6]; // 6 service types
		req1Result 	= -1;
		req2Result 	= -1;
		req3Result	= -1;
	}
	
	
	/**
	 * Constructor: Initialise an RQVResult instance given these parameters
	 * @param sConfig
	 * @param req1Result
	 * @param req2Result
	 * @param req3Result
	 */
	public RQVResult (int sConfig[], double req1Result, double req2Result, double req3Result){
		this.sConfig = sConfig;
		this.req1Result	= (int) (ManagingSystem.MULTIPLIER_RELIABILITY * req1Result); //Math.round(req1Result);
		this.req2Result	= (int) (ManagingSystem.MULTIPLIER * req2Result);//Math.round(req2Result);
		this.req3Result = (int) (ManagingSystem.MULTIPLIER * req3Result);//Math.round(req3Result);
	}
	
	
	/**
	 * Return its representation as a String
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    str.append("sConfig:" + Arrays.toString(sConfig) + NEW_LINE);
	    str.append("Req1Result:" + req1Result + NEW_LINE);
	    str.append("Req2Result:" + req2Result + NEW_LINE);
	    str.append("Req3Result:" + req3Result + NEW_LINE);
		return str.toString();
	}
	
	
	/**
	 * Get result of QoS R1: reliability
	 * @return
	 */
	public int getReq1Result(){
		return req1Result;
	}
	
	
	/**
	 * Get result of QoS R2: cost per workflow execution
	 * @return
	 */
	public int getReq2Result(){
		return req2Result;
	}

	
	/**
	 * Get result of QoS R3: response time per workflow execution
	 * @return
	 */
	public int getReq3Result() {
	    return req3Result;
	}
	
	
	/** Get the services configuration for this particular set of results */
	public int[] getsConfig() {
	    return sConfig;
	}
}