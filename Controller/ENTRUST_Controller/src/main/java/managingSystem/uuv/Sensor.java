package managingSystem.uuv;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Sensor extends Synchronizer {
	
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
	public Sensor(ActivFORMSEngine engine, ManagingSystemUUV managingSystem){
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
    public void sendAverageRates(double r1, double r2, double r3
//    							){
//    							, double r4){
//    							, double r4, double r5){
    							, double r4, double r5, double r6){
    							//, double r4, double r5, double r6, double r7, double r8) {
    	
//    	System.out.println("Probe.sendAverageRates()");

		String [] avgRates = new String[ManagingSystemUUV.NUM_OF_SENSORS];
		avgRates[0] = "avgRates[0]=" + (int)(r1*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[1] = "avgRates[1]=" + (int)(r2*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[2] = "avgRates[2]=" + (int)(r3*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[3] = "avgRates[3]=" + (int)(r4*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[4] = "avgRates[4]=" + (int)(r5*ManagingSystemUUV.MULTIPLIER_RATES);
		avgRates[5] = "avgRates[5]=" + (int)(r6*ManagingSystemUUV.MULTIPLIER_RATES);
//		avgRates[6] = "avgRates[6]=" + (int)(r7*ManagingSystemUUV.MULTIPLIER_RATES);
//		avgRates[7] = "avgRates[7]=" + (int)(r8*ManagingSystemUUV.MULTIPLIER_RATES);

		engine.send(setAverageMRates,this, avgRates);
    }
    
    
    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }

}
