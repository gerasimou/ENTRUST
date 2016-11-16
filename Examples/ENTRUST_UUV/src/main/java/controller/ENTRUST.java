//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- ENTRUST controller main class						                                    //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//

package controller;

import java.io.PrintWriter;

import activforms.engine.ActivFORMSEngine;
import auxiliary.Utility;

public class ENTRUST implements Runnable{

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER 		= 100;
	public static final double MULTIPLIER_RATES = 1000;

    /** ActivForms engine*/
	private ActivFORMSEngine engine;

	/** Verification Engine*/
	private VerificationEngine verificationEngine;

	/** Sensor handle*/
	private Sensor sensor;
 
	/** Effector handle*/
	private Effector effector;


	/**
	 * Managing System constructor
	 */
	public ENTRUST(int portNumber) {		
	    try {
			//initialise ActiveFORMS engine (MAPE virtual machine)
			this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9999);
		    this.engine.setRealTimeUnit(1000);

		    //init sensor
		    this.sensor = new Sensor(engine, portNumber);
		    
		    //init effector
		    this.effector = new Effector(engine, sensor.getOutputStream());
		    
		    //init verification engine
		    this.verificationEngine = new VerificationEngine(engine);
		    
		    //start the MAPE virtual machine
		    engine.start();
	    } 
	    catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Run the ENTRUST controller
	 */
	public void run(){
		new Thread(new OutputWriterAssignment()).start();
		sensor.startListening();
	}
	
	
	class OutputWriterAssignment implements Runnable{
		@Override
		public void run() {
			PrintWriter out = null;
			
			try {
				do{
					out = sensor.getOutputStream();
					Thread.sleep(500);
				}
				while (out == null);
				effector.setOutputStream(out);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
