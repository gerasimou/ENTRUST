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
import java.util.Arrays;
import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Effector extends Synchronizer{

	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

	/** Communication handle(s)*/
    private PrintWriter out;			

        
    /** Signal(s)*/
    int changeService, planExecuted, noPlanningRequired, noAnalysisRequired;

    /** new FX configuration*/
    private int[] newConfiguration;

    
    
    /**
     * Constructor: create a new effector
     * @param engine
     * @param entrustController
     */
    public Effector(ActivFORMSEngine engine, PrintWriter out){
    	//assign handlers
    	this.engine 			= engine;
		this.out				= out;
	
		//get signal(s) ID
		changeService 			= engine.getChannel("changeService");
		planExecuted			= engine.getChannel("planExecuted");
		noPlanningRequired		= engine.getChannel("noPlanningRequired");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
	
		//register signals to MAPE virtual machine
		this.engine.register(changeService, this, "serviceType", "serviceId", "newConfig");
		this.engine.register(planExecuted, this, "newConfig", "currentConfig");
		this.engine.register(noPlanningRequired, this, "currentConfig");
		this.engine.register(noAnalysisRequired, this, "currentConfig");
		
		//reset configuration
		newConfiguration = new int[ENTRUST.NUM_OF_OPERATIONS];
    	Arrays.fill(newConfiguration, -1);
    }


    /**
     * Set output stream (after sensor is connected to the managed system)
     * @param out
     */
	protected void setOutputStream(PrintWriter out){
		this.out = out;
	}

    
    /**
     * Function executed when Effector receives a signal (one of the registered signals).
     * Upon receiving such signal, the Effector must realise the appropriate action 
     * to the managed system 
     */
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	if (channelId == changeService){
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");
	    	System.out.println("\tEffector: Service " + serviceType +":"+ newConfiguration[serviceType-1] +"=>"+ (serviceId-1) );

		    // Service should be changed here
		    newConfiguration[serviceType-1] = serviceId-1;
		}
		else if (channelId == planExecuted){
	    	System.out.println(this.getClass().getSimpleName() + ".receive()\t\t" + data.get("currentConfig"));
			returnResult(newConfiguration);
		}
		else if (channelId == noPlanningRequired || channelId == noAnalysisRequired){
		    System.out.println("\t No change");
			returnResult(newConfiguration);	
		}
    }
    
    
    /** return the result to the managed system 
     * @param newConfiguration
     */
    public void returnResult(int [] newConfiguration){
    	String result = Arrays.toString(newConfiguration);

    	try{
	    	if (newConfiguration != null){
	    		System.out.printf("{New Config}:\t%s\n\n", result);
	    		out.println(result);
	    		out.flush();
	    	}
    	}
    	catch (NullPointerException e){
    		System.err.println("PrintWriter is NULL");
    	}
    }

    
}
