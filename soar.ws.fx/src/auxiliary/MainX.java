package auxiliary;

import soar.ws.fx.MarketWatchService1;

public class MainX {

	public static void main(String[] args) throws Exception {
		
		MarketWatchService1 wms = new MarketWatchService1();
		
		wms.initialiseService(0.99, 1, 0.5, null, null, "WM1");
		
		wms.run("Test");
	}

}
