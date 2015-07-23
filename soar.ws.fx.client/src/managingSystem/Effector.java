package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import main.ManagingSystem;

public class Effector implements Synchronizer{

//    private Sensor[] sensors;
	private int[] newConfiguration;
    private ManagingSystem managingSystem;
    private ActivFORMSEngine engine;
    
    // Signals
    int changeService, allPlanStepsExecuted, noPlanningNeeded, noAnalysisRequired;
    
    public Effector(ActivFORMSEngine engine, ManagingSystem managingSystem){
		this.engine 		= engine;
		this.managingSystem	= managingSystem;
	
		changeService 			= engine.getChannel("changeService");
		allPlanStepsExecuted	= engine.getChannel("allPlanStepsExecuted");
		noPlanningNeeded		= engine.getChannel("noPlanningNeeded");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
		
		this.engine.register(changeService, this, "serviceType", "serviceId", "sConfig");
		this.engine.register(allPlanStepsExecuted, this, "newConfig", "sConfig");
		this.engine.register(noPlanningNeeded, this, "sConfig");
		this.engine.register(noAnalysisRequired, this, "sConfig");
    }

    
    @Override
    public boolean readyToReceive(int arg0) {
//    	System.out.println("Effector.readyToreceive()");
    	return true;
    }

    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
//    	System.out.println("Effector.receive()");
		System.out.println(data.get("sConfig"));
		if (channelId == changeService){
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");
//		    mainX.setSpeed(newSpeed);
		    // Service should be changed here
		}
		else if (channelId == allPlanStepsExecuted){
//			System.out.println("All Plan Steps Executed");
//			int datad = (int) data.get("allDone");
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					// TODO Auto-generated method stub
					managingSystem.returnResult(newConfiguration);	
//				}
//			}).start();;
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
//			System.out.println("No Planning Needed");
			managingSystem.returnResult(newConfiguration);	
			
		}
		
    }
    

    
    @Override
    public void accepted(int arg0) {
	
    }

}
