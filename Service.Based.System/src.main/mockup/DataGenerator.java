package mockup;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import auxiliary.Utility;

public class DataGenerator {

	private List<Operation> operationsList;
	private int numOfOperations;
	private int servicesPerOperation;
	private static Random rand;
	
	
	public DataGenerator(int operationsNum, int servicesNum){
		this.numOfOperations 		= operationsNum;
		this.servicesPerOperation	= servicesNum;
		this.operationsList 		= new ArrayList<Operation>();
		this.rand 					= new Random(System.currentTimeMillis());
		
		//create new operations
		for (int index=0; index<numOfOperations; index++){
			operationsList.add(new Operation(servicesPerOperation));
		}
	}
	
	
	private static double generateServiceCharacteristic(double max, double min){
		String formatted = null;
		DecimalFormat df=new DecimalFormat("#.####");
		try {
			double range 	= max - min;
			double value 	= rand.nextDouble();
			double scaled	= value * range;
			double shifted 	= scaled + min; 
			formatted		= df.format(shifted);
			return (Double)df.parse(formatted);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (Double) null;
	}
	
	
	protected void printSystemCharacteristics(){
		for (Operation operation : operationsList){
			System.out.println(operation.toString());
		}
	}
	
	
	protected void exportSystemCharacteristics(){
		StringBuilder outputStr = new StringBuilder();
		outputStr.append("Operations: " + numOfOperations +"\n");
		outputStr.append("Services Per Operation: " + servicesPerOperation +"\n");
		outputStr.append("-------------------------\n");
		for (Operation operation : operationsList){
			List<double[]> operationCharacteristicsList = operation.getCharacteristicsAsListOfDouble();
			for (double[] srvChr : operationCharacteristicsList){
				outputStr.append(Arrays.toString(srvChr) +"\t");
			}
			outputStr.append("\n");
		}
		outputStr.append("\n\n");
		Utility.exportToFile(Selection.OUTPUT_FILE, outputStr.toString(), false);
	}
	
	
	protected List<List<double[]>> getSystemCharacteristicsAsListofListOfDoubles(){
		List<List<double[]>> operationCharacteristicsList = new ArrayList<List<double[]>>();
		for (Operation operation : operationsList){
			operationCharacteristicsList.add(operation.getCharacteristicsAsListOfDouble());
		}
		return operationCharacteristicsList;		
	}

	
	protected List<List<Operation.Service>> getSystemCharacteristicsAsListofListOfService(){
		List<List<Operation.Service>> operationCharacteristicsList = new ArrayList<List<Operation.Service>>();
		for (Operation operation : operationsList){
			operationCharacteristicsList.add(operation.servicesList);
		}
		return operationCharacteristicsList;		
	}

	
	protected List<List<Operation.Service>> generateCartesianProductList(){
		List<List<Operation.Service>> listCP = null;
		try{
			
			List<List<Operation.Service>> operationCharacteristicsList = getSystemCharacteristicsAsListofListOfService();
	
			//calculate the size of the cartesian product list
			int listCPSize = 1;
			for (List<Operation.Service> srvList : operationCharacteristicsList){
				listCPSize *= srvList.size();
			}
			
			//initialise list with the first list
			listCP = new ArrayList<List<Operation.Service>>(listCPSize);
//			listCP.add(operationCharacteristicsList.get(0));

//			System.out.println(listCP.get(0).get(0));
			
			for (int index=0; index<operationCharacteristicsList.size(); index++){//for all the elements of the big list
				List<List<Operation.Service>> listCPTemp = new ArrayList<List<Operation.Service>>();
				
				List<Operation.Service> list1 = operationCharacteristicsList.get(index);
				
				if (listCP.isEmpty()){
					for (int j=0; j<list1.size(); j++){//for all the elements in the currently examined list						
						List<Operation.Service> serviceList = new ArrayList<Operation.Service>();
						serviceList.add(list1.get(j));
						listCPTemp.add(serviceList);						
					}//for					
				}
				else{
					for (int i=0; i<listCP.size(); i++){//for all the elements in the CP list
						for (int j=0; j<list1.size(); j++){//for all the elements in the currently examined list
		
							List<Operation.Service> serviceList = new ArrayList<Operation.Service>();
							serviceList.addAll(listCP.get(i));
							serviceList.add(list1.get(j));
							listCPTemp.add(serviceList);						
						}//for
					}//for
				}
	
				list1.clear();
				listCP.clear();
				listCP = listCPTemp;//new ArrayList<String>(listCPTemp);
			}//for
			
			operationCharacteristicsList.clear();
		}
		catch (OutOfMemoryError e){
			System.err.println(listCP.size());
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println(listCP.size());
		for (List<Operation.Service> list : listCP){
			System.out.println(Arrays.toString(list.toArray()));
		}
		
		return listCP;
	}
	
	
	//private class
	class Operation{
		List<Service> servicesList;
		
		private Operation(int servicesPerOperation){
			this.servicesList = new ArrayList<Service>();
			
			//create new services
			for (int index=0; index<servicesPerOperation; index++){
				servicesList.add(new Service());
			}
		}
		
		@Override
		public String toString(){
			StringBuilder str = new StringBuilder();
			for (Service service : servicesList){
				str.append(service.toString() +"-");
			}
			return str.toString();
		}
		
		public List<double[]> getCharacteristicsAsListOfDouble(){
			List<double[]> serviceCharacteristicsList = new ArrayList<double[]>();
			for (Service service : servicesList){
				serviceCharacteristicsList.add(service.getCharacteristicsAsArray());
			}
			return serviceCharacteristicsList;
		}
	
		
		class Service{
			
			private double reliability;
			private double cost;
			private double responseTime;
			
			private Service(double reliability, double cost, double responseTime){
				this.reliability	= reliability;
				this.cost			= cost;
				this.responseTime	= responseTime;
			}
			
			private Service(){
				this.reliability 	= DataGenerator.generateServiceCharacteristic(Selection.RELIABILITY_MIN, Selection.RELIABILITY_MAX);
				this.cost			= DataGenerator.generateServiceCharacteristic(Selection.COST_MIN, Selection.COST_MAX);
				this.responseTime	= DataGenerator.generateServiceCharacteristic(Selection.RESPONSETIME_MIN, Selection.RESPONSETIME_MAX);
			}
			
			
			public double getReliability(){
				return this.reliability;
			}
			
			public double getCost(){
				return this.cost;
			}
			
			public double getResponseTime(){
				return this.responseTime;
			}
			
			@Override
			public String toString(){
				return "["+ reliability +","+ cost +","+ responseTime +"]";
			}
			
			public double[] getCharacteristicsAsArray(){
				return new double[] {this.reliability, this.cost, this.responseTime};
			}
			
			
			public double evaluateService(){
				double result;
				result = ( (reliability / Selection.RELIABILITY_MAX) 	+
						   (1.0 / cost) +
						   (1.0 / responseTime)
						 ) / 3.0;
//						 (cost/Selection.COST_MAX)					+
//						 (responseTime/Selection.RESPONSETIME_MAX);
				return result;
			}
		}
	}	
}
