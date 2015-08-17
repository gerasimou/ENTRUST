package mainX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import activforms.engine.ActivFORMSEngine;
import activforms.goalmanagement.Goal;
import managingsystem.Effector;
import managingsystem.Probe;
import managedsystem.GoalChecker;

public class MainX {

	public static final double MULTIPLIER = 1000;
	
//    private int speed 	= 25;
//    Sensor[] sensors 	= new Sensor[3];
//    int []count 		= new int[3];
	int [] newConfiguration = new int[4];
    Probe probe;
    Effector effector;
    ServerSocket serverSocket;// 	= new ServerSocket(portNumber);
    Socket clientSocket;//		= serverSocket.accept();
    PrintWriter out;// 			= new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader in;//			= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


    /**
     * @param args
     */
    public static void main(String[] args) {
    	
		// Declaring managed system
		MainX mainX = new MainX();
	
		// Declaring managing system
		ActivFORMSEngine engine = null;
		try {
		    engine = new ActivFORMSEngine("models/uuv-avg.xml", 9999);
		    engine.setRealTimeUnit(1000);

		    // Setting Probe
		    mainX.probe = new Probe(engine, mainX);
		    mainX.resetNewConfiguration();
	
		    // Prism plugin
//		    PrismPlugin prismPlugin = new PrismPlugin(engine);
	
//		    engine.addGoal(new Goal("Requirement1", "currentConfiguration.req1Result > 20", new GoalChecker(), ""));
//		    engine.addGoal(new Goal("Requirement2", "currentConfiguration.req2Result < 120", new GoalChecker(), ""));
			    	
		    
		    // Setting Effector
		    mainX.effector = new Effector(engine);
		    mainX.effector.setNewConfigurationArray(mainX.newConfiguration);
//		    effector.setSensors(mainX.sensors);
		    mainX.effector.setMainX(mainX);
//			   mainX.changeSensorStatus(true, true, true);
	
		    engine.start();
		    mainX.startListening();
	
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
    }
    
    
    public void startListening() throws IOException{
		 int portNumber = 56567;
		 serverSocket 	= new ServerSocket(portNumber);
		 System.out.println("Prism(server) ready - awaiting requests\n");

		 clientSocket	= serverSocket.accept();
		 out 			= new PrintWriter(clientSocket.getOutputStream(), true);
		 in				= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 System.out.println("Connection established");
		 String input;
		 
		 while (true){
			 input = in.readLine();
			 try{
				if (input.toLowerCase().equals("done"))
					break;
			 
				 String inputs[] = input.split(",");

				 double r1  = Double.parseDouble(inputs[0]);
				 double r2  = Double.parseDouble(inputs[1]);
				 double r3  = Double.parseDouble(inputs[2]);
				 
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
    
    
    public void returnResult(int [] newConfiguration){
    	String output = "";
    	for (int index=0; index<newConfiguration.length; index++){
    		int tempResult = newConfiguration[index];
    		if (index==3 && tempResult!=-1)
    			output += tempResult/100.0;
    		else
    			output += tempResult +",";
    	}
//    	output = "1,0,1,3.0";
    	System.out.println(output);
    	
    	out.println(output);
    	out.flush();
//    	resetNewConfiguration();
    }
    
    public void resetNewConfiguration(){
    	Arrays.fill(newConfiguration, -1);
    }

    
}

