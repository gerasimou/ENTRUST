package main;

import java.util.List;

import auxiliary.Utility;
import prism.PrismAPI;

/**
 * This is a dummy class used for the evaluation of the overheads
 * for runtime verification of adaptation requirements (RQV)
 * @author sgerasimou
 *
 */
public class MainEvaluator {
	private static String  modelFilename 		;//	= Utility.getProperty("MODEL_FILE");
	private static String  propertiesFilename	;//	= Utility.getProperty("PROPERTIES_FILE");
    public  static int NUM_OF_OPERATIONS 		;//	= Integer.parseInt(Utility.getProperty("NUM_OF_OPERATIONS"));
    public static int NUM_OF_SERVICES			;//	= Integer.parseInt(Utility.getProperty("NUM_OF_SERVICES"));	
	
    private static String parameters[][][];// = new String[NUM_OF_OPERATIONS][NUM_OF_SERVICES][3];

	
	public static void main(String[] args){
		if (args.length>1)
			throw new IllegalArgumentException(String.format("Only configuration file required as input. You passed %s arguments", args.length));
		Utility.readConfigFile(args[0]);
		
		modelFilename 		= Utility.getProperty("MODEL_FILE");
		propertiesFilename	= Utility.getProperty("PROPERTIES_FILE");
	    NUM_OF_OPERATIONS 	= Integer.parseInt(Utility.getProperty("NUM_OF_OPERATIONS"));
	    NUM_OF_SERVICES		= Integer.parseInt(Utility.getProperty("NUM_OF_SERVICES"));	
	    parameters			= new String[NUM_OF_OPERATIONS][NUM_OF_SERVICES][3];
		
		System.out.println("MainEvaluator");
		
		populateData();
				
		Analyser analyser = new Analyser(modelFilename, propertiesFilename);
//		analyser.doAnalysis(parameters);
		runFXscenario(analyser);
	}

	
	private static void populateData(){
	    //populate data
		String input[];
	    for (int i=0; i<NUM_OF_SERVICES; i++){
	    	input 				= Utility.getProperty("SRV_MARKET_WATCH_"+(i+1)).trim().split(",");
	    	parameters[0][i][0]	= input[1]; parameters[0][i][1]	= input[2]; parameters[0][i][2]	= input[3];
	    	input 				= Utility.getProperty("SRV_TECHNICAL_ANALYSIS_"+(i+1)).trim().split(",");
	    	parameters[1][i][0]	= input[1]; parameters[1][i][1]	= input[2]; parameters[1][i][2]	= input[3];
	    	input 				= Utility.getProperty("SRV_FUNDAMENTAL_ANALYSIS_"+(i+1)).trim().split(",");
	    	parameters[2][i][0]	= input[1]; parameters[2][i][1]	= input[2]; parameters[2][i][2]	= input[3];
	    	input 				= Utility.getProperty("SRV_ALARM_"+(i+1)).trim().split(",");
	    	parameters[3][i][0]	= input[1]; parameters[3][i][1]	= input[2]; parameters[3][i][2]	= input[3];
	    	input 				= Utility.getProperty("SRV_ORDER_"+(i+1)).trim().split(",");
	    	parameters[4][i][0]	= input[1]; parameters[4][i][1]	= input[2]; parameters[4][i][2]	= input[3];
	    	input 				= Utility.getProperty("SRV_NOTIFICATION_"+(i+1)).trim().split(",");
	    	parameters[5][i][0]	= input[1]; parameters[5][i][1]	= input[2]; parameters[5][i][2]	= input[3];
	    }	
	}
	
	
	private static void runFXscenario(Analyser analyser){
		String event		 = "E-1";
		
		//Scenario
		//E1:Normal
		event = "E1";
		System.out.println("E1:Normal");
		analyser.doAnalysis(parameters);
		
		//E2:MW1.reliability(0.9);
		event = "E2";
		System.out.println("E2:MW1.reliability(0.9)");
		parameters[0][0][0]="0.9";
		analyser.doAnalysis(parameters);
		
		//E3:FA2.responseTime(1.2);
		event = "E3";
		System.out.println("E3:FA2.responseTime(1.2)");
		parameters[2][1][2]="1.2";
		analyser.doAnalysis(parameters);
		
		//E4:MW1.reliability(0.976);
		event = "E4";
		System.out.println("E4:MW1.reliability(0.976)");
		parameters[0][0][0]="0.976";
		analyser.doAnalysis(parameters);
		
		//E5:TA1.reliability(0.985) & NOT1.time(1.0");
		event = "E5";
		System.out.println("E5:TA1.reliability(0.985) & NOT1.time(1.0");
		parameters[1][0][0]="0.985";
		parameters[5][0][2]="1";
		analyser.doAnalysis(parameters);

		//E6:OR1.reliability(0.95) & TA1.reliability(0.998)
		event = "E6";
		System.out.println("E6:OR1.reliability(0.95) & TA1.reliability(0.998)") ;
		parameters[4][0][0]="0.9";
		parameters[1][0][0]="0.998";
		analyser.doAnalysis(parameters);
		
		//E7:Normal
		event = "E7";
		System.out.println("E7:Normal {FA2.time(0.7), NOT1.time(1.8), OR1.reliability(0.995)}") ;
		parameters[2][1][2]="0.7";
		parameters[5][0][2]="1.8";
		parameters[4][0][0]="0.995";
		analyser.doAnalysis(parameters);
	}
}




class Analyser{
	private PrismAPI prism 			;
	private String  modelAsString 	;

	public Analyser(String modelFilename, String propertiesFilename){
		prism = new PrismAPI();
		modelAsString = Utility.readFile(modelFilename);
		prism.setPropertiesFile(propertiesFilename);
	}
	
	
	protected void doAnalysis(String[][][] parameters){
		final int SERVICES = parameters[0].length;
		for (int s1=0; s1<SERVICES; s1++){
			for (int s2=0; s2<SERVICES; s2++){
				for (int s3=0; s3<SERVICES; s3++){
					for (int s4=0; s4<SERVICES; s4++){
						for (int s5=0; s5<SERVICES; s5++){
							for (int s6=0; s6<SERVICES; s6++){

								//Generate a correct PRISM model									
								String modelString = realiseProbabilisticModel(new String[][]{parameters[0][s1], parameters[1][s2], 
																							  parameters[2][s3], parameters[3][s4], 
																							  parameters[4][s5], parameters[5][s6]});
								
								//load the PRISM model
								prism.loadModel(modelString);

								//run PRISM
								List<Double> prismResult = prism.runPrism();
								double req1result = prismResult.get(0);
								double req2result = prismResult.get(1);
								double req3result = prismResult.get(2);
								System.out.println(s1 +" "+ s2 +" "+ s3 +" "+s4 +" "+s5+" "+s6 +"\t"+ req1result +"\t"+ req2result +"\t"+ req3result);
							}//s6
						}//s5
					}//s4
				}//s3
			}//s2
		}//s1			
	}
    
	
    /**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(String[][] parameters){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");

    	//process the given parameters
    	for (int index=0; index<parameters.length; index++){
    		int serviceindex = index+1;
    		model.append("const double op"+ serviceindex + "Fail = " + (1-Double.parseDouble(parameters[index][0])) +";\n");
    		model.append("const double op"+ serviceindex + "Cost = " + parameters[index][1] +";\n");
    		model.append("const double op"+ serviceindex + "Time = " + parameters[index][2] +";\n\n");;
    	}    	
    	
    	return model.toString();
    }    
}
