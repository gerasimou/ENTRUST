package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import main.ManagingSystem;

public class Effector extends Synchronizer{

	private int[] newConfiguration;
    private ManagingSystem managingSystem;
    private ActivFORMSEngine engine;
    
    // Signals
    int changeService, allPlanStepsExecuted, noPlanningNeeded, noAnalysisRequired;
    
    public Effector(ActivFORMSEngine engine, ManagingSystem managingSystem){
		this.engine 		= engine;
		this.managingSystem	= managingSystem;
		
		this.newConfiguration = new int[this.managingSystem.NUM_OF_OPERATIONS];
	
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
    public void receive(int channelId, HashMap<String, Object> data) {
//    	System.out.println("Effector.receive()");
		if (channelId == changeService){
			System.out.println("ChangeService:\t" + data.get("sConfig"));
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");

		    // Service should be changed here
		    newConfiguration[serviceType-1] = serviceId-1;
		}
		else if (channelId == allPlanStepsExecuted){
			System.out.println("All Plan Steps Executed");
			managingSystem.returnResult(newConfiguration);	
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
			System.out.println("No Planning Needed");
			managingSystem.returnResult(newConfiguration);	
			
		}
		
    }
    
        
}
