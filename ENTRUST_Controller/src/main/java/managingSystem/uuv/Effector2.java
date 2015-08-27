package managingSystem.uuv;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Effector2 extends Synchronizer{

	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** Managing system handle*/
    private ManagingSystemUUV2 managingSystem;
    
    /** new UUV configuration*/
    private int[] newConfiguration;
    
    /** Signal(s)*/
    int onSensor, offSensor, changeSpeed, planExecuted, noPlanningRequired, noAnalysisRequired;

    
    public Effector2(ActivFORMSEngine engine, ManagingSystemUUV2 managingSystem){
    	//assign handles
    	this.engine 		= engine;
		this.managingSystem	= managingSystem;
	
		//get signals
		onSensor 				= engine.getChannel("onSensor");
		offSensor 				= engine.getChannel("offSensor");
		changeSpeed 			= engine.getChannel("changeSpeed");
		planExecuted	= engine.getChannel("planExecuted");
		noPlanningRequired		= engine.getChannel("noPlanningRequired");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
		
		//register signals
		engine.register(onSensor, this, "sensorId", "currentConfiguration");
		engine.register(offSensor, this, "sensorId", "currentConfiguration");
		engine.register(changeSpeed, this, "newSpeed", "currentConfiguration");
		engine.register(planExecuted, this, "currentConfiguration");
		engine.register(noPlanningRequired, this, "currentConfiguration");
		engine.register(noAnalysisRequired, this, "currentConfiguration");
    }


int oldSpeed;
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
		    if (newSpeed != oldSpeed){
			oldSpeed = newSpeed;
		    }
		    else{
			System.err.println("same speed:" + newSpeed);
		    }
		    System.out.println("\t Speed: " + newSpeed);
		}
		else if (channelId == planExecuted){
			managingSystem.returnResult(newConfiguration);
		}
		else if (channelId == noPlanningRequired || channelId == noAnalysisRequired){
		    System.out.println("\t No change");
			managingSystem.returnResult(newConfiguration);	
		}
    }
    
    
    public void setNewConfigurationArray(int[] newConfiguration){
    	this.newConfiguration = newConfiguration;
    }    

}
