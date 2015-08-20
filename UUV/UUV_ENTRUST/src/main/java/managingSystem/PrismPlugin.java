package managingSystem;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;
import mainX.MainX;
import prism.RQVResult;

public class PrismPlugin extends Synchronizer{
    
    /** ActivForms engine*/
	private ActivFORMSEngine engine;
    
    /** Analyser */
    private Analyser analyser;

    /** Signal(s)*/
	private int calculate_probability, receive_probability;
   
    
   /**
    * Constructor: create a new PrismPlugin instance 
    * @param engine
    */
    public PrismPlugin(ActivFORMSEngine engine){
    	//assign handles
		this.analyser = new Analyser();
		this.engine   = engine;
		
		//get signals
		calculate_probability = engine.getChannel("calculate_probability");
		receive_probability = engine.getChannel("receive_probability");

		//register signals
		engine.register(calculate_probability, this, "avgRates", "currentConfiguration", "&RQVResultsArray");		
    }

    
    
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
    	
		if (channelId == calculate_probability){
		    HashMap<Integer, Integer> avgRates = (HashMap<Integer, Integer>)data.get("avgRates");

		    double R1 = avgRates.get(0)/ManagingSystem.MULTIPLIER_RATES;
		    double R2 = avgRates.get(1)/ManagingSystem.MULTIPLIER_RATES;
		    double R3 = avgRates.get(2)/ManagingSystem.MULTIPLIER_RATES;
	
		    HashMap<Integer, HashMap> currentConfiguration = (HashMap<Integer, HashMap>)data.get("currentConfiguration");
		    String binaryString = "" + currentConfiguration.get("sensors").get(0) + currentConfiguration.get("sensors").get(1) + currentConfiguration.get("sensors").get(2);
		    int PSC = Integer.parseInt(binaryString, 2);
		    System.out.println("Running RQV -R1:" + R1 + " R2:" + R2 + " R3:" + R3 + " PSC:" + PSC);
		    RQVResult[] results = analyser.doAnalysis(R1, R2, R3, PSC);
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
		    
			plan(results);
		    engine.send(receive_probability, this);
		}
    }
    
    
    private void plan(RQVResult[] results){
    	int maxR1 = 0;
    	int maxR2 = 0;
    	for (RQVResult result :results){
    		if (result.getReq1Result() > maxR1){
    			maxR1 = result.getReq1Result();
    		}
    		if (result.getReq2Result() > maxR2){
    			maxR2 = result.getReq2Result();
    		}
    	}
//    	System.out.println(maxR1 +"\t"+ maxR2);
    	
    }

}
