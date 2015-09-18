//FOREX dtmc model (6 operations with 4 services for each operation)
dtmc

//services reliability
// const double op1Fail=0.011; //failure probability of service for op1
// const double op1Time=5;
// const double op1Cost=10;

// const double op2Fail=0.004; //failure probability of service for op2
// const double op2Time=5;
// const double op2Cost=10;

// const double op3Fail=0.007; //failure probability of service for op3
// const double op3Time=5;
// const double op3Cost=10;

// const double op4Fail=0.002; //failure probability of service for op4
// const double op4Time=5;
// const double op4Cost=10;

// const double op5Fail=0.002; //failure probability of service for op4
// const double op5Time=5;
// const double op5Cost=10;

// const double op6Fail=0.002; //failure probability of service for op4
// const double op6Time=5;
// const double op6Cost=10;


/////////////////////////////////
//Workflow
/////////////////////////////////
module forex
	//local state
	state : [0..15] init 0;
	//Init
	[fxStart]	state = 0 	->	0.66 : (state'=1) + 0.34 : (state'=9);

	//Op1: Market watch
	[startOp1]		state = 1	->	1.0  : (state'=2);  //invoke op1
	[endOp1Fail]	state = 2 	->	1.0  : (state'=5);	//failed op1
	[endOp1Succ] 	state = 2	->	1.0  : (state'=3) ; //succ   op1

	//Op2: Technical Analysis
	[startOp2]		state = 3	->	1.0  : (state'=4);	//invoke op2
	[endOp2Fail]	state = 4	->	1.0  : (state'=5);	//failed op2
	[endOp2Succ]	state = 4	->	1.0  : (state'=6);	//succ   op2

	//Technical analysis result
	[taResult]		state=6 	->	0.61 : (state'=1) + 0.28 : (state'=11) + 0.11 : (state'=7);

	//Op3: Alarm
	[startOp3]		state=7		->	1.0  : (state'=8);
	[endOp3Fail]	state=8		->	1.0  : (state'=5);
	[endOp3Succ]	state=8		->	1.0  : (state'=13);

	//Op4: Fundamental Analysis
	[startOp4]		state=9		-> 	1.0  : (state'=10);
	[endOp4Fail]	state=10	->	1.0  : (state'=5);
	[endOp4Succ]	state=10	->	0.53  : (state'=0) + 0.27 : (state'=11) + 0.20 : (state'=9);

	//Op5: Place Order
	[startOp5]		state=11	->	1.0  : (state'=12);
	[endOp5Fail]	state=12	->	1.0  : (state'=5);
	[endOp5Succ]	state=12	->	1.0  : (state'=13);

	//Op6: Notify trader
	[startOp6]		state=13	->	1.0  : (state'=14);
	[endOp6Fail]	state=14	->	1.0  : (state'=5);
	[endOp6Succ]	state=14	->	1.0  : (state'=15);

	//End
	[fxFail]		state = 5	->	1.0  : (state'=5);	//failed fx
	[fxSucc]		state = 15	->	1.0  : (state'=15);	//succ   fx
endmodule



/////////////////////////////////
//Operation 1: Market watch
/////////////////////////////////
module strategyOp1
	operation1 : [0..3] init 0;

	[startOp1]		operation1 = 0 ->	1.0 	: (operation1'=1);
	[runS1]     	operation1 = 1 ->	op1Fail : (operation1'=2) + 1-op1Fail : (operation1'=3); 
	[endOp1Fail]	operation1 = 2	->	1.0 	: (operation1'=0);//failed
	[endOp1Succ]	operation1 = 3	->	1.0 	: (operation1'=0); //succ	
endmodule


/////////////////////////////////
//Operation 2: Technical Analysis
/////////////////////////////////
module strategyOp2
	operation2 : [0..3] init 0;

	[startOp2]		operation2 = 0 ->	1.0 	: (operation2'=1);
	[runS2]     	operation2 = 1 ->	op2Fail : (operation2'=2) + 1-op2Fail : (operation2'=3); 
	[endOp2Fail]	operation2 = 2	->	1.0 	: (operation2'=0);//failed
	[endOp2Succ]	operation2 = 3	->	1.0 	: (operation2'=0); //succ	
endmodule


//////////////////
//Operation 3: Alarm
/////////////////////////////////
module strategyOp3
	operation3 : [0..3] init 0;

	[startOp3]		operation3 = 0 ->	1.0 	: (operation3'=1);
	[runS3]     	operation3 = 1 ->	op3Fail : (operation3'=2) + 1-op3Fail : (operation3'=3); 
	[endOp3Fail]	operation3 = 2	->	1.0 	: (operation3'=0);//failed
	[endOp3Succ]	operation3 = 3	->	1.0 	: (operation3'=0); //succ	
endmodule



/////////////////////////////////
//Operation 4: Fundamental Analysis
/////////////////////////////////
module strategyOp4
	operation4 : [0..3] init 0;

	[startOp4]		operation4 = 0 ->	1.0 	: (operation4'=1);
	[runS4]     	operation4 = 1 ->	op4Fail : (operation4'=2) + 1-op4Fail : (operation4'=3); 
	[endOp4Fail]	operation4 = 2	->	1.0 	: (operation4'=0);//failed
	[endOp4Succ]	operation4 = 3	->	1.0 	: (operation4'=0); //succ	
endmodule

/////////////////////////////////
//Operation 5: Alarm
/////////////////////////////////
module strategyOp5
	operation5 : [0..3] init 0;

	[startOp5]		operation5 = 0 ->	1.0 	: (operation5'=1);
	[runS5]     	operation5 = 1 ->	op5Fail : (operation5'=2) + 1-op5Fail : (operation5'=3); 
	[endOp5Fail]	operation5 = 2	->	1.0 	: (operation5'=0);//failed
	[endOp5Succ]	operation5 = 3	->	1.0 	: (operation5'=0); //succ	
endmodule


/////////////////////////////////
//Operation 6: Notification
/////////////////////////////////
module strategyOp6
	operation6 : [0..3] init 0;

	[startOp6]		operation6 = 0 ->	1.0 	: (operation6'=1);
	[runS6]     	operation6 = 1 ->	op6Fail : (operation6'=2) + 1-op6Fail : (operation6'=3); 
	[endOp6Fail]	operation6 = 2	->	1.0 	: (operation6'=0);//failed
	[endOp6Succ]	operation6 = 3	->	1.0 	: (operation6'=0); //succ	
endmodule


/////////////////////////////////
//Rewards
/////////////////////////////////
rewards "time"
	//OP1: PROB
	operation1 = 1 : op1Time;

	//OP2: PROB
	operation2 = 1 : op2Time;
	
	//OP3: PROB
	operation3 = 1 : op3Time;
	
	//OP4: PROB
	operation4 = 1 : op4Time;

	//OP5: PROB
	operation5 = 1 : op5Time;

	//OP6: PROB
	operation6 = 1 : op6Time;
endrewards


rewards "cost"
	//OP1: PROB
	operation1 = 1 : op1Cost;

	//OP2: PROB
	operation2 = 1 : op2Cost;
	
	//OP3: PROB
	operation3 = 1 : op3Cost;
	
	//OP4: PROB
	operation4 = 1 : op4Cost;

	//OP5: PROB
	operation5 = 1 : op5Cost;

	//OP6: PROB
	operation6 = 1 : op6Cost;
endrewards


//System parameters (as appended programmatically)

