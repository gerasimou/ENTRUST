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
        	sbs.run();
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
}
