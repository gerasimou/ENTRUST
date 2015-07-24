package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import main.ManagingSystem;

public class Probe implements Synchronizer {
    
	/** ActivFORMS engine*/
    private ActivFORMSEngine engine;

    /** MainX handle*/
//    private ManagingSystem mainX;
    
    /** Signal(s)*/
    private int setAverageFRates;
    
    
    /**
     * Constructor: create a new probe instance
     * @param engine
     * @param mainX
     */
    public Probe(ActivFORMSEngine engine, ManagingSystem mainX){
//		this.mainX = mainX;
		this.engine = engine;
		setAverageFRates = engine.getChannel("setAverageFRates");
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
			
		engine.send(setAverageFRates, this, avgRates);
//		System.out.println("Measurement Taken SensorId=" + sensorId);
    }
    
    
    @Override
    public void accepted(int arg0) {
//    	System.out.println("Probe.accepted()");
    }

    @Override
    public boolean readyToReceive(int arg0) {
//    	System.out.println("Probe.readyToReceive()");
    	return false;
    }

    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }
}
