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
    int changeService, allPlanStepsExecuted, noPlanningNeeded, noAnalysisRequired;

    
    /**
    * Constructor: create a new effector instance
	**/
    public Effector(ActivFORMSEngine engine, ManagingSystem managingSystem){
    	//assign handles
		this.engine 		= engine;
		this.managingSystem	= managingSystem;
		
		this.newConfiguration = new int[this.managingSystem.NUM_OF_OPERATIONS];
	
		//get signals
		changeService 			= engine.getChannel("changeService");
		allPlanStepsExecuted	= engine.getChannel("allPlanStepsExecuted");
		noPlanningNeeded		= engine.getChannel("noPlanningNeeded");
		noAnalysisRequired		= engine.getChannel("noAnalysisRequired");
		
		//register the signals
		this.engine.register(changeService, this, "serviceType", "serviceId", "sConfig");
		this.engine.register(allPlanStepsExecuted, this, "newConfig", "sConfig");
		this.engine.register(noPlanningNeeded, this, "sConfig");
		this.engine.register(noAnalysisRequired, this, "sConfig");
    }

    
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	System.out.println("\tEffector.receive()");
		if (channelId == changeService){
			System.out.println("\t\tChangeService:\t" + data.get("sConfig"));
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");

		    // Service should be changed here
		    newConfiguration[serviceType-1] = serviceId-1;
		}
		else if (channelId == allPlanStepsExecuted){
			System.out.println("\t\tAll Plan Steps Executed");
			managingSystem.returnResult(newConfiguration);	
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
			System.out.println("\tNo Planning Needed");
			managingSystem.returnResult(newConfiguration);	
		}
		
    }
    
        
}
