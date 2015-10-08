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

public class ENTRUST {

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
	public ENTRUST() {		
	    try {
			//initialise ActiveFORMS engine (MAPE virtual machine)
			this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9999);
		    this.engine.setRealTimeUnit(1000);

		    //init sensor
		    this.sensor = new Sensor(engine, this);
		    
		    //init effector
		    this.effector = new Effector(engine, this, sensor.getOutputStream());
		    
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
	 * Start the ENTRUST controller
	 */
	public void start(){
		//if communication with the managed system occurs through sockets, give the port number
		//TODO
		int portNumber = 56567;
		sensor.startListening(portNumber);
	}
	
	
    /**
     * Set output stream (after sensor is connected to the managed system)
     * @param out
     */
	protected void assignOutputStream(PrintWriter out){
		effector.assignOutputStream(out);
	}
}
