package managingSystem;

import java.util.ArrayList;
import java.util.List;

import fx.services.AbstractServiceClient;

public class Knowledge {
	
	private Knowledge(){}
	
	
	private static final List<List<ServiceCharacteristics>> operationCharacteristicsList = new ArrayList<List<ServiceCharacteristics>>();
	
	/** List that keep the services per operation */
	private static List<List<AbstractServiceClient>> operationsList;

	
	
    /** Create a list for storing services characteristics that will be used when carrying out RQV*/
	public static void initKnowledge (List<List<AbstractServiceClient>> opsList){
    	for (int operation=0; operation<opsList.size(); operation++){
    		List<ServiceCharacteristics> servicesCharacteristicsList = new ArrayList<ServiceCharacteristics>();

    		for (int service=0; service<opsList.get(operation).size(); service++){
    			AbstractServiceClient serviceClient = opsList.get(operation).get(service);
    			Object[] serviceCharacteristics = serviceClient. getServiceCharacteristics();
    			servicesCharacteristicsList.add(new ServiceCharacteristics((String)serviceCharacteristics[0], (double)serviceCharacteristics[1], 
    																	   (double)serviceCharacteristics[2], (double)serviceCharacteristics[3]));
    		}//for
    		operationCharacteristicsList.add(servicesCharacteristicsList);

    	}//for
    	
    	operationsList = opsList;
    }//
	
	
	public static List<List<ServiceCharacteristics>> getKnowledge(){
		return operationCharacteristicsList;
	}
	
	
	public static List<List<AbstractServiceClient>> getOperationsList(){
		return operationsList;
	}

	
    //private class
    static class ServiceCharacteristics{
    	/** service actual reliability*/
    	private double reliability;
    	
    	/** cost per invocation*/
    	private double costPerInvocation;
    	
    	/** time required to carry out its functionality per invocation*/
    	private double timePerInvocation;
    	
    	/** Service ID*/
    	private String id;
    	    	
    	public ServiceCharacteristics(String id, double costPerInvocation, double timePerInvocation, double reliability) {
    		this.id 				= id;
    		this.costPerInvocation 	= costPerInvocation;
    		this.timePerInvocation	= timePerInvocation;
    		this.reliability		= reliability;
		}

    	
    	public double getCostPerInvocation(){
    		return this.costPerInvocation;
    	}
    	
    	public double getTimePerInvocation(){
    		return this.timePerInvocation;
    	}
    	
    	public String getID(){
    		return this.id;
    	}
    	
    	public double getReliability(){
    		return this.reliability;
    	}
    	
    	public void setReliability(double reliability){
    		this.reliability = reliability;
    	}
    }
}
