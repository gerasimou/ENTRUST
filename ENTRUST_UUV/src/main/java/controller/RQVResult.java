package controller;

//class that represents a data structure for storing an RQVResult
public class RQVResult{
	int 	sensor1;
	int		sensor2;
	int		sensor3;
	int 	speed;
	int 	req1Result; // expected number of accurate measurements
	int 	req2Result; // expected power consumption
	
	public RQVResult(){
		sensor1 	= -1;
		sensor2 	= -1;
		sensor3		= -1;
		speed		= -1;
		req1Result 	= -1;
		req2Result 	= -1;
	}
	
	public RQVResult (int CSC, double speed, double req1Result, double req2Result){
		this.sensor1 	= CSC%2;
		this.sensor2 	= CSC%4>1 ? 1 : 0;
		this.sensor3 	= CSC%8>3 ? 1 : 0;
		this.speed   	= (int) (speed * ENTRUST.MULTIPLIER);
		this.req1Result	= (int) (Math.round(req1Result * ENTRUST.MULTIPLIER));
		this.req2Result	= (int) (Math.round(req2Result * ENTRUST.MULTIPLIER));
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    str.append("Sensor1:" + sensor1 + NEW_LINE);
	    str.append("Sensor2:" + sensor2 + NEW_LINE);
	    str.append("Sensor3:" + sensor3 + NEW_LINE);
	    str.append("Speed:" + speed + NEW_LINE);
	    str.append("Req1Result:" + req1Result + NEW_LINE);
	    str.append("Req2Result:" + req2Result + NEW_LINE);
		return str.toString();
	}
	
	public int getSensor1(){
		return sensor1;
	}
	
	public int getSensor2(){
		return sensor2;
	}
	
	public int getSensor3(){
		return sensor3;
	}

	public int getSpeed(){
		return speed;
	}
	
	public int getReq1Result(){
		return req1Result;
	}
	
	public int getReq2Result(){
		return req2Result;
	}



}