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
//    	System.out.println("Effector.receive()");
		System.out.println(data.get("currentConfiguration"));
		if (channelId == onSensor){
		    int sensorId = (Integer) data.get("sensorId");
//		    sensors[sensorId].setStatus(true);
		    newConfiguration[sensorId] = 1;
		}
		else if (channelId == offSensor){
		    int sensorId = (Integer) data.get("sensorId");
//		    sensors[sensorId].setStatus(false);
		    newConfiguration[sensorId] = 0;
		}
		else if (channelId == changeSpeed){
		    int newSpeed = (Integer) data.get("newSpeed");
//		    mainX.setSpeed(newSpeed);
		    newConfiguration[3] = newSpeed;
		}
		else if (channelId == allPlanStepsExecuted){
//			System.out.println("All Plan Steps Executed");
//			int datad = (int) data.get("allDone");
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					managingSystem.returnResult(newConfiguration);	
//				}
//			}).start();;
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
//			System.out.println("No Planning Needed");
			managingSystem.returnResult(newConfiguration);	
		}
    }
    
//    public void setSensors(Sensor[] sensors) {
//    	System.out.println("Effector.setSensors()");
//    	this.sensors = sensors;
//    }
    
    public void setNewConfigurationArray(int[] newConfiguration){
    	this.newConfiguration = newConfiguration;
    }    

}
