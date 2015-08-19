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

import java.util.List;

import auxiliary.Utility;
import prism.PrismAPI;
import prism.RQVResult;



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
    private final double R2;

    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    private final int NUM_OF_SPEED_CONFIGS	; // [0,21], discrete steps
    private final int NUM_OF_CONFIGURATIONS ;

    /** ouput file */
    String fileName;

    /** Structure that keeps the result after RQV (i.e., reliability, cost, and response time) */
    private RQVResult RQVResultsArray[];    
    
    
    
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
		this.R1 = Double.parseDouble(Utility.getProperty("R1"));
		this.R2 = Double.parseDouble(Utility.getProperty("R2"));

		//init the output file
		this.fileName = Utility.getProperty("RQV_OUTPUT_FILE");

		//init system characteristics
	    NUM_OF_SENSORS			= 3;
	    NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)-1); //possible sensor configurations
	    NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	    NUM_OF_CONFIGURATIONS	= NUM_OF_SENSOR_CONFIGS * NUM_OF_SPEED_CONFIGS;

	    //init structure for storing configuration results
	    this.RQVResultsArray = new RQVResult [NUM_OF_CONFIGURATIONS];

    }	
	
	
    
    
    
    
	public RQVResult[] doAnalysis(double r1, double r2, double r3, int PSC){
		
		for (int CSC=1; CSC<8; CSC++){
			for (int s=20; s<=40; s++){
				
				int index 	= ((CSC-1)*21)+(s-20);

				double p1 	= estimateP(s/10.0, 3.5, 1.5,95);
				double p2 	= estimateP(s/10.0, 3.0, 2.0,90);
				double p3 	= estimateP(s/10.0, 2.5, 2.5,85);
				
				//Generate a correct PRISM model									
				String modelString = realiseProbabilisticModel(r1, r2, r3, p1, p2, p3, CSC, PSC, s/10.0);
   				                		
				//load the PRISM model
				prism.loadModel(modelString);

				//run PRISM
				List<Double> prismResult = prism.runPrism();
				double req1result = prismResult.get(0);
				double req2result = prismResult.get(1);
				
				RQVResultsArray[index] = new RQVResult(CSC, s/10.0, req1result, req2result);
			}
		}

		return RQVResultsArray;
	}
	
	
    /**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double r1, double r2, double r3, double p1, double p2, double p3,
    										 int CSC, int PSC, double speed){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");
		model.append("const double r1  = "+ r1 		+";\n");
		model.append("const double r2  = "+ r2 		+";\n");
		model.append("const double r3  = "+ r3 		+";\n");
		model.append("const double p1  = "+ p1 		+";\n");
		model.append("const double p2  = "+ p2 		+";\n");
		model.append("const double p3  = "+ p3 		+";\n");
		model.append("const int    CSC = "+ CSC 	+";\n");
		model.append("const int    PSC = "+ PSC 	+";\n");
		model.append("const double s   = "+ speed 	+";\n\n");

    	return model.toString();
    }
	
	
	
	private static double estimateP(double speed, double speedThreshold, double alpha, double beta){
		if (speed <= speedThreshold){
			return (100 - alpha * speed / speedThreshold);
		}
		else{
			double result;
			result = beta - 30 * (speed-speedThreshold) / (5.0);
			return (result < 0) ? 0 : result;
		}
	}
		
	
	
	@SuppressWarnings("unused")
	public void plan(double r1, double r2, double r3){
    	RQVResult bestConfiguration = null; // Hint: the best configuration is the one with the lowest utility function
    	double    bestConfigurationResult = Integer.MAX_VALUE;
    	
    	int index 				= Integer.MIN_VALUE;
    	double functionResult	= Integer.MAX_VALUE;
    	
    	for (int sensorConfig=0; sensorConfig<NUM_OF_SENSOR_CONFIGS; sensorConfig++){
    		for (int speedConfig=0; speedConfig<NUM_OF_SPEED_CONFIGS; speedConfig++){
    			index = (sensorConfig*NUM_OF_SPEED_CONFIGS) + speedConfig;
    			if ( (RQVResultsArray[index].getReq1Result()>R1) && (RQVResultsArray[index].getReq1Result()<R2) ) { // (r1) & (r2) are satisfied
    				functionResult = 5 * RQVResultsArray[index].getReq2Result() + 5/RQVResultsArray[index].getSpeed();
    				//increase the utility value for a configuration that includes a failed sensor    				
    				if (r1<=2.5 && (sensorConfig+1)%2>0) functionResult += 50;
    				if (r2<=2.5 && (sensorConfig+1)%4>1) functionResult += 50;
    				if (r3<=2.5 && (sensorConfig+1)%8>3) functionResult += 50;
    				
    				if (functionResult<bestConfigurationResult){// a new best configuration has been found
    					bestConfigurationResult = functionResult;
    					bestConfiguration		= RQVResultsArray[index];
    				}
    			}
    		}
    	}
 
    	System.out.println("Best Configuration \n ------------------\n" + bestConfiguration.toString());
    	System.out.println("Best result:\t" + bestConfigurationResult);
    	Utility.exportToFile(fileName, "\nBest Configuration \n ------------------\n" + bestConfiguration.toString(), true);
    	Utility.exportToFile(fileName, "Best result:\t" + bestConfigurationResult, true);
    }
	
}
