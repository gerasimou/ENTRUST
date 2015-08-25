package evaluation;

import java.util.List;

import auxiliary.Utility;
import prism.PrismAPI;

/**
 * This is a dummy class used for the evaluation of the overheads
 * for runtime verification of adaptation requirements (RQV)
 * @author sgerasimou
 *
 */
public class MainEvaluator {
	private final static String  modelFilename 		= "models/uuv.sm";
	private final static String  propertiesFilename	= "models/uuv.csl";
	
	private final static double r1		= 5;
	private final static double r2		= 4;
	private final static double r3		= 4;
	private final static int 	PSC		= 5;

	
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(15000);

		for (int i=0; i<5; i++){
			Analyser analyser = new Analyser(modelFilename, propertiesFilename);
			analyser.doAnalysis(r1, r2, r3, PSC);
			Thread.sleep(5000);
		}
	}
}



class Analyser{
	private PrismAPI prism 			;//= new PrismAPI();
	private String  modelAsString 	;

	public Analyser(String modelFilename, String propertiesFilename){
		prism = new PrismAPI();
		modelAsString = Utility.readFile(modelFilename);
		prism.setPropertiesFile(propertiesFilename);
	}
	
	
	protected void doAnalysis(double r1, double r2, double r3, int PSC){
		for (int CSC=1; CSC<8; CSC++){
			for (int s=20; s<=40; s++){
				
				int index 	= ((CSC-1)*21)+(s-20);

				double speed = s/10.0;
				
				double p1 	= estimateP(speed, 3.5, 1.5,95);
				double p2 	= estimateP(speed, 3.0, 2.0,90);
				double p3 	= estimateP(speed, 2.5, 2.5,85);
				
				//Generate a correct PRISM model									
				String modelString = realiseProbabilisticModel(r1, r2, r3, p1, p2, p3, CSC, PSC, speed);
   				                		
				//load the PRISM model
				prism.loadModel(modelString);

				//run PRISM
				List<Double> prismResult = prism.runPrism();
				double req1result = prismResult.get(0);
				double req2result = prismResult.get(1);
				
				System.out.println("["+ CSC +","+ speed +"] : "+ req1result +","+ req2result);
			}
		}
	}
    
	
	/**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double r1, double r2, double r3, double p1, double p2, double p3,
    										 int CSC, int PSC, double speed){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");
		model.append("const double r1  = "+ r1 		+";\n");
		model.append("const double r2  = "+ r2 		+";\n");
		model.append("const double r3  = "+ r3 		+";\n");
		model.append("const double p1  = "+ p1 		+";\n");
		model.append("const double p2  = "+ p2 		+";\n");
		model.append("const double p3  = "+ p3 		+";\n");
		model.append("const int    CSC = "+ CSC 	+";\n");
		model.append("const int    PSC = "+ PSC 	+";\n");
		model.append("const double s   = "+ speed 	+";\n\n");

    	return model.toString();
    }


	private double estimateP(double speed, double speedThreshold, double alpha, double beta){
		if (speed <= speedThreshold){
			return (100 - alpha * speed / speedThreshold);
		}
		else{
			double result;
			result = beta - 30 * (speed-speedThreshold) / (5.0);
			return (result < 0) ? 0 : result;
		}
	}
}
