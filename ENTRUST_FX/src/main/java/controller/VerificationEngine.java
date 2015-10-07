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
		engine.register(verify, this, "avgFRates", "currentConfig", "&RQVResultsArray");		
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
		    Object[][] parameters = new Object[ENTRUST.NUM_OF_OPERATIONS][ENTRUST.NUM_OF_SERVICES];

		    HashMap<Integer, HashMap<Integer,Integer>> avgRates = (HashMap<Integer, HashMap<Integer,Integer>>)data.get("avgFRates");

		    int row=-1;
		    for (int i=0; i<avgRates.size(); i++){
		    	
		    	HashMap<Integer, Integer> operationRates = avgRates.get(i+1);
		    	for (int serviceIndex=0; serviceIndex<operationRates.size(); serviceIndex++){
			    	if (serviceIndex%ENTRUST.NUM_OF_SERVICES==0)
			    		row++;
			    	parameters[row][serviceIndex%ENTRUST.NUM_OF_SERVICES] = operationRates.get(serviceIndex+1);		    		
		    	}//for

		    }//for
	
		    
			//Run QV
			RQVResult[] results = qv.runQV(parameters);
			
			//When done, send the results to MAPE virtual machine
		    HashMap<Integer, HashMap<String, Object>> RQVResultsArray = (HashMap<Integer, HashMap<String, Object>>)data.get("&RQVResultsArray");
		    HashMap<String, Object> RQV;
		    for(int index = 0; index < results.length; index++){
				RQV = RQVResultsArray.get(index);
				int[] configuration = results[index].getsConfig();
				for (int operationIndex=0; operationIndex<configuration.length; operationIndex++){
					int config = configuration[operationIndex];
					
					HashMap<Integer, UppaalType> sconfig = (HashMap<Integer, UppaalType>)RQV.get("sConfig");
					UppaalType type = sconfig.get(operationIndex+1);
					type.setValue(config+1);					
				}
				((UppaalType)RQV.get("RQV1Result")).setValue(results[index].getReq1Result());
				((UppaalType)RQV.get("RQV2Result")).setValue(results[index].getReq2Result());
				((UppaalType)RQV.get("RQV3Result")).setValue(results[index].getReq3Result());
		    }
		    
		    engine.send(verifDone, this);			
		}
    }
}
