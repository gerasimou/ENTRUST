package prism;

import java.util.HashMap;

import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;
import activforms.types.UppaalType;
import main.ManagingSystem;
import managingSystem.Analyser;
import managingSystem.RQVResult;

public class PrismPlugin implements Synchronizer{
    
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
		
		//register receiving signal
		this.engine.register(startRQV, this, "avgFRates", "sConfig", "&RQVResultsArray");		

		//and get channels for incoming and outgoing signals
		this.startRQV = engine.getChannel("startRQV");
		this.finishRQV = engine.getChannel("finishRQV");
    }
  
    
    /**
     * When invoked, carry out QV
     */
    @Override
    public void receive(int channelId, HashMap<String, Object> data) {
		if (channelId == startRQV){
		    HashMap<Integer, Integer> avgRates = (HashMap<Integer, Integer>)data.get("avgRates");
		    System.out.println(avgRates);

		    int servicesPerOperation    = analyser.getNumOfServicesPerOperation();
		    int[][] servicesReliability = new int[analyser.getNumOfOperations()][servicesPerOperation];
		    int row=-1;
		    for (int i=0; i<avgRates.size(); i++){
		    	System.err.println(avgRates.get(i));
		    	if (i%servicesPerOperation==0)
		    		row++;
		    	servicesReliability[row][i%servicesPerOperation] = avgRates.get(i);
		    }
		    
		    analyser.doAnalysis(services, r2, r3, PSC)
		    
		    
		    double R1 = avgRates.get(0)/ManagingSystem.MULTIPLIER;
		    double R2 = avgRates.get(1)/ManagingSystem.MULTIPLIER;
		    double R3 = avgRates.get(2)/ManagingSystem.MULTIPLIER;
	

		    
		    RQVResult[] results = analyser.doAnalysis(R1, R2, R3, PSC);
		    HashMap<Integer, HashMap<String, Object>> array = (HashMap<Integer, HashMap<String, Object>>)data.get("&RQVResultsArray");
		    HashMap<String, Object> RQV;
		    for(int i = 0; i < results.length; i++){
				RQV = array.get(i);
				((UppaalType)(((HashMap<String, UppaalType>)RQV.get("cConfig"))).get("0")).setValue(3/*For example*/);
				((UppaalType)RQV.get("req1Result")).setValue(results[i].getReq1Result());
				((UppaalType)RQV.get("req2Result")).setValue(results[i].getReq2Result());
				((UppaalType)RQV.get("req3Result")).setValue(results[i].getReq2Result());
		    }
	
		    
		    //return output to ActivFORMS
//		    System.out.println("Sending probability");
		    engine.send(finishRQV, this);
		}
    }
    
    
    @Override
    public void accepted(int arg0) {
	if (arg0 == startRQV){}
//	    System.out.println("Probability accepted");
    }

    
    @Override
    public boolean readyToReceive(int arg0) {
    	return true;
    }
}
