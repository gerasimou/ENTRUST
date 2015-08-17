package managingsystem;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import mainX.MainX;
import activforms.engine.ActivFORMSEngine;
import activforms.engine.Synchronizer;

public class Probe extends Synchronizer {
    
    private ActivFORMSEngine engine;
    int setAverageMRates;
//    Timer timer = new Timer();
    private MainX mainX;
    
    
    public Probe(ActivFORMSEngine engine, MainX mainX){
		this.mainX = mainX;
		this.engine = engine;
		setAverageMRates = engine.getChannel("setAverageMRates");
//		timer.scheduleAtFixedRate(new TickerTask(), 0, 1000*10);
    }

    @Override
    public void accepted(int arg0) {
//    	System.out.println("Probe.accepted()");
    }

    @Override
    public boolean readyToReceive(int arg0) {
//    	System.out.println("Probe.readyToReceive()");
    	return false;
    }

    @Override
    public void receive(int arg0, HashMap<String, Object> arg1) {
//    	System.out.println("Probe.receive()");
    }

    public void sendAverageRates(double r1, double r2, double r3) {
//    	System.out.println("Probe.sendAverageRates()");
//		int[] measurements = mainX.getMeasurements();
		String [] avgRates = new String[3];
		avgRates[0] = "avgRates[0]=" + (int)(r1*MainX.MULTIPLIER);
		avgRates[1] = "avgRates[1]=" + (int)(r2*MainX.MULTIPLIER);
		avgRates[2] = "avgRates[2]=" + (int)(r3*MainX.MULTIPLIER);
//		for (int i = 0; i < measurements.length; i++) {
//		    avgRates[i] = "avgRates[" + i + "]=" + measurements[i];///10;
//		    measurements[i] = 0;
//		}
		engine.send(setAverageMRates,this, avgRates);
//		System.out.println("Measurement Taken SensorId=" + sensorId);
    }

//    private class TickerTask extends TimerTask {
//	   	@Override
//	   	public void run() {
//	   		sendAverageRates();
//	   	 }
//    }
}
