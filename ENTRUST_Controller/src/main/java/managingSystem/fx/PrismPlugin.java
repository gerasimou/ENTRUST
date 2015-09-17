package managingSystem.fx;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;

public class PrismPlugin extends Synchronizer{
    
	/** Analyser handle*/
    private Analyser analyser;
    
    /** Engine handle*/
    private ActivFORMSEngine engine;

    /** Communication signals*/
    private int startRQV, finishRQV;

    
    
    /**
     * Constructor: Create a PrismPlugin instance
     * @param engine
     */
    public PrismPlugin(ActivFORMSEngine engine){
    	//new analyser instance
		this.analyser 	= new Analyser();
		
		//assign engine instance
		this.engine 	= engine;
		
		//and get channels for incoming and outgoing signals
		this.startRQV = engine.getChannel("startRQV");
		this.finishRQV = engine.getChannel("finishRQV");
		
		//register receiving signal
		this.engine.register(startRQV, this, "avgFRates", "currentConfig", "&RQVResultsArray");		
    }
  
    
    /**
     * When invoked, carry out QV
     */
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {    	
    	
		if (channelId == startRQV){
		    HashMap<Integer, HashMap<Integer,Integer>> avgFRates = (HashMap<Integer, HashMap<Integer,Integer>>)data.get("avgFRates");
	    	System.out.print("\tPrismPlugin.receive()\t");
	    	System.out.println("\t\t" + avgFRates);

		    int servicesPerOperation    = analyser.getNumOfServicesPerOperation();
		    int[][] servicesReliability = new int[analyser.getNumOfOperations()][servicesPerOperation];
		    int row=-1;
		    for (int i=0; i<avgFRates.size(); i++){
		    	
		    	HashMap<Integer, Integer> operationRates = avgFRates.get(i+1);
		    	for (int serviceIndex=0; serviceIndex<operationRates.size(); serviceIndex++){
			    	if (serviceIndex%servicesPerOperation==0)
			    		row++;
			    	servicesReliability[row][serviceIndex%servicesPerOperation] = operationRates.get(serviceIndex+1);		    		
		    	}//for
		    	
		    }//for

		    RQVResult [] results = analyser.runQV(servicesReliability);
		    
		    HashMap<Integer, HashMap<String, Object>> RQVResultsArray = (HashMap<Integer, HashMap<String, Object>>)data.get("&RQVResultsArray");
		    HashMap<String, Object> RQV;
		    for(int index = 0; index < results.length; index++){
				RQV = RQVResultsArray.get(index);
				int[] configuration = results[index].getsConfig();
				for (int operationIndex=0; operationIndex<configuration.length; operationIndex++){
					int config = configuration[operationIndex];
					
					HashMap<Integer, UppaalType> sconfig = (HashMap<Integer, UppaalType>)RQV.get("sConfig");
					UppaalType type = sconfig.get(operationIndex+1);
					type.setValue(config+1);					
//					((UppaalType)(((HashMap<Integer, UppaalType>)RQV.get("sConfig"))).get(operationIndex+1)).setValue(config);					
				}
				((UppaalType)RQV.get("RQV1Result")).setValue(results[index].getReq1Result());
				((UppaalType)RQV.get("RQV2Result")).setValue(results[index].getReq2Result());
				((UppaalType)RQV.get("RQV3Result")).setValue(results[index].getReq3Result());
		    }
//	
		    
		    //return output to ActivFORMS
//		    System.out.println("Sending RQV results");
		    engine.send(finishRQV, this);
		}
    }    
//    
//	private void export(RQVResult[] results) {
//		StringBuilder output = new StringBuilder();
//		for (RQVResult result : results) {
//			int s1 = result.sensor1;
//			int s2 = result.sensor2;
//			int s3 = result.sensor3;
//			int speed = result.speed;
//			int r1 = result.req1Result;
//			int r2 = result.req2Result;
//			String str = "{{" + s1 + "," + s2 + "," + s3 + "}," + speed + "," + r1 + "," + r2 + "},\n";
//			output.append(str);
//		}
//		Utility.exportToFile("data.txt", output.toString(), false);
//	}
}
