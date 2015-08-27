package managingSystem.uuv;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Probe2 extends Synchronizer {
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Signal(s)*/
	private int setAverageRates;

    /** MainX handle*/
	// private ManagingSystem managingSystems;
    
    
    /**
     * Constructor: create a new probe instance
     * @param engine
     * @param mainX
     */
	public Probe2(ActivFORMSEngine engine, ManagingSystemUUV2 managingSystem){
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
    public void sendAverageRates(double r1, double r2, double r3) {
//    	System.out.println("Probe.sendAverageRates()");

		String [] avgRates = new String[3];
		avgRates[0] = "avgRates[0]=" + (int)(r1*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[1] = "avgRates[1]=" + (int)(r2*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[2] = "avgRates[2]=" + (int)(r3*ManagingSystemUUV.MULTIPLIER_RATES);

		engine.send(setAverageRates,this, avgRates);
    }
    
    
    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }

}
