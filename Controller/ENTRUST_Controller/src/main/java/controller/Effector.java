//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- Effector class						                                    			//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//

package controller;

import java.io.PrintWriter;
import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Effector extends Synchronizer{

	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** Managing system handler*/
    private ENTRUST entrustController;
        
	/** Communication handle(s)*/
    private PrintWriter out;			

    /** Signal(s)*/
    int executorSignal1, executorSignaln;

    /**
     * Constructor: create a new effector
     * @param engine
     * @param entrustController
     */
    public Effector(ActivFORMSEngine engine, ENTRUST entrustController, PrintWriter out){
    	//assign handlers
    	this.engine 			= engine;
		this.entrustController	= entrustController;
		this.out				= out;
	
		//get signal(s) ID
		executorSignal1		= engine.getChannel("executorSignal1");
		executorSignaln 	= engine.getChannel("executorSignaln");
		
		//register signals to MAPE virtual machine
		engine.register(executorSignal1, this, "dataExpectedByEffector");
		engine.register(executorSignaln, this, "dataExpectedByEffector");
    }



    /**
     * Function executed when Effector receives a signal (one of the registered signals).
     * Upon receiving such signal, the Effector must realise the appropriate action 
     * to the managed system 
     */
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	System.out.println(this.getClass().getSimpleName() + ".receive()\t\t" + data.get("currentConfiguration"));
    	if (channelId == executorSignal1){
		    int action = (Integer) data.get("dataExpectedByEffector");
		    //realise adaptation action to the managed system
		    //TODO...
		}
		else if (channelId == executorSignaln){
		    int action = (Integer) data.get("dataExpectedByEffector");
		    //realise adaptation action to the managed system
		    //TODO...
		}
    }
    
}
