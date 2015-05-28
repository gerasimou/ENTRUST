package org.spg.Service.Based.System;



/**
 * Main Class
 * @author sgerasimou
 *
 */
public class SBSMain 
{	
    public static void main( String[] args )
    {
    	SBS sbs;
    	try {
        	sbs = new SBS();
//        	sbs.printServices();
        	sbs.run(20);
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
}
