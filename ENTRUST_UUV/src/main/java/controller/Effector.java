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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
    int onSensor, offSensor, changeSpeed, planExecuted, noPlanningRequired, noAnalysisRequired;

    /** new UUV configuration*/
    private int[] newConfiguration;

    
    
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
		onSensor 				= engine.getChannel("onSensor");
		offSensor 				= engine.getChannel("offSensor");
		changeSpeed 			= engine.getChannel("changeSpeed");
		planExecuted			= engine.getChannel("planExecuted");
		noPlanningRequired		= engine.getChannel("noPlanningRequired");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
	
		//register signals to MAPE virtual machine
		engine.register(onSensor, this, "sensorId", "currentConfiguration");
		engine.register(offSensor, this, "sensorId", "currentConfiguration");
		engine.register(changeSpeed, this, "newSpeed", "currentConfiguration");
		engine.register(planExecuted, this, "currentConfiguration");
		engine.register(noPlanningRequired, this, "currentConfiguration");
		engine.register(noAnalysisRequired, this, "currentConfiguration");
		
		//reset configuration
    	Arrays.fill(newConfiguration, -1);
    }



    /**
     * Function executed when Effector receives a signal (one of the registered signals).
     * Upon receiving such signal, the Effector must realise the appropriate action 
     * to the managed system 
     */
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	System.out.println(this.getClass().getSimpleName() + ".receive()\t\t" + data.get("currentConfiguration"));
    	if (channelId == onSensor){
		    int sensorId = (Integer) data.get("sensorId");
		    newConfiguration[sensorId] = 1;
		    System.out.println("\t Sensor " + sensorId +": ON");
		}
		else if (channelId == offSensor){
		    int sensorId = (Integer) data.get("sensorId");
		    newConfiguration[sensorId] = 0;
		    System.out.println("\t Sensor " + sensorId +": OFF");
		}
		else if (channelId == changeSpeed){
		    int newSpeed = (Integer) data.get("newSpeed");
		    newConfiguration[3] = newSpeed;
		}
		else if (channelId == planExecuted){
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
    	String result = "";

    	for (int index=0; index<newConfiguration.length; index++){
    		int tempResult = newConfiguration[index];
    		if (index==3 && tempResult!=-1)
    			result += tempResult/ENTRUST.MULTIPLIER;
    		else
    			result += tempResult +",";
    	}

    	System.out.printf("{New Config}:\t%s\n\n", result);
    	out.println(result);
    	out.flush();
    }

    
}
