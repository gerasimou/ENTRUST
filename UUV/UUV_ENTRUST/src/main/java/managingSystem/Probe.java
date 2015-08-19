package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import mainX.MainX;

public class Probe extends Synchronizer {
	
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Signal(s)*/
	private int setAverageMRates;

    /** MainX handle*/
	// private ManagingSystem managingSystems;
    
    
    /**
     * Constructor: create a new probe instance
     * @param engine
     * @param mainX
     */
	public Probe(ActivFORMSEngine engine, ManagingSystem managingSystem){
    	//assign handles
		this.engine = engine;
		//this.managingSystem	= managingSystem;

		//get signals
		setAverageMRates = engine.getChannel("setAverageMRates");
    }

    	
    /**
    * Send average rate to monitor through ActivFORMS
    * @param avgFRates
    */
    public void sendAverageRates(double r1, double r2, double r3) {
//    	System.out.println("Probe.sendAverageRates()");

		String [] avgRates = new String[3];
		avgRates[0] = "avgRates[0]=" + (int)(r1*ManagingSystem.MULTIPLIER_RATES);
		avgRates[1] = "avgRates[1]=" + (int)(r2*ManagingSystem.MULTIPLIER_RATES);
		avgRates[2] = "avgRates[2]=" + (int)(r3*ManagingSystem.MULTIPLIER_RATES);

		engine.send(setAverageMRates,this, avgRates);
    }
    
    
    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }

}
