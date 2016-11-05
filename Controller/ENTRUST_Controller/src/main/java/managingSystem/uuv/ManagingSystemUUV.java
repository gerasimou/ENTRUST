package managingSystem.uuv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import activforms.engine.ActivFORMSEngine;
import auxiliary.Utility;
import managedSystem.uuv.UUV;
import managingSystem.uuv.Effector;
import managingSystem.uuv.Sensor;

public class ManagingSystemUUV {

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER 		= 100;
	public static final double MULTIPLIER_RATES = 1000;

    /** ActivForms engine*/
	private ActivFORMSEngine engine;

	/** PRISM plugin*/
	private VerificationEngine verificationEngine;

	/** Probe handle*/
	private Sensor sensor;
 
	/** Effector handle*/
	private Effector effector;

	/** Communication handles*/
    private ServerSocket serverSocket;		// 	= new ServerSocket(portNumber);
    private Socket clientSocket;			//	= serverSocket.accept();
    private PrintWriter out;				// 	= new PrintWriter(clientSocket.getOutputStream(), true);
    private BufferedReader in;				//	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    /** new UUV configuration */
	int [] newConfiguration = new int[NUM_OF_SENSORS+1];
	
	
	/** Response Time stuff */
	StringBuilder responseTimes = new StringBuilder();
	long before;
	long after;
	
	public final static int NUM_OF_SENSORS				= 6;
	public final static int NUM_OF_SENSOR_COMBINATIONS 	= (int)Math.pow(2, NUM_OF_SENSORS);
	
	
	/**
	 * Managing System constructor
	 */
	public ManagingSystemUUV() {
		
		//initialise ActiveFORMS engine
	    try {
			this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9998);
		    this.engine.setRealTimeUnit(1000);

		    //init probe
		    this.sensor = new Sensor(engine, this);
//		    resetNewConfiguration();
		    
		    //init effector
		    this.effector = new Effector(engine, this);
		    effector.setNewConfigurationArray(newConfiguration);
		    
		    //init PRISM plugin
		    this.verificationEngine = new VerificationEngine(engine);
		    
		    //start the engine
		    engine.start();
	    } 
	    catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Listen for a message from the managed system
	 * @throws IOException
	 */
    public void startListening() throws IOException{
		 int portNumber = 56567;
		 serverSocket 	= new ServerSocket(portNumber);
		 System.out.println("Managing system ready - awaiting requests\n");

		 clientSocket	= serverSocket.accept();
		 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
		 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 System.out.println("Connection established");
		 
		 while (true){
			 try{

				 String input = in.readLine();
				 before = System.currentTimeMillis();
				
				 if (input.toLowerCase().equals("done"))
					break;
			 
				 String inputs[] = input.split(",");

				 double r1  = Double.parseDouble(inputs[0]);
				 double r2  = Double.parseDouble(inputs[1]);
				 double r3  = Double.parseDouble(inputs[2]);
				 double r4  = Double.parseDouble(inputs[3]);
				 double r5  = Double.parseDouble(inputs[4]);
				 double r6  = Double.parseDouble(inputs[5]);
//				 double r7  = Double.parseDouble(inputs[6]);
//				 double r8  = Double.parseDouble(inputs[7]);
				 System.out.println("{Rates}: " + r1 +","+ r2 +","+ r3 +","+ r4 +","+ r5 +","+ r6);// +","+ r7 +","+ r8);

				 sensor.sendAverageRates(r1, r2, r3, r4, r5, r6);//, r7, r8);
				 
			 }
			 catch (Exception e){
				 Utility.exportToFile("data/UUV/responseTimes.csv", responseTimes.toString(), true);
				 e.printStackTrace();
				 System.exit(-1);
			 }
		 }
		 out.println("Done");
		 System.exit(0);
    }
    
    
    
	/**
	 * Run managing system given an input
	 * @throws IOException
	 */
    public void run(String input) throws IOException{
    	before = System.currentTimeMillis();
				
		 if (input.toLowerCase().equals("done"))
			return;
			 
		 String inputs[] = input.split(",");

		 double r1  = Double.parseDouble(inputs[0]);
		 double r2  = Double.parseDouble(inputs[1]);
		 double r3  = Double.parseDouble(inputs[2]);
		 double r4  = Double.parseDouble(inputs[3]);
		 double r5  = Double.parseDouble(inputs[4]);
		 double r6  = Double.parseDouble(inputs[5]);
//				 double r7  = Double.parseDouble(inputs[6]);
//				 double r8  = Double.parseDouble(inputs[7]);
//				 System.out.println("{Rates}: " + r1 +","+ r2 +","+ r3);// +","+ r4 +","+ r5 +","+ r6 +","+ r7 +","+ r8);

		 sensor.sendAverageRates(r1, r2, r3
//				 				);
//		 						, r4);
//				 				,r4, r5);
		 						,r4, r5, r6);
		 						//,r4, r5, r6, r7, r8);
    }
    
    
    /** return the result to the managed system
     * 
     * @param newConfiguration
     */
    public void returnResult(int [] newConfiguration){
    	String result = "";

    	for (int index=0; index<newConfiguration.length; index++){
    		int tempResult = newConfiguration[index];
    		if (index==NUM_OF_SENSORS && tempResult!=-1)
    			result += tempResult/ManagingSystemUUV.MULTIPLIER;
    		else
    			result += tempResult +",";
    	}

    	System.out.printf("{New Config}:\t%s\n\n", result);
//    	Utility.exportToFile("sendToManagedSystem.txt", result, true);
    	after 			= System.currentTimeMillis();
		responseTimes.append((after - before) +"\n");
		 Utility.exportToFile("data/responseTimes.csv", responseTimes.toString(), true);

		
		out.println(result);
    	out.flush();

		/** jar */
//		 System.exit(1);
    }
	
	
	
	/**
	 * Reset the configuration; initialisation function
	 */
    private void resetNewConfiguration(){
    	Arrays.fill(newConfiguration, -1);
    }
	
}
