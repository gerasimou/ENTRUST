package main;

import controller.ENTRUST;
import uuv.UUV;

public class MainENTRUST {

	public static void main(String[] args) {
		try {
			int portNumber = 56567;

			Thread entrust	= new Thread(new ENTRUST(portNumber), "Entrust" );
			Thread uuv		= new Thread(new UUV(portNumber),     "UUV");
			
			entrust.start();
			uuv.start();
			
			entrust.join();
			uuv.join();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
