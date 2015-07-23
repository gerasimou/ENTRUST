//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- Helper class that instantiates PrismWrapper class and invokes the appropriate methods	//
//	  to quantify the property of interest.				                                    //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//
package managingSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import auxiliary.Utility;
import prism.PrismAPI;
import prism.PrismWrapper;



public class Analyser {
	
	/** PRISM instance */
	PrismAPI prism;
	
	/** Name of model file */
	String modelFileName;
	
	/** Name of properties file */
	String propertiesFileName;
	
	/** Model string*/
	String modelAsString;
	
	/** System QoS requirement(s) */
    private final double R1;

    /** System characteristics*/
    private final int NUM_OF_OPERATIONS;
    private final int NUM_OF_SERVICES;	
    private final int NUM_OF_CONFIGURATIONS;
    
    /** Structure that keeps the result after RQV (i.e., reliability, cost, and response time) */
    private RQVResult RQVResultsArray[];
    
    /** ouput file */
    private String fileName;

    
    
    /**
     * Constructor
     */
    public Analyser(){		
		//Read  model and properties parameters
		this.modelFileName 		= Utility.getProperty("MODEL_FILE");
		this.propertiesFileName	= Utility.getProperty("PROPERTIES_FILE");

		//initialise PRISM instance
		this.prism = new PrismAPI();
		prism.setPropertiesFile(propertiesFileName);

		//Read the model
		this.modelAsString = Utility.readFile(modelFileName);		

		//load system QoS requirements
		this.R1 = Double.parseDouble(Utility.getProperty("RELIABILITY_THRESHOLD"));
		
		//init the output file
		this.fileName = Utility.getProperty("RQV_OUTPUT_FILE");
		
		//init system characteristics
	    //TODO initialised programmatically
	    this.NUM_OF_OPERATIONS		= 6;
	    this.NUM_OF_SERVICES		= 2;
	    this.NUM_OF_CONFIGURATIONS	= (int)Math.pow(NUM_OF_SERVICES, NUM_OF_OPERATIONS);
	    
	    //init structure for storing configuration results
	    this.RQVResultsArray = new RQVResult [NUM_OF_CONFIGURATIONS];
    }	
	
	
    
    
	public RQVResult[] doAnalysis(double r1, double r2, double r3, int PSC){
		double req1result;
		double req2result;
		int index;

		for (int CSC=1; CSC<8; CSC++){
			for (int s=20; s<=40; s++){
				
				index = ((CSC-1)*21)+(s-20);

				double p1 	= 0;//estimateP(s/10.0, 3.5, 1.5,95);
				double p2 	= 0;//estimateP(s/10.0, 3.0, 2.0,90);
				double p3 	= 0;//estimateP(s/10.0, 2.5, 2.5,85);
				
				req1result = Double.parseDouble(prismWrapper.runPrism(r1, r2, r3, p1, p2, p3, PSC, CSC, s/10.0, 10, 0));
				req2result = Double.parseDouble(prismWrapper.runPrism(r1, r2, r3, p1, p2, p3, PSC, CSC, s/10.0, 10, 1));

					RQVResultsArray[index] = new RQVResult(CSC, s, req1result, req2result);
			}
		}

		return RQVResultsArray;
	}
	
	
	public int getNumOfOperations(){
		return this.NUM_OF_OPERATIONS;
	}
	
	public int getNumOfServicesPerOperation(){
		return this.NUM_OF_OPERATIONS;
	}
}
