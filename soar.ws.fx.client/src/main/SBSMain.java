package main;

import java.util.Arrays;

import managingSystem.ManagingSystemOld;
import sbs.SBS;

/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBSMain 
{	
    public static void main( String[] args )
    {
    	    	
    	/** Managing system handler*/
    	ManagingSystem managingSystem;

    	/** SBS system handler */
    	SBS sbs;

    	try {
        	sbs = new SBS();
        	Thread sbsThread = new Thread(sbs, "SBS");
        	sbsThread.start();
        	
        	managingSystem = new ManagingSystem(sbs);
        	Thread managingSystemThread = new Thread(managingSystem, "ManagingSystem");
        	managingSystemThread.start();
        	
        	//wait until the simulation ends
        	while (sbsThread.isAlive());
        	//when it does, stop the managing system.
        	managingSystemThread.interrupt();
        	
        	while (sbsThread.isAlive() || managingSystemThread.isAlive());
    		System.err.println("Done");

    	} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    private static void numbers(){
    	final int OPERATIONS = 3;
    	final int SERVICES   = 3;
    	final int CONFIGURATIONS = (int) Math.pow(SERVICES, OPERATIONS);
    	
    	//generate the divisors
    	int[] divisor = new int[OPERATIONS];//{243, 81, 27, 9, 3, 1}; //{#srv^(#op-1), #srv^(#op-2), ..., #srv^(0)}
    	for (int i=0; i<OPERATIONS; i++){
    		divisor[OPERATIONS-1-i] = (int) Math.pow(SERVICES, i);
    	}
    	System.out.println(Arrays.toString(divisor));
    	
    	int x, num;
    	for (int config=0; config<CONFIGURATIONS; config++){
	    	num = config;    		 
	    	System.out.printf("\n%d)\t", config);
	    	for (int i=0; i<divisor.length; i++){
	        	x = num / divisor[i];
	        	
	        	System.out.print(x +" ");
	        	num -= (divisor[i] * x);
	    	}
    	}    	
    	
    	System.exit(0);
    }
}
