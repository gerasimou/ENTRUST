package managingSystem.fx;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;
import auxiliary.Utility;
import cern.colt.Arrays;
import main.MainENTRUST;

public class VerificationEngine extends Synchronizer{
    
	/** Analyser handle*/
    private QV qv;
    
    /** Engine handle*/
    private ActivFORMSEngine engine;

    /** Communication signals*/
    private int startRQV, finishRQV;

    
    
    /**
     * Constructor: Create a PrismPlugin instance
     * @param engine
     */
    public VerificationEngine(ActivFORMSEngine engine){
    	//new analyser instance
		this.qv 	= new QV();
		
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
//	    	System.out.print(this.getClass().getSimpleName() + ".receive()\t\t" + avgFRates);

		    int servicesPerOperation    = qv.getNumOfServicesPerOperation();
		    int[][] servicesReliability = new int[qv.getNumOfOperations()][servicesPerOperation];
		    int row=-1;
		    for (int i=0; i<avgFRates.size(); i++){
		    	
		    	HashMap<Integer, Integer> operationRates = avgFRates.get(i+1);
		    	for (int serviceIndex=0; serviceIndex<operationRates.size(); serviceIndex++){
			    	if (serviceIndex%servicesPerOperation==0)
			    		row++;
			    	servicesReliability[row][serviceIndex%servicesPerOperation] = operationRates.get(serviceIndex+1);		    		
		    	}//for
		    	
		    }//for

		    RQVResult [] results = qv.runQV(servicesReliability);
		    
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
		    export(results);
		    engine.send(finishRQV, this);
		}
    }    
//    
	private void export(RQVResult[] results) {
		StringBuilder output = new StringBuilder();
		for (RQVResult result : results) {
			int config[] = result.sConfig;
			int r1 = result.req1Result;
			int r2 = result.req2Result;
			int r3 = result.req3Result;
			String str = Arrays.toString(config) +","+ r1 + "," + r2 + "," + r3 +"\n"; 
			output.append(str);
		}
		Utility.exportToFile(MainENTRUST.event+".csv", output.toString(), false);
	}
}
