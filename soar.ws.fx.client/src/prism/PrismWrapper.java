package prism;
//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//	- PRISMWrapper Class that invokes PRISM API and performs quantitative model checking	//
//	  for a parameterised model and a requested property.                                   //
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//	- PRISM.modelCheck(Model model, Property property, Expression expression) --> bug???    //
//																							//
//==========================================================================================//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;

 public class PrismWrapper {
	 
	private PrismLog		mainLog;
	private Prism			prism;
	private ModulesFile		modulesFile;
	private PropertiesFile	propertiesFile;
//	private Model			model;
	private 		String	MODELFILENAME			;
	private 		String  PROPERTIESFILENAME 		;
	private final String	PRISMOUTPUTFILENAME		= "logfile/output_Prism.txt";
	private final String 	MODELOUTPUTFILENAME		= "logfile/output_PrismWrapper.txt";
	private final String	ERRORFILENAME			= "logfile/output_error.txt";
	private String			modelString				= "";
	private File			specificationFile		;			

	private boolean 		bRealizeParametersFirst = true;
	private BufferedWriter 	errorFileWriter 		;
	private BufferedWriter fileWriter 				;
	private String 			prismResult				;
	
	private DecimalFormat df = new DecimalFormat("###.####");

	
	
	/**
	 * Class constructor 
	 * @param modelFile - the Markov model file to be provided as input to PRISM
	 * @param propertiesFile - the temporal logic file to be provided as input to PRISM
	 */
	public PrismWrapper(String modelFile, String propertiesFile) {
		this.MODELFILENAME  	= modelFile;
		this.PROPERTIESFILENAME	= propertiesFile;
		this.specificationFile 	= new File(PROPERTIESFILENAME);
		new File("logfile").mkdir();	
		readModel();
		initPrism();
	}
	
	
	/**
	 * Initialises PRISM object and log file
	 */
	private void initPrism(){
		try {
			//read the model just the first time the tool runs
			mainLog = new PrismFileLog(PRISMOUTPUTFILENAME,false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
		}
		catch (PrismException e) {
			writeToFileError("Error: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * This function receives data for the model and returns a double value for the quantified property.
	 * @param r1
	 * @param r2
	 * @param r3
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param PSC
	 * @param CSC
	 * @param s
	 * @param T
	 * @param propertyIndex
	 * @return
	 */
	public String runPrism(double r1, double r2, double r3, 
							double p1, double p2, double p3, 
							int PSC, int CSC, 
							double s, double T, int propertyIndex){
		try{
			//do this just for the first examined property - no need to realise parameters or build the model since
			// the model is the same when analysed for more than one requirements
			if (propertyIndex==0){
				//realise parameter values and start PRISM object only when monitored parameter values change
				realizeParameters(r1, r2, r3, p1, p2, p3, PSC, CSC, s, T);

//				writeModelToFile();
				//build the model
				buildModel();
			}

			prismResult = goPrism(propertiesFile, propertyIndex);
//			writeToFile(sensor1_rate, sensor2_rate, sensor3_rate, sensor1_succ_prob, sensor2_succ_prob, sensor3_succ_prob,
//						PREVIOUS_SENSOR_CONFIGURATION, CURRENT_SENSOR_CONFIGURATION, uuv_speed, T, prismResult);
//			prism.closeDown(false);
			return prismResult;
		}
		catch (PrismException e) {
			writeToFileError(r1 +"\t"+ r2 +"\t"+ r3 +"\t"+ p1 +"\t"+ p2 +"\t"+ p3 +"\t"+
							 PSC +"\t"+ CSC +"\t"+ s +"\t"+ T +"\t"+ "Error: " + e.getMessage());
			return ("-1.0");
		}
		catch (Exception e){
			writeToFileError(r1 +"\t"+ r2 +"\t"+ r3 +"\t"+ p1 +"\t"+ p2 +"\t"+ p3 +"\t"+
					 PSC +"\t"+ CSC +"\t"+ s +"\t"+ T +"\t"+ "Error: " + e.getMessage());
			System.out.println(e.getMessage() +"\t"); e.printStackTrace(); System.exit(0);
			return ("-2.0");
		}
	}
	
	
	/**
	 * Builds PRISM model
	 * @return <b>true</b> if the method completes successfully, <b>false</b> otherwise
	 */
	private boolean buildModel(){
		try{			
			modulesFile = prism.parseModelString(modelString);
			modulesFile.setUndefinedConstants(null);
			propertiesFile = prism.parsePropertiesFile(modulesFile, specificationFile);
			propertiesFile.setUndefinedConstants(null);
//			model = 
			prism.buildModel(modulesFile);
			return (true);
		}
		catch (FileNotFoundException e) {
			writeToFileError("Error: " + e.getMessage());
			return (false);
		}
		catch (PrismException e) {
			writeToFileError("Error: " + e.getMessage());
			System.err.println(e.getLocalizedMessage());
			return (false);
		}
		catch (Exception e ){
			writeToFileError("Error: " + e.getMessage());
			return (false);
		}
	}
	
	
	
	/**
	 * Run PRISM and quantify the property of interest
	 * @param propertiesFile
	 * @param propertyIndex
	 * @return
	 * @throws PrismLangException
	 * @throws PrismException
	 */
	private String goPrism(PropertiesFile propertiesFile, int propertyIndex) throws PrismLangException, PrismException
	{
//			System.out.print(propertiesFile.getProperty(propertyIndex).toString());
			Result result = prism.modelCheck(propertiesFile, propertiesFile.getProperty(propertyIndex));
//			Result result = prism.modelCheck(model, propertiesFile, propertiesFile.getProperty(0));
			return (result.getResult().toString());						
	}

	
	
	/**
	 * Replace parameters with their actual values acquired while monitoring the system
	 * @param sensor1_rate
	 * @param sensor2_rate
	 * @param sensor3_rate
	 * @param sensor1_succ_prob
	 * @param sensor2_succ_prob
	 * @param sensor3_succ_prob
	 * @param PREVIOUS_SENSOR_CONFIGURATION
	 * @param CURRENT_SENSOR_CONFIGURATION
	 * @param uuv_speed
	 * @param T
	 * @return <b>true</b> if the method completes successfully, <b>false</b> otherwise
	 */
	private boolean realizeParameters(double sensor1_rate, double sensor2_rate, double sensor3_rate, 
			double sensor1_succ_prob, double sensor2_succ_prob, double sensor3_succ_prob, 
			int PREVIOUS_SENSOR_CONFIGURATION, int CURRENT_SENSOR_CONFIGURATION, double uuv_speed, double T){
		if (bRealizeParametersFirst){
			modelString = modelString.replaceFirst("r1;" , "r1="+sensor1_rate+";");
			modelString = modelString.replaceFirst("r2;" , "r2="+sensor2_rate+";");
			modelString = modelString.replaceFirst("r3;" , "r3="+sensor3_rate+";");
			modelString = modelString.replaceFirst("p1;" , "p1="+sensor1_succ_prob+";");
			modelString = modelString.replaceFirst("p2;" , "p2="+sensor2_succ_prob+";");
			modelString = modelString.replaceFirst("p3;" , "p3="+sensor3_succ_prob+";");
			modelString = modelString.replaceFirst("PSC;", "PSC="+PREVIOUS_SENSOR_CONFIGURATION+";");
			modelString = modelString.replaceFirst("CSC;", "CSC="+CURRENT_SENSOR_CONFIGURATION+";");
			modelString = modelString.replaceFirst("s;", "s="+uuv_speed+";");
//			modelString = modelString.replaceFirst("T;", "T="+T+";");
			bRealizeParametersFirst = false;
		}
		else{
			modelString = modelString.replaceFirst("r1=[\\d]+.[\\d]+[-E\\d]*;", "r1="+sensor1_rate+";");
			modelString = modelString.replaceFirst("r2=[\\d]+.[\\d]+[-E\\d]*;", "r2="+sensor2_rate+";");
			modelString = modelString.replaceFirst("r3=[\\d]+.[\\d]+[-E\\d]*;", "r3="+sensor3_rate+";");
			modelString = modelString.replaceFirst("p1;=[\\d]+.[\\d]+;", "p1;="+sensor1_succ_prob+";");
			modelString = modelString.replaceFirst("p2=[\\d]+.[\\d]+;", "p2="+sensor2_succ_prob+";");
			modelString = modelString.replaceFirst("p3=[\\d]+.[\\d]+;", "p3="+sensor3_succ_prob+";");
			modelString = modelString.replaceFirst("PSC=[\\d]+;", "PSC="+PREVIOUS_SENSOR_CONFIGURATION+";");
			modelString = modelString.replaceFirst("CSC=[\\d]+;", "CSC="+CURRENT_SENSOR_CONFIGURATION+";");
			modelString = modelString.replaceFirst("s=[\\d]+.[\\d]+;", "s="+uuv_speed+";");
//			modelString = modelString.replaceFirst("T=[\\d]+.[\\d]+[-E\\d]*;", "T="+T+";");
		}
		return true;
	}
	
	
	
	/**
	 * Prints the model at the standard output
	 */
	private void printModel(){
		System.out.println(modelString);
	}
	
	
	
	/**
	 * Reads the model and save it as a string to <b>modelString</b> variable
	 * @return <b>true</b> if the operation completes successfully, <b>false</b> otherwise
	 */
	private boolean readModel(){
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(MODELFILENAME));
			String sCurrentLine;
			while ( (sCurrentLine = bufferedReader.readLine()) != null ){
				modelString += sCurrentLine + "\n";
			}
			bufferedReader.close();
		} 
		catch (IOException e) {
			writeToFileError("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Write to file all the result of the quantified property and the realised parameters of the model
	 * @param sensor1_rate
	 * @param sensor2_rate
	 * @param sensor3_rate
	 * @param sensor1_succ_prob
	 * @param sensor2_succ_prob
	 * @param sensor3_succ_prob
	 * @param PREVIOUS_SENSOR_CONFIGURATION
	 * @param CURRENT_SENSOR_CONFIGURATION
	 * @param uuv_speed
	 * @param T
	 * @param prismResult
	 * @return <b>true</b> if the operation completes successfully, <b>false</b> otherwise
	 */
	private boolean writeToFileParameters(double sensor1_rate, double sensor2_rate, double sensor3_rate, 
			double sensor1_succ_prob, double sensor2_succ_prob, double sensor3_succ_prob, 
			int PREVIOUS_SENSOR_CONFIGURATION, int CURRENT_SENSOR_CONFIGURATION, double uuv_speed, double T, String prismResult){
		try {
//			if (fileWriter==null){
				fileWriter = new BufferedWriter(new FileWriter(MODELOUTPUTFILENAME, true));
//			}
			fileWriter.write( 	df.format(Double.parseDouble(prismResult))  +"\t"+ PREVIOUS_SENSOR_CONFIGURATION +"\t"+ CURRENT_SENSOR_CONFIGURATION +"\t"+ df.format(uuv_speed)  +"\t"+ df.format(T) + "\t"+
								df.format(sensor1_rate) +"\t"+ df.format(sensor2_rate) +"\t"+ df.format(sensor3_rate) +"\t"+ 
								df.format(sensor1_succ_prob) +"\t"+ df.format(sensor2_succ_prob) +"\t"+ df.format(sensor3_succ_prob) +"\n" );
			fileWriter.close();
		} catch (IOException e) {
			writeToFileError("Error: " + e.getMessage());
			return false;
		}		
		return true;
	}
	
	
	
	/**
	 * Write the realised model to the appropriate file
	 * @return <b>true</b> if the operation completes successfully, <b>false</b> otherwise
	 */
	private boolean writeToFileModel(){
		try {
//			if (fileWriter==null){
				fileWriter = new BufferedWriter(new FileWriter(MODELOUTPUTFILENAME, true));
//			}
			fileWriter.write(modelString +"\n\n");
//			fileWriter.write(propertiesFile.getProperty(0).toString() +"\n");
			fileWriter.close();
		} catch (IOException e) {
			writeToFileError("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Write data to error file
	 * @param errorMessage
	 * @return <b>true</b> if the operation completes successfully, <b>false</b> otherwise
	 */
	private boolean writeToFileError(String errorMessage){
		try {
//			if (errorFileWriter==null){
				errorFileWriter = new BufferedWriter(new FileWriter(ERRORFILENAME, true));
//			}
			errorFileWriter.write(errorMessage +"\n");
			errorFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	
	
	/**
	 * Returns the result for the last quantified property as String
	 * @return the result as String
	 */
	protected String getPrismResult(){
		return prismResult;
	}

	
	
	/**
	 * Main function - Do nothing
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String args[]) throws InterruptedException
	{
//		LocalMainXOld.main(null);
	}
	
}
