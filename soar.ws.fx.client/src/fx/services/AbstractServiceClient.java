package fx.services;

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
	
	public String toString(){
		return "[" + id +"$ r:"+ nominalReliability +", t:"+ timePerInvocation +", c:" + costPerInvocation + "]"; 
	}
	
	
	public String getReliabilityAsString(){
		return "(" + timesSucceeded +"/"+ timesInvoked +")";
	}
	
	
	public double getReliability(){
		this.actualReliability = timesSucceeded / (timesInvoked + 0.0);
		return this.actualReliability;
	}
	
	
	public double getCostPerInvocation(){
		return this.costPerInvocation;
	}
	
	
	public double timePerInvocation(){
		return this.timePerInvocation;
	}
	
	
	public String getID(){
		return this.id;
	}
	
	public abstract void execute();
}
