package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Effector extends Synchronizer{

	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** Managing system handle*/
    private ManagingSystem managingSystem;
    
    /** new UUV configuration*/
    private int[] newConfiguration;
    
    /** Signal(s)*/
    int onSensor, offSensor, changeSpeed, allPlanStepsExecuted, noPlanningNeeded, noAnalysisRequired;

    
    public Effector(ActivFORMSEngine engine, ManagingSystem managingSystem){
    	//assign handles
    	this.engine 		= engine;
		this.managingSystem	= managingSystem;
	
		//get signals
		onSensor 				= engine.getChannel("onSensor");
		offSensor 				= engine.getChannel("offSensor");
		changeSpeed 			= engine.getChannel("changeSpeed");
		allPlanStepsExecuted	= engine.getChannel("allPlanStepsExecuted");
		noPlanningNeeded		= engine.getChannel("noPlanningNeeded");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
		
		//register signals
		engine.register(onSensor, this, "sensorId", "currentConfiguration");
		engine.register(offSensor, this, "sensorId", "currentConfiguration");
		engine.register(changeSpeed, this, "newSpeed", "currentConfiguration");
		engine.register(allPlanStepsExecuted, this, "currentConfiguration");
		engine.register(noPlanningNeeded, this, "currentConfiguration");
		engine.register(noAnalysisRequired, this, "currentConfiguration");
    }



    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	System.out.println(this.getClass().getSimpleName() + ".receive()\t\t" + data.get("currentConfiguration"));

    	if (channelId == onSensor){
		    int sensorId = (Integer) data.get("sensorId");
		    newConfiguration[sensorId] = 1;
		}
		else if (channelId == offSensor){
		    int sensorId = (Integer) data.get("sensorId");
		    newConfiguration[sensorId] = 0;
		}
		else if (channelId == changeSpeed){
		    int newSpeed = (Integer) data.get("newSpeed");
		    newConfiguration[3] = newSpeed;
		}
		else if (channelId == allPlanStepsExecuted){
			managingSystem.returnResult(newConfiguration);	
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
			managingSystem.returnResult(newConfiguration);	
		}
    }
    
    
    public void setNewConfigurationArray(int[] newConfiguration){
    	this.newConfiguration = newConfiguration;
    }    

}