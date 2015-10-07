//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- RQVResult class 																		//
//	  Class that represents a data structure for storing a configuratio result				//																				//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//

package controller;


public class RQVResult{
	/** system QoS requirement 1*/
	int 	req1Result; 
	
	/** system QoS requirement 2 */
	int 	req2Result; 

	
	/**
	 * Constructor: create new RQVResult instance
	 */
	public RQVResult(){
		req1Result 	= -1;
		req2Result 	= -1;
	}

	
	/**
	 * Constructor: create new RQVResult instance using the given parameters
	 * @param req1Result
	 * @param req2Result
	 */
	public RQVResult (double req1Result, double req2Result){
		this.req1Result	= (int) (Math.round(req1Result * ENTRUST.MULTIPLIER));
		this.req2Result	= (int) (Math.round(req2Result * ENTRUST.MULTIPLIER));
	}
	
	
	@Override
	/**
	 * Return the string representation of this RQVResult
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    str.append("Req1Result:" + req1Result + NEW_LINE);
	    str.append("Req2Result:" + req2Result + NEW_LINE);
		return str.toString();
	}
	

	/**
	 * Get system QoS requirement 1
	 * @return
	 */
	public int getReq1Result(){
		return req1Result;
	}

	
	/**
	 * Get system QoS requirement 2
	 * @return
	 */
	public int getReq2Result(){
		return req2Result;
	}
}