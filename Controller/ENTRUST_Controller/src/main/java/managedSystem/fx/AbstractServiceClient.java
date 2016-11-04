package managedSystem.fx;

public abstract class AbstractServiceClient {

	/** service nominal reliability*/
	private double nominalReliability;

	/** service actual reliability*/
	private double actualReliability;
	
	/** cost per invocation*/
	private double costPerInvocation;
	
	/** time required to carry out its functionality per invocation*/
	private double timePerInvocation;
	
	/** Service ID*/
	private String id;
	
	/** number of times invoked */
	protected int timesInvoked;
	
	/** number of times the service replied (within the advertised response time) */
	protected int timesSucceeded;

	/** how long the service is idle */
	private final int MAX_IDLE = 5;
	private int idle = MAX_IDLE;
	
	
	/**
	 * Constructor: A new object that represents a specific client for a given service
	 * @param ID
	 * @param reliability
	 * @param cost
	 * @param responseTime
	 */
	public AbstractServiceClient(String ID, double reliability, double cost, double responseTime){
		this.nominalReliability = reliability;
		this.actualReliability	= reliability;
		this.costPerInvocation	= cost;
		this.timePerInvocation	= responseTime;
		this.id					= ID;
		this.timesInvoked		= 0;
		this.timesSucceeded		= 0;
	}
	
	
	/** Returns the features of this service as a string*/
	@Override
	public String toString(){
		return "[" + id +"$ r:"+ nominalReliability +", t:"+ timePerInvocation +", c:" + costPerInvocation + "]"; 
	}
	
	
	/** Returns the service reliability as a string */
	public String getReliabilityAsString(){
//		return "(" + timesSucceeded +"/"+ timesInvoked +")";
		return "("+ this.actualReliability +")"; 
	}
	
	/** Returns the service reliability */
	public double getReliability(){
		return this.actualReliability; 
	}
	
	
	/** Returns the service reliability (actually it calculates the reliability first) */
	public void calculateReliability(){
		if (timesInvoked==0)
			if (idle==MAX_IDLE){
				idle = 0;
				actualReliability = this.nominalReliability;
			}
			else{
				idle++;
			}
		else{
			this.actualReliability = timesSucceeded / (timesInvoked + 0.0);
			//reset
//			if ((actualReliability * 1000)==1000)
//				System.out.println(toString() +"\t"+ timesSucceeded +"/"+ timesInvoked);
			timesInvoked 	= 0;
			timesSucceeded	= 0;
		}
	}
	
	public void setReliability(double newReliability){
		actualReliability = newReliability;
	}
	
	public void setCost(double cost){
		costPerInvocation = cost;
	}
	
	public void setResponseTime(double responseTime){
		timePerInvocation = responseTime;
	}
	
	/** Returns service cost per invocation */
	public double getCostPerInvocation(){
		return this.costPerInvocation;
	}
	
	
	/** Returns service response time */
	public double timePerInvocation(){
		return this.timePerInvocation;
	}
	
	
	/** Returns service ID */
	public String getID(){
		return this.id;
	}
	
	
	/** Returns service features as a double array */
	public double[] getFeatures(){
		return new double[]{this.actualReliability, this.costPerInvocation, this.timePerInvocation};
	}
	
	
	/** Returns all service characteristics as an Object array
	 * [0]:ID; [1]:reliability; [2]:cost; [3]:time */
	public Object[] getServiceCharacteristics(){
		return new Object[]{this.id, this.nominalReliability, this.costPerInvocation, this.timePerInvocation};
	}
	
	
	/** Main service client function that invokes the service*/
	public abstract void execute();
}
