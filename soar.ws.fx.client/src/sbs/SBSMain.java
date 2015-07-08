package sbs;



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
        	sbs.run();
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
}
