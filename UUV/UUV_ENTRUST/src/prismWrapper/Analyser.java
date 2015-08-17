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
package prismWrapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class Analyser {
	//Given
    private final int R1					= 20;
    private final int R2					= 120;
    private final int NUM_OF_SENSORS		= 3;
    private final int NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)-1); //possible sensor configurations
    private final int NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	private final PrismWrapper prismWrapper;
	private final String modelFilename		= "input/uuv.sm";
	private final String propertiesFilename	= "input/uuv.csl";

    String fileName;

    //Structure that keeps the result after RQV (i.e., expected number of accurate measurements + expected power consumption)
    RQVResult RQVResultsArray[] = new RQVResult [NUM_OF_SENSOR_CONFIGS * NUM_OF_SPEED_CONFIGS];

    private BufferedWriter fileWriter 				;
    
    public Analyser(String filename){
		prismWrapper = new PrismWrapper(modelFilename, propertiesFilename);
		this.fileName = filename;
    }	
	
	
	public RQVResult[] doAnalysis(double r1, double r2, double r3, int PSC){
		double req1result;
		double req2result;
		int index;

		String str	= "{\n";	
    	str += "sensor1 rate: " + r1 +"\n";
    	str += "sensor2 rate: " + r2 +"\n";
    	str += "sensor3 rate: " + r3 +"\n";
    	str += "Previous Configuration: " + PSC +"\n\n";
		
		for (int CSC=1; CSC<8; CSC++){
			for (int s=20; s<=40; s++){
				
				index = ((CSC-1)*21)+(s-20);

				double p1 	= estimateP(s/10.0, 3.5, 1.5,95);
				double p2 	= estimateP(s/10.0, 3.0, 2.0,90);
				double p3 	= estimateP(s/10.0, 2.5, 2.5,85);
				
				req1result = Double.parseDouble(prismWrapper.runPrism(r1, r2, r3, p1, p2, p3, PSC, CSC, s/10.0, 10, 0));
				req2result = Double.parseDouble(prismWrapper.runPrism(r1, r2, r3, p1, p2, p3, PSC, CSC, s/10.0, 10, 1));

				str += "{"; 
				str += (CSC%2) +",";
				str += (CSC%4>1 ? 1 : 0) +",";
				str += (CSC%8>3 ? 1 : 0) +",";
				str += s +",";
				str += Math.round(req1result) +","; 
				str += Math.round(req2result) +"}";
				if (!(CSC==7&&s==40))
					str +=",\n";

				RQVResultsArray[index] = new RQVResult(CSC, s, req1result, req2result);
			}
		}
		str += "\n}";
//		System.out.println(str);
		writeToFile(fileName, str);

		return RQVResultsArray;
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
	
	
	public void plan(double r1, double r2, double r3){
    	RQVResult bestConfiguration = null; // Hint: the best configuration is the one with the lowest utility function
    	double    bestConfigurationResult = Integer.MAX_VALUE;
    	
    	int index 				= Integer.MIN_VALUE;
    	double functionResult	= Integer.MAX_VALUE;
    	
    	for (int sensorConfig=0; sensorConfig<NUM_OF_SENSOR_CONFIGS; sensorConfig++){
    		for (int speedConfig=0; speedConfig<NUM_OF_SPEED_CONFIGS; speedConfig++){
    			index = (sensorConfig*NUM_OF_SPEED_CONFIGS) + speedConfig;
    			if ( (RQVResultsArray[index].req1Result>R1) && (RQVResultsArray[index].req1Result<R2) ) { // (r1) & (r2) are satisfied
    				functionResult = 5 * RQVResultsArray[index].req2Result + 5/RQVResultsArray[index].speed;
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
    	writeToFile(fileName, "\nBest Configuration \n ------------------\n" + bestConfiguration.toString());
    	writeToFile(fileName, "Best result:\t" + bestConfigurationResult);
    }
	
	
	
	private boolean writeToFile(String filename, String output){
		try {
//			if (fileWriter==null){
				fileWriter = new BufferedWriter(new FileWriter(filename, true));
//			}
			fileWriter.write(output +"\n");
			fileWriter.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
}
