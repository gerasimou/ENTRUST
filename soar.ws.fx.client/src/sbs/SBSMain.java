package sbs;

import managingSystem.ManagingSystem;

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
        	
        	while (sbsThread.isAlive());
        	managingSystemThread.interrupt();
    		System.err.println("Done");

    	} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
}
