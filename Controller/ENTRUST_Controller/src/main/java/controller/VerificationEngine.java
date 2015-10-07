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

public class VerificationEngine extends Synchronizer{
    
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Analyser */
    private QV qv;

    /** Signal(s)*/
	private int verify, verifDone;
   
    
   /**
    * Constructor: create a new PrismPlugin instance 
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
		engine.register(verify, this, "dataExpectedByVerificationEngine");		
    }


    /**
     * Function executed when VerificationEngine receives a signal (one of the registered signals).
     * Upon receiving such signal, the VerificationEngine carries out quantitative verification of system properties
     **/
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	System.out.println(this.getClass().getSimpleName() + ".receive()");
    	
		if (channelId == verify){
			
			//get the data structure for carrying out quantitative verification from analyser
			//TODO...
			
			
			//Run QV
			//TODO...
			RQVResult[] results = qv.runQV();
			
			
			//When done, send the results to MAPE virtual machine
		    engine.send(verifDone, this);			
		}
    }
}
