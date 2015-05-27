package org.spg.Service.Based.System;

public class MainX {

    public static void main( String[] args )
    {
    	SBS sbs;
    	try {
        	sbs = new SBS();
//        	sbs.printServices();
        	sbs.run(5);
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }

}
