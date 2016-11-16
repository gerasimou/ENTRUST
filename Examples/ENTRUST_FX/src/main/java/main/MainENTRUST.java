package main;

import controller.ENTRUST;
import fx.FX;

public class MainENTRUST {

	public static void main(String[] args) {
		try {
			int portNumber = 56567;
			
			Thread entrust	= new Thread(new ENTRUST(portNumber), "Entrust" );
			Thread fx		= new Thread(new FX(portNumber),      "FX");
			
			entrust.start();
			fx.start();
			
			entrust.join();
			fx.join();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
