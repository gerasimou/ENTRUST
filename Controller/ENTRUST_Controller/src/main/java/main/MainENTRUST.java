package main;

import controller.ENTRUST;

public class MainENTRUST {
	public static String configFile = "resources/config.properties";

	public static void main(String[] args) {
		try {
			ENTRUST entrust = new ENTRUST();
			entrust.start();
			System.exit(-1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
