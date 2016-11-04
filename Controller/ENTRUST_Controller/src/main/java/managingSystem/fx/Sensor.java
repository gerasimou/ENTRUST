package managingSystem.fx;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Sensor extends Synchronizer {
    
	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** MainX handle*/
//    private ManagingSystem managingSystem;
    
    /** Signal(s)*/
    private int setAverageRates;
    
    
    /**
     * Constructor: create a new probe instance
     * @param engine
     * @param mainX
     */
    public Sensor(ActivFORMSEngine engine, ManagingSystemFX  managingSystem){
    	//assign handles
		this.engine = engine;
		//this.managingSystem	= managingSystem;

		//get signals
		setAverageRates = engine.getChannel("setAverageRates");
    }

    
    /**
     * Send average rate to monitor through ActivFORMS
     * @param avgFRates
     */
    public void sendAverageRates(int avgFRates[][], int numOfOperations, int numOfServicesPerOperation) {
	
		String [] avgRates = new String[numOfOperations * numOfServicesPerOperation];// 6x3
		int index = 0;
		for(int operation = 0; operation < numOfOperations; operation++){
		    for (int service = 0; service < numOfServicesPerOperation; service++){
			avgRates[index++] = "currentAvgFRates[" + (operation+1) + "][" + (service+1) + "]=" + avgFRates[operation][service];
		    }	
		}
			
		engine.send(setAverageRates, this, avgRates);
//		System.out.println("Measurement Taken SensorId=" + sensorId);
    }
    
    
    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }
}
