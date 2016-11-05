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
package managingSystem.uuv;

import java.util.List;

import auxiliary.Utility;
import managedSystem.uuv.UUV;
import prism.PrismAPI;



public class QV {
	
	/** PRISM instance */
	PrismAPI prism;

	/** Name of model file */
	String modelFileName;

	/** Name of properties file */
	String propertiesFileName;
	
	/** Model string*/
	String modelAsString;

	/** System QoS requirement(s) */
//    private final double R1;
//    private final double R2;

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
    public QV(){
		//Read  model and properties parameters
		this.modelFileName 		= Utility.getProperty("MODEL_FILE");
		this.propertiesFileName	= Utility.getProperty("PROPERTIES_FILE");

		//initialise PRISM instance
		this.prism = new PrismAPI();
		prism.setPropertiesFile(propertiesFileName);

		//Read the model
		this.modelAsString = Utility.readFile(modelFileName);		

		//load system QoS requirements
//		this.R1 = Double.parseDouble(Utility.getProperty("R1"));
//		this.R2 = Double.parseDouble(Utility.getProperty("R2"));

		//init the output file
		this.fileName = Utility.getProperty("RQV_OUTPUT_FILE");

		//init system characteristics
	    NUM_OF_SENSORS			= ManagingSystemUUV.NUM_OF_SENSORS;
	    NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)-1); //possible sensor configurations
	    NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	    NUM_OF_CONFIGURATIONS	= NUM_OF_SENSOR_CONFIGS * NUM_OF_SPEED_CONFIGS;

	    //init structure for storing configuration results
	    this.RQVResultsArray = new RQVResult [NUM_OF_CONFIGURATIONS];

    }	
	
	
    
    
    
    
	public RQVResult[] doAnalysis(double r1, double r2, double r3
//									, int PSC){
//									, double r4, int PSC){
//									, double r4, double r5, int PSC){						
									, double r4, double r5, double r6, int PSC){
									//, double r4, double r5, double r6, double r7, double r8, int PSC){
		
		for (int CSC=1; CSC<ManagingSystemUUV.NUM_OF_SENSOR_COMBINATIONS; CSC++){
			for (int s=20; s<=40; s++){
				
				int index 	= ((CSC-1)*21)+(s-20);

				double p1 	= estimateP2(s/10.0, 5);
				double p2 	= estimateP2(s/10.0, 7);
				double p3 	= estimateP2(s/10.0, 11);
				double p4 	= estimateP2(s/10.0, 5);
				double p5 	= estimateP2(s/10.0, 7);
				double p6 	= estimateP2(s/10.0, 11);
//				double p7 	= estimateP2(s/10.0, 9);
//				double p8 	= estimateP2(s/10.0, 8);
//				double p1 	= estimateP(s/10.0, 3.5, 1.5,95);
//				double p2 	= estimateP(s/10.0, 3.0, 2.0,90);
//				double p3 	= estimateP(s/10.0, 2.5, 2.5,85);
				
				//Generate a correct PRISM model									
				String modelString = realiseProbabilisticModel(r1, r2, r3, r4, r5, r6,// r7, r8, 
															   p1, p2, p3, p4, p5, p6,// p7, p8, 
															   CSC, PSC, s/10.0);
   				                		
				//load the PRISM model
				prism.loadModel(modelString);

				//run PRISM
//				System.out.println(index);
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
    private String realiseProbabilisticModel(double r1, double r2, double r3, double r4, double r5, double r6, //double r7, double r8,
    										 double p1, double p2, double p3, double p4, double p5, double p6, //double p7, double p8,
    										 int CSC, int PSC, double speed){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");
		model.append("const double r1  = "+ r1 		+";\n");
		model.append("const double r2  = "+ r2 		+";\n");
		model.append("const double r3  = "+ r3 		+";\n");
		model.append("const double r4  = "+ r4 		+";\n");
		model.append("const double r5  = "+ r5 		+";\n");
		model.append("const double r6  = "+ r6 		+";\n");
//		model.append("const double r7  = "+ r7 		+";\n");
//		model.append("const double r8  = "+ r8 		+";\n");
		model.append("const double p1  = "+ p1 		+";\n");
		model.append("const double p2  = "+ p2 		+";\n");
		model.append("const double p3  = "+ p3 		+";\n");
		model.append("const double p4  = "+ p4 		+";\n");
		model.append("const double p5  = "+ p5 		+";\n");
		model.append("const double p6  = "+ p6 		+";\n");
//		model.append("const double p7  = "+ p7 		+";\n");
//		model.append("const double p8  = "+ p8 		+";\n");
		model.append("const int    CSC = "+ CSC 	+";\n");
		model.append("const int    PSC = "+ PSC 	+";\n");
		model.append("const double s   = "+ speed 	+";\n\n");

    	return model.toString();
    }
	
	private static double estimateP2(double speed, double alpha){
		return 100 - alpha * speed;
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
		
	
	
	@Deprecated
	public void plan(double r1, double r2, double r3){
    	RQVResult bestConfiguration = null; // Hint: the best configuration is the one with the lowest utility function
    	double    bestConfigurationResult = Integer.MAX_VALUE;
    	
    	int index 				= Integer.MIN_VALUE;
    	double functionResult	= Integer.MAX_VALUE;
    	
    	for (int sensorConfig=0; sensorConfig<NUM_OF_SENSOR_CONFIGS; sensorConfig++){
    		for (int speedConfig=0; speedConfig<NUM_OF_SPEED_CONFIGS; speedConfig++){
    			index = (sensorConfig*NUM_OF_SPEED_CONFIGS) + speedConfig;
    			if ( (RQVResultsArray[index].getReq1Result()>20) && (RQVResultsArray[index].getReq1Result()<100) ) { // (r1) & (r2) are satisfied
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
