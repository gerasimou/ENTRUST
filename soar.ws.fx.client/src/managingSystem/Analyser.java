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
import fx.services.AbstractServiceClient;
import managingSystem.Knowledge.ServiceCharacteristics;
import prism.PrismAPI;



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
	
    
    
    
    public RQVResult[] runQV(int[][] servicesReliability){
	    System.out.println("\tAnalyser.runQV()");
    	int index = 0;
    	
    	//get the characteristics of services from the knowledge
    	List<List<ServiceCharacteristics>> operationCharacteristicsList = Knowledge.getKnowledge();
    	
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
                			        
                				double[][] srvFeatures = new double[][]{srv0.getFeatures(), srv1.getFeatures(), srv2.getFeatures(),
                														srv3.getFeatures(), srv4.getFeatures(), srv5.getFeatures()};
                				//Generate a correct PRISM model									
                				String modelString = realiseProbabilisticModel(srvFeatures);
                   				                		
                				//load the PRISM model
                				prism.loadModel(modelString);

                				//run PRISM
                				List<Double> prismResult = prism.runPrism();
                				
                				RQVResultsArray[index++] = new RQVResult(new int[]{indexList0, indexList1, indexList2, indexList3, indexList4, indexList5}, 
                														 prismResult.get(0), prismResult.get(1), prismResult.get(2));
                				
                				
//                				String srvsID = "[" + srv0.getID() +","+ srv1.getID() +","+ srv2.getID() +","+
//                									  srv3.getID() +","+ srv4.getID() +","+ srv5.getID() +"]\t";
                				
//                				System.out.println(index +"), "+ Arrays.toString(prismResult.toArray()));                				                				
                			}//for op6                			
                		}//for op5    			            			
            		}//for op6    			
        		}//for op3    			
    		}//for op2
    	}//for op1

    	return RQVResultsArray;
    }//runQV
    
    
    
    /**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double[][] srvFeatures){
    	StringBuilder model = new StringBuilder(modelAsString);
    	for (int index=0; index<srvFeatures.length; index++){
    		int serviceindex = index+1;
    		model.append("const double op"+ serviceindex + "Fail = " + (1-srvFeatures[index][0]) +";\n");
    		model.append("const double op"+ serviceindex + "Cost = " + srvFeatures[index][1] +";\n");
    		model.append("const double op"+ serviceindex + "Time = " + srvFeatures[index][2] +";\n\n");;
    	}    	
    	return model.toString();
    }
    
	
	public int getNumOfOperations(){
		return this.NUM_OF_OPERATIONS;
	}
	
	public int getNumOfServicesPerOperation(){
		return this.NUM_OF_SERVICES;
	}

}
