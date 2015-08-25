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
import managingSystem.uuv.Effector;
import managingSystem.uuv.Probe;

public class ManagingSystemUUV {

	/** Multiplier for use in ActiveForms (no use of doubles, hence converted to integers)*/
	public static final double MULTIPLIER 		= 100;
	public static final double MULTIPLIER_RATES = 1000;

    /** ActivForms engine*/
	private ActivFORMSEngine engine;

	/** PRISM plugin*/
	private PrismPlugin prismPlugin;

	/** Probe handle*/
	private Probe probe;
 
	/** Effector handle*/
	private Effector effector;

	/** Communication handles*/
    private ServerSocket serverSocket;		// 	= new ServerSocket(portNumber);
    private Socket clientSocket;			//	= serverSocket.accept();
    private PrintWriter out;				// 	= new PrintWriter(clientSocket.getOutputStream(), true);
    private BufferedReader in;				//	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    /** new UUV configuration */
	int [] newConfiguration = new int[4];
	
	
	/**
	 * Managing System constructor
	 */
	public ManagingSystemUUV() {
		
		//initialise ActiveFORMS engine
	    try {
			this.engine = new ActivFORMSEngine(Utility.getProperty("ACTIVFORMS_MODEL_FILE"), 9999);
		    this.engine.setRealTimeUnit(1000);

		    //init probe
		    this.probe = new Probe(engine, this);
//		    resetNewConfiguration();
		    
		    //init effector
		    this.effector = new Effector(engine, this);
		    effector.setNewConfigurationArray(newConfiguration);
		    
		    //init PRISM plugin
		    this.prismPlugin = new PrismPlugin(engine);
		    
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
//    	probe.sendAverageRates(5, 4, 4);
		 int portNumber = 56567;
		 serverSocket 	= new ServerSocket(portNumber);
		 System.out.println("Managing system ready - awaiting requests\n");

		 clientSocket	= serverSocket.accept();
		 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
		 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 System.out.println("Connection established");

		 while (true){
			 String input = in.readLine();
			 try{
				if (input.toLowerCase().equals("done"))
					break;
			 
				 String inputs[] = input.split(",");

				 double r1  = Double.parseDouble(inputs[0]);
				 double r2  = Double.parseDouble(inputs[1]);
				 double r3  = Double.parseDouble(inputs[2]);
				 System.out.println("{Rates}: " + r1 +","+ r2 +","+ r3);

				 probe.sendAverageRates(r1,r2,r3);
//				 returnResult(newConfiguration);
			 }
			 catch (Exception e){
				 e.printStackTrace();
				 System.exit(-1);
			 }
		 }
		 out.println("Done");
		 System.exit(0);
    }
    
    
    /** return the result to the managed system
     * 
     * @param newConfiguration
     */
    public void returnResult(int [] newConfiguration){
    	String result = "";

    	for (int index=0; index<newConfiguration.length; index++){
    		int tempResult = newConfiguration[index];
    		if (index==3 && tempResult!=-1)
    			result += tempResult/ManagingSystemUUV.MULTIPLIER;
    		else
    			result += tempResult +",";
    	}

    	System.out.println("{New Config}:\t" + result);
    	Utility.exportToFile("sendToManagedSystem.txt", result, true);
    	out.println(result);
    	out.flush();
    }
	
	
	
	/**
	 * Reset the configuration; initialisation function
	 */
    private void resetNewConfiguration(){
    	Arrays.fill(newConfiguration, -1);
    }
	
}
