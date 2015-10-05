//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- Quantitative Verification class														// 
// 	  It uses PrismApi class and invokes the appropriate methods	//
//	  to quantify the property of interest.				                                    //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//==========================================================================================//
package controller;

import java.util.List;

import auxiliary.Utility;
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

    /** ouput file */
    String fileName;

    /** Structure that keeps the result after RQV (i.e., reliability, cost, and response time) */
    private RQVResult RQVResultsArray[];    
    
    /** System characteristics*/
    //TODO...

    
    
    /**
     * Constructor
     */
    public QV(){
		try{
			//Read  model and properties parameters
			this.modelFileName 		= Utility.getProperty("MODEL_FILE");
			this.propertiesFileName	= Utility.getProperty("PROPERTIES_FILE");

			//initialise PRISM instance
			this.prism = new PrismAPI();
			prism.setPropertiesFile(propertiesFileName);

		
			//Read the model
			this.modelAsString = Utility.readFile(modelFileName);		

			//init the output file
			this.fileName = Utility.getProperty("RQV_OUTPUT_FILE");
	
			//init system characteristics 
			//TODO... 
			
			//init structure for storing QV results
			//e.g. this.RQVResultsArray = new RQVResult[#configurations];
			this.RQVResultsArray = new RQVResult[1];
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

    }	
	
	
    
    
    
    /**
     * Run quantitative verification
     * @param parameters
     */
	public RQVResult[] runQV(Object ... parameters){
		//For all configurations run QV and populate RQVResultArray
		//TODO...
			
			//1) Instantiate parametric stochastic model								
			String modelString = realiseProbabilisticModel(parameters);
			
			//2) load PRISM model
			prism.loadModel(modelString);

			//3) run PRISM
			List<Double> prismResult 	= prism.runPrism();
			double req1result 			= prismResult.get(0);
			double req2result 			= prismResult.get(1);
			
			//4) store configuration results
			RQVResultsArray[0] = new RQVResult(req1result, req2result);
		
		//return RQVResult for all configurations
		return RQVResultsArray;
		
	}
	
	
	
    /**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(Object ... parameters){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");

    	//process the given parameters
    	//...
    	//e.g. model.append(parameters[0].toString());
    	
    	return model.toString();
    }    
}
