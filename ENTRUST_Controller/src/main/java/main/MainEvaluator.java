package main;

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
	private final static String  modelFilename 		= "models/uuv6.sm";
	private final static String  propertiesFilename	= "models/uuv.csl";
	public final static int NUM_OF_SENSORS				= 6;
	public final static int NUM_OF_SENSOR_COMBINATIONS 	= (int)Math.pow(2, NUM_OF_SENSORS);
	
	private final static double r1		= 5;
	private final static double r2		= 4;
	private final static double r3		= 4;
	private final static double r4		= 5;
	private final static double r5		= 4;
	private final static double r6		= 4;
	private final static int 	PSC		= 5;

	
	public static void main(String[] args) throws InterruptedException {
//		Thread.sleep(5000);

		for (int i=0; i<5; i++){
			Analyser analyser = new Analyser(modelFilename, propertiesFilename);
			long before = System.currentTimeMillis();
			analyser.doAnalysis(r1, r2, r3
//							  	, PSC);
//								, r4, PSC);
								, r4, r5, r6, PSC);
//								, r4, r5, r6, r7, r8, PSC);
			long after= System.currentTimeMillis();
			System.err.println("Time: " + ((after - before)/1000.0));
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
	
	
	protected void doAnalysis(double r1, double r2, double r3
				//			, int PSC){
				//			, double r4, int PSC){
							, double r4, double r5, double r6, int PSC){
							//, double r4, double r5, double r6, double r7, double r8, int PSC){

		for (int CSC=1; CSC<MainEvaluator.NUM_OF_SENSOR_COMBINATIONS; CSC++){
			for (int s=20; s<=40; s++){
				
				int index 	= ((CSC-1)*21)+(s-20);

				double speed = s/10.0;
				
				double p1 	= estimateP2(s/10.0, 5);
				double p2 	= estimateP2(s/10.0, 7);
				double p3 	= estimateP2(s/10.0, 11);
				double p4 	= estimateP2(s/10.0, 5);
				double p5 	= estimateP2(s/10.0, 7);
				double p6 	= estimateP2(s/10.0, 11);
				
				//Generate a correct PRISM model									
				String modelString = realiseProbabilisticModel(r1, r2, r3, r4, r5, r6,// r7, r8, 
															   p1, p2, p3, p4, p5, p6,// p7, p8, 
															   CSC, PSC, s/10.0);
   				                		
				//load the PRISM model
				prism.loadModel(modelString);

				//run PRISM
				List<Double> prismResult = prism.runPrism();
				double req1result = prismResult.get(0);
				double req2result = prismResult.get(1);
				
				System.out.println(index + "\t["+ CSC +","+ speed +"] : "+ req1result +","+ req2result);
			}
		}
	}
    
	
    /**
     * Generate a complete and correct PRISM model instance using the service features given as parameters
     * @param srvFeatures
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(double r1, double r2, double r3, double r4, double r5, double r6, //double r7, double r8,
    										 double p1, double p2, double p3, double p4, double p5, double p6, //double p7, double p8,
    										 int CSC, int PSC, double speed){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");
		model.append("const double r1  = "+ r1 		+";\n");
		model.append("const double r2  = "+ r2 		+";\n");
		model.append("const double r3  = "+ r3 		+";\n");
		model.append("const double r4  = "+ r4 		+";\n");
		model.append("const double r5  = "+ r5 		+";\n");
		model.append("const double r6  = "+ r6 		+";\n");
//		model.append("const double r7  = "+ r7 		+";\n");
//		model.append("const double r8  = "+ r8 		+";\n");
		model.append("const double p1  = "+ p1 		+";\n");
		model.append("const double p2  = "+ p2 		+";\n");
		model.append("const double p3  = "+ p3 		+";\n");
		model.append("const double p4  = "+ p4 		+";\n");
		model.append("const double p5  = "+ p5 		+";\n");
		model.append("const double p6  = "+ p6 		+";\n");
//		model.append("const double p7  = "+ p7 		+";\n");
//		model.append("const double p8  = "+ p8 		+";\n");
		model.append("const int    CSC = "+ CSC 	+";\n");
		model.append("const int    PSC = "+ PSC 	+";\n");
		model.append("const double s   = "+ speed 	+";\n\n");

    	return model.toString();
    }
	
	private static double estimateP2(double speed, double alpha){
		return 100 - alpha * speed;
	}
}
