package managingSystem;

import java.util.ArrayList;
import java.util.List;

import fx.services.AbstractServiceClient;
import sbs.SBS;

public class ManagingSystem{
	
	List<List<AbstractServiceClient>> operationsList;
		
	SBS sbs;
	
	public ManagingSystem(SBS theSystem){
		this.sbs 			= theSystem;
		this.operationsList = this.sbs.getOpetionsList();
	}

	
	public void execute(){
		System.err.println(this.getClass().getName());
		sbs.setActiveServicesList(new int[]{0,1,0,0,0,0});
	}
	
	
    /** Set up the initial system configuration considering nominal values for the services*/
    private void setupInitialConfiguration(){
    	//get the cartesian product
    	
    	List<List<Double>> configurationResults = new ArrayList<List<Double>>();
    	
    	for (int indexList0=0; indexList0<operationsList.get(0).size(); indexList0++){
    		AbstractServiceClient srvList0 = operationsList.get(0).get(indexList0);

    		for (int indexList1=0; indexList1<operationsList.get(1).size(); indexList1++){
    			AbstractServiceClient srvList1  = operationsList.get(1).get(indexList1);
    			
        		for (int indexList2=0; indexList2<operationsList.get(2).size(); indexList2++){
        			AbstractServiceClient srvList2 = operationsList.get(2).get(indexList2); 
        			
            		for (int indexList3=0; indexList3 < operationsList.get(3).size(); indexList3++){
            			AbstractServiceClient srvList3 = operationsList.get(3).get(indexList3);
            					
                		for (int indexList4=0; indexList4<operationsList.get(4).size(); indexList4++){
                			AbstractServiceClient srvList4 = operationsList.get(4).get(indexList4);

                			for (int indexList5=0; indexList5 < operationsList.get(5).size(); indexList5++){
                				AbstractServiceClient srvList5  = operationsList.get(5).get(indexList5);
                				
                    			List<Double> list = new ArrayList<Double>();
//                    			list
                    		}                			
                		}    			            			
            		}    			
        		}    			
    		}
    	}
    }
	
}
