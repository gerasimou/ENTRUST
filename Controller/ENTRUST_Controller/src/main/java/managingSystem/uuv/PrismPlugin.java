package managingSystem.uuv;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;
import auxiliary.Utility;

public class PrismPlugin extends Synchronizer{
    
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Analyser */
    private Analyser analyser;

    /** Signal(s)*/
	private int startRQV, finishRQV;
   
    
   /**
    * Constructor: create a new PrismPlugin instance 
    * @param engine
    */
    public PrismPlugin(ActivFORMSEngine engine){
    	//assign handles
		this.analyser = new Analyser();
		this.engine   = engine;
		
		//get signals
		startRQV = engine.getChannel("calculate_probability");
		finishRQV = engine.getChannel("receive_probability");

		//register signals
		engine.register(startRQV, this, "avgRates", "currentConfiguration", "&RQVResultsArray");		
    }

    
    
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
//    	System.out.println("PrismPlugin.receive()");
		if (channelId == startRQV){
		    HashMap<Integer, Integer> avgRates = (HashMap<Integer, Integer>)data.get("avgRates");

		    double R1 = avgRates.get(0)/ManagingSystemUUV.MULTIPLIER_RATES;
		    double R2 = avgRates.get(1)/ManagingSystemUUV.MULTIPLIER_RATES;
		    double R3 = avgRates.get(2)/ManagingSystemUUV.MULTIPLIER_RATES;
		    double R4 = avgRates.get(3)/ManagingSystemUUV.MULTIPLIER_RATES;
		    double R5 = avgRates.get(4)/ManagingSystemUUV.MULTIPLIER_RATES;
		    double R6 = avgRates.get(5)/ManagingSystemUUV.MULTIPLIER_RATES;
//		    double R7 = avgRates.get(6)/ManagingSystemUUV.MULTIPLIER_RATES;
//		    double R8 = avgRates.get(7)/ManagingSystemUUV.MULTIPLIER_RATES;
	
		    HashMap<Integer, HashMap> currentConfiguration = (HashMap<Integer, HashMap>)data.get("currentConfiguration");
		    String binaryString = "" + currentConfiguration.get("sensors").get(0) + currentConfiguration.get("sensors").get(1) + currentConfiguration.get("sensors").get(2);
		    int PSC = Integer.parseInt(binaryString, 2);
		    System.out.println("Running RQV -R1:" + R1 + " R2:" + R2 + " R3:" + R3 + " PSC:" + PSC);
		    
		    //run QV
		    RQVResult[] results = analyser.doAnalysis(R1, R2, R3
//		    										, PSC);
//													, R4, PSC);
//		    										, R4, R5, PSC);
		    										, R4, R5, R6, PSC);
													//, R4, R5, R6, R7, R8, PSC);
		    
		    HashMap<Integer, HashMap<String, Object>> array = (HashMap<Integer, HashMap<String, Object>>)data.get("&RQVResultsArray");

		    HashMap<String, Object> RQV;
		    for(int i = 0; i < results.length; i++){
				RQV = array.get(i);
				((UppaalType)((HashMap)RQV.get("sensors")).get(0)).setValue(results[i].getSensor1());
				((UppaalType)((HashMap)RQV.get("sensors")).get(1)).setValue(results[i].getSensor2());
				((UppaalType)((HashMap)RQV.get("sensors")).get(2)).setValue(results[i].getSensor3());
				((UppaalType)RQV.get("speed")).setValue(results[i].getSpeed());
				((UppaalType)RQV.get("req1Result")).setValue(results[i].getReq1Result());
				((UppaalType)RQV.get("req2Result")).setValue(results[i].getReq2Result());
		    }
		    
//		    plan(results);
//		    export(results);
		    engine.send(finishRQV, this);
		}
    }
    
    
//    private void plan(RQVResult[] results){
//    	int maxR1 = 0;
//    	int maxR2 = 0;
//    	double bestCost = Double.MAX_VALUE;
//    	RQVResult bestResult = null;
//    	for (RQVResult result :results){
//    		int r1 =  result.getReq1Result();
//    		int r2 =  result.getReq2Result();
//    		if (r1>=2000 && r2<=12000 ){
//    			double cost = r2 + 10000.0/result.speed;
//    			if (cost < bestCost){
//    				bestCost = cost;
//    				bestResult = result;
//    			}
//    		}
//    		if (result.getReq1Result() > maxR1){
//    			maxR1 = result.getReq1Result();
//    		}
//    		if (result.getReq2Result() > maxR2){
//    			maxR2 = result.getReq2Result();
//    		}
//    	}
//    	System.out.println(maxR1 +"\t"+ maxR2 +"\t"+ bestResult.toString());
//    }
    
    
//    private void export(RQVResult[] results){
//    	StringBuilder output = new StringBuilder();
//    	for (RQVResult result :results){
//    		int s1 	= result.sensor1;
//    		int s2 	= result.sensor2;
//    		int s3 	= result.sensor3;
//       		int speed 	= result.speed;
//       		int r1		= result.req1Result;
//       		int r2		= result.req2Result;
//       		String str = "{{"+s1 +","+ s2 +"," + s3 +"},"+ speed +","+ r1 +","+ r2 +"},\n";
//       		output.append(str);
//    	}
//    	Utility.exportToFile("data.txt", output.toString(), false);
//    }

}
