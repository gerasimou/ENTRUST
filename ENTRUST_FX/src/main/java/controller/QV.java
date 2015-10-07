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
import controller.Knowledge.ServiceCharacteristics;
import fx.AbstractServiceClient;
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

    /** output file */
    String fileName;

    /** Structure that keeps the result after RQV (i.e., reliability, cost, and response time) */
    private RQVResult RQVResultsArray[];    
    
    /** System characteristics*/
    private final int NUM_OF_OPERATIONS;
    private final int NUM_OF_SERVICES;	
    private final int NUM_OF_CONFIGURATIONS;

    
    
    /**
     * Constructor
     */
    public QV(){
		//init system characteristics 
    	this.NUM_OF_OPERATIONS		= ENTRUST.NUM_OF_OPERATIONS;
 	    this.NUM_OF_SERVICES		= ENTRUST.NUM_OF_SERVICES;
 	    this.NUM_OF_CONFIGURATIONS	= (int)Math.pow(NUM_OF_SERVICES, NUM_OF_OPERATIONS);

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
		    
			//init structure for storing QV results
			this.RQVResultsArray = new RQVResult[NUM_OF_CONFIGURATIONS];
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
		
    	int index = 0;

    	//get the characteristics of services from the knowledge
    	List<List<AbstractServiceClient>> operationsList = Knowledge.getOperationsList();
    	
    	for (int indexList0=0; indexList0<operationsList.get(0).size(); indexList0++){
    		AbstractServiceClient srv0 = operationsList.get(0).get(indexList0);

    		for (int indexList1=0; indexList1<operationsList.get(1).size(); indexList1++){
    			AbstractServiceClient srv1  = operationsList.get(1).get(indexList1);
    			
        		for (int indexList2=0; indexList2<operationsList.get(2).size(); indexList2++){
        			AbstractServiceClient srv2 = operationsList.get(2).get(indexList2); 
        			
            		for (int indexList3=0; indexList3 < operationsList.get(3).size(); indexList3++){
            			AbstractServiceClient srv3 = operationsList.get(3).get(indexList3);
            					
                		for (int indexList4=0; indexList4<operationsList.get(4).size(); indexList4++){
                			AbstractServiceClient srv4 = operationsList.get(4).get(indexList4);

                			for (int indexList5=0; indexList5 < operationsList.get(5).size(); indexList5++){
                				AbstractServiceClient srv5  = operationsList.get(5).get(indexList5);
                				
                				double[][] arguments = new double[][]{srv0.getFeatures(), srv1.getFeatures(), srv2.getFeatures(),
									srv3.getFeatures(), srv4.getFeatures(), srv5.getFeatures()};
				
								
								
								//1) Instantiate parametric stochastic model								
								String modelString = realiseProbabilisticModel(arguments);
								
								//2) load PRISM model
								prism.loadModel(modelString);
	
								//3) run PRISM
								List<Double> prismResult 	= prism.runPrism();
								double req0result 			= prismResult.get(0);
								double req1result 			= prismResult.get(1);
								double req2result 			= prismResult.get(2);
				
								//4) store configuration results
                				
                				RQVResultsArray[index++] = new RQVResult(new int[]{indexList0, indexList1, indexList2, indexList3, indexList4, indexList5}, 
                														req0result, req1result, req2result);
                			}//for op6                			
                		}//for op5    			            			
            		}//for op6    			
        		}//for op3    			
    		}//for op2
    	}//for op1
		
		//return RQVResult for all configurations
		return RQVResultsArray;
		
	}

	
	
    /**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double[][] parameters){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");

    	//process the given parameters
    	for (int index=0; index<parameters.length; index++){
    		int serviceindex = index+1;
    		model.append("const double op"+ serviceindex + "Fail = " + (1-(double)parameters[index][0]) +";\n");
    		model.append("const double op"+ serviceindex + "Cost = " + (double)parameters[index][1] +";\n");
    		model.append("const double op"+ serviceindex + "Time = " + (double)parameters[index][2] +";\n\n");;
    	}    	
    	
    	return model.toString();
    }    
 
}
