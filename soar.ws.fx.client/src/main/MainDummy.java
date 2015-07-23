package main;

import fx.services.MW.MarketWatchClient2;
import fx.services.TA.TechnicalAnalysisClient;

public class MainDummy {

	public static void main(String[] args) {
		try {
//			WatchMarketClient1		clientWM1 = new WatchMarketClient1();	
			MarketWatchClient2 		clientWM2 = new MarketWatchClient2();
			
			TechnicalAnalysisClient	clientTA = new TechnicalAnalysisClient();
			
//			clientWM1.initialise("9999");
//			clientWM1.run();
			
			clientWM2.initialise("1111");
			clientWM2.run();
			
//			clientTA.initialise("1234");
//			clientTA.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
