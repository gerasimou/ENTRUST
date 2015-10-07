//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- Verification Engine class						                                    	//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//

package controller;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;

public class VerificationEngine extends Synchronizer{
    
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** QV */
    private QV qv;

    /** Signal(s)*/
	private int verify, verifDone;
   
    
   /**
    * Constructor: create a new VerificationEngine instance 
    * @param engine
    */
    public VerificationEngine(ActivFORMSEngine engine){
    	//assign handles
		this.qv = new QV();
		this.engine   = engine;
		
		//get signal(s) ID
		verify 		= engine.getChannel("verify");
		verifDone 	= engine.getChannel("verifDone");

		//register signals to MAPE virtual machine
		engine.register(verify, this, "avgRates", "currentConfiguration", "&RQVResultsArray");		
    }


    /**
     * Function executed when VerificationEngine receives a signal (one of the registered signals).
     * Upon receiving such signal, the VerificationEngine carries out quantitative verification of system properties
     **/
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
//    	System.out.println(this.getClass().getSimpleName() + ".receive()");
    	
		if (channelId == verify){
			
			//get the data structure for carrying out quantitative verification from analyser
		    Object[] parameters = new Object[4];

		    HashMap<Integer, Integer> avgRates = (HashMap<Integer, Integer>)data.get("avgRates");		    
		    parameters[0] = avgRates.get(0)/ENTRUST.MULTIPLIER_RATES;
		    parameters[1] = avgRates.get(1)/ENTRUST.MULTIPLIER_RATES;
		    parameters[2] = avgRates.get(2)/ENTRUST.MULTIPLIER_RATES;
	
		    HashMap<Integer, HashMap> currentConfiguration = (HashMap<Integer, HashMap>)data.get("currentConfiguration");
		    String binaryString = "" + currentConfiguration.get("sensors").get(0) + currentConfiguration.get("sensors").get(1) + currentConfiguration.get("sensors").get(2);
		    parameters[3] 		= Integer.parseInt(binaryString, 2);
			
			//Run QV
			RQVResult[] results = qv.runQV(parameters);
			
			//When done, send the results to MAPE virtual machine
		    HashMap<Integer, HashMap<String, Object>> array = (HashMap<Integer, HashMap<String, Object>>)data.get("&RQVResultsArray");
		    HashMap<String, Object> RQV;
		    for(int i = 0; i < results.length; i++){
			    try{
				RQV = array.get(i);
				((UppaalType)((HashMap)RQV.get("sensors")).get(0)).setValue(results[i].getSensor1());
				((UppaalType)((HashMap)RQV.get("sensors")).get(1)).setValue(results[i].getSensor2());
				((UppaalType)((HashMap)RQV.get("sensors")).get(2)).setValue(results[i].getSensor3());
				((UppaalType)RQV.get("speed")).setValue(results[i].getSpeed());
				((UppaalType)RQV.get("req1Result")).setValue(results[i].getReq1Result());
				((UppaalType)RQV.get("req2Result")).setValue(results[i].getReq2Result());
			    }
			    catch (NullPointerException e){
			    	System.err.println(i);
			    	e.printStackTrace();
			    	System.exit(-1);
			    }
		    }
		    
		    engine.send(verifDone, this);			
		}
    }
}
