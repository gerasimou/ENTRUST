package managingSystem.fx;

import java.util.Arrays;
import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Effector extends Synchronizer{

	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** Managing system handle*/
    private ManagingSystemFX managingSystem;
    
    /** new UUV configuration*/
    private int[] newConfiguration;
    
    /** Signal(s)*/
    int changeService, planExecuted, noPlanningRequired, noAnalysisRequired;

    
    /**
    * Constructor: create a new effector instance
	**/
    public Effector(ActivFORMSEngine engine, ManagingSystemFX managingSystem){
    	//assign handles
		this.engine 		= engine;
		this.managingSystem	= managingSystem;
		
		this.newConfiguration = new int[this.managingSystem.NUM_OF_OPERATIONS];
		Arrays.fill(newConfiguration, -1);

		//get signals
		changeService 		= engine.getChannel("changeService");
		planExecuted		= engine.getChannel("planExecuted");
		noPlanningRequired	= engine.getChannel("noPlanningRequired");
		noAnalysisRequired	= engine.getChannel("noAnalysisRequired");
		
		//register the signals
//		this.engine.register(changeService, this, "serviceType", "serviceId", "sConfig");
		this.engine.register(changeService, this, "serviceType", "serviceId", "newConfig");
		this.engine.register(planExecuted, this, "newConfig", "currentConfig");
		this.engine.register(noPlanningRequired, this, "currentConfig");
		this.engine.register(noAnalysisRequired, this, "currentConfig");
    }

    
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
		if (channelId == changeService){
		    int serviceId = (Integer) data.get("serviceId");
		    int serviceType = (Integer) data.get("serviceType");
//	    	System.out.println("\tEffector: Service " + serviceType +":"+ newConfiguration[serviceType-1] +"=>"+ (serviceId-1) );

		    // Service should be changed here
		    newConfiguration[serviceType-1] = serviceId-1;
		}
		else if (channelId == planExecuted){
			System.out.println("\tEffector: All Plan Steps Executed\t" + data.toString());
			managingSystem.returnResult(newConfiguration);
//			Arrays.fill(newConfiguration, -1);
		}
		else if (channelId == noPlanningRequired || channelId == noAnalysisRequired){
			System.out.println("\tEffector: No Planning Needed\t" + data.toString());
			managingSystem.returnResult(null);	
//			Arrays.fill(newConfiguration, -1);
		}
		
    }    
        
}
