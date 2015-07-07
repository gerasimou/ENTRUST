package mockup;

import java.util.Arrays;
import java.util.List;

import auxiliary.Utility;
import mockup.DataGenerator.Operation;

public class Selection {
	
	public static final double RELIABILITY_MIN	= 0.95;
	public static final double RELIABILITY_MAX	= 0.9999;
	public static final double COST_MIN			= 1; 
	public static final double COST_MAX			= 15;
	public static final double RESPONSETIME_MIN	= 0.5;
	public static final double RESPONSETIME_MAX	= 5;
	public static final String OUTPUT_FILE		= "data63.txt";
	
	private List<List<Operation.Service>> listCP;
	
	
	public static void main(String args[]){
		int numOfOperations				= 6;
		int numOfServicesPerOperation	= 3;
		
		
		DataGenerator dataGen = new DataGenerator(numOfOperations, numOfServicesPerOperation);
//		dataGen.printOperationCharacteristics();
		dataGen.exportSystemCharacteristics();
		
		Selection selection = new Selection();
		
		selection.listCP = dataGen.generateCartesianProductList();

		selection.exportListCP();

		selection.selectBestConfiguration(numOfOperations, numOfServicesPerOperation);
		
	}
	
	/**
	 * Selects the best configuration that satisfies system requirements
	 * @return
	 */
	public List<Operation.Service>selectBestConfiguration(int numOfOperations, int numOfServicesPerOperation){
		List<Operation.Service> configurationBest 	= null;
		double resultBest							= -1;
		int    indexBest 							= -1;
		for (int index=0; index<listCP.size(); index++){
			List<Operation.Service> configuration  = listCP.get(index);
			double result = 0;
			for (Operation.Service selectedService : configuration){
				result += selectedService.evaluateService();
			}
			result /= numOfOperations;
			if (result > resultBest){
				resultBest 			= result;
				configurationBest 	= configuration; 
				indexBest			= index+1;
			}
		}
		
		String output = "\nBest Results\n------------\n" + indexBest +" "+ configurationBest +"\t"+ resultBest; 
		Utility.exportToFile(OUTPUT_FILE, output, true);
		return configurationBest;
	}
	
	
	private void exportListCP(){
		StringBuilder outputStr = new StringBuilder();
		outputStr.append("Possible System Configurations\n---------------------\n");
		for (List<Operation.Service> configuration : listCP){
			outputStr.append(Arrays.toString(configuration.toArray()) + "\n");
		}
		Utility.exportToFile(OUTPUT_FILE, outputStr.toString(), true);
	}
	
}
