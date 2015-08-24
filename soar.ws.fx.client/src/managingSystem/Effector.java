package managingSystem;

import java.util.Arrays;
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
		Arrays.fill(newConfiguration, -1);

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
		if (channelId == changeService){
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");
	    	System.out.println("\tEffector: Service " + serviceType +":"+ newConfiguration[serviceType-1] +"=>"+ (serviceId-1) );

		    // Service should be changed here
		    newConfiguration[serviceType-1] = serviceId-1;
		}
		else if (channelId == allPlanStepsExecuted){
			System.out.println("\tEffector: All Plan Steps Executed");
			managingSystem.returnResult(newConfiguration);
//			Arrays.fill(newConfiguration, -1);
		}
		else if (channelId == noPlanningNeeded || channelId == noAnalysisRequired){
			System.out.println("\tEffector: No Planning Needed");
			managingSystem.returnResult(null);	
//			Arrays.fill(newConfiguration, -1);
		}
		
    }    
        
}
