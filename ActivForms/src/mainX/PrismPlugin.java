package mainX;

import java.util.HashMap;

import prismWrapper.Analyser;
import prismWrapper.RQVResult;
import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;

public class PrismPlugin implements Synchronizer{

    static String fileName	= "data/dataFile.txt";
    
    Analyser analyser;
    private int calculate_probability, receive_probability;
    private ActivFORMSEngine engine;

    
    public PrismPlugin(ActivFORMSEngine engine){
	
		analyser = new Analyser(fileName);
		this.engine = engine;
		
		calculate_probability = engine.getChannel("calculate_probability");
		engine.register(calculate_probability, this, "avgRates", "currentConfiguration", "&RQVResultsArray");
		
		receive_probability = engine.getChannel("receive_probability");
    }

    
    @Override
    public void accepted(int arg0) {
	if (arg0 == receive_probability){}
//	    System.out.println("Probability accepted");
    }

    
    @Override
    public boolean readyToReceive(int arg0) {
    	return true;
    }
    
    
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
		if (channelId == calculate_probability){
		    HashMap<Integer, Integer> avgRates = (HashMap<Integer, Integer>)data.get("avgRates");
//		    System.out.println(avgRates);
		    double R1 = avgRates.get(0)/MainX.MULTIPLIER;
		    double R2 = avgRates.get(1)/MainX.MULTIPLIER;
		    double R3 = avgRates.get(2)/MainX.MULTIPLIER;
	
		    HashMap<Integer, HashMap> currentConfiguration = (HashMap<Integer, HashMap>)data.get("currentConfiguration");
		    String binaryString = "" + currentConfiguration.get("sensors").get(0) + currentConfiguration.get("sensors").get(1) + currentConfiguration.get("sensors").get(2);
		    int PSC = Integer.parseInt(binaryString, 2);
		    System.out.println("Calculating probability-R1:" + R1 + " R2:" + R2 + " R3:" + R3 + " PSC:" + PSC);
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
	
//		    System.out.println("Sending probability");
		    engine.send(receive_probability, this);
		}
    }

}
