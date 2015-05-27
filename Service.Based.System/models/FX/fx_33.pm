//FOREX dtmc model (Simplified: 4 operations with 3 services for each operation)
dtmc

//Evoxxx defined params
//Which services are enabled
evolve const int op1Code [1..7]; //possible combinations for services implementing operation 1
evolve const int op2Code [1..7]; //possible combinations for services implementing operation 2
evolve const int op4Code [1..7]; //possible combinations for services implementing operation 4

//Sequence of service execution
evolve const int seqOp1  [1..6]; //(#services op1)!
evolve const int seqOp2  [1..6]; //(#services op2)!
evolve const int seqOp4  [1..6]; //(#services op3)!

//distribution for probabilistic selection
evolve distribution probOp1 [3];
evolve distribution probOp2 [3];
evolve distribution probOp4 [3];


//flag indicating whether a service is selected or not (will be assembled based on the chromosome value)
const int op1S1 = mod(op1Code,2)>0?1:0;
const int op1S2 = mod(op1Code,4)>1?1:0;
const int op1S3 = mod(op1Code,8)>3?1:0;
const int op2S1 = mod(op2Code,2)>0?1:0;
const int op2S2 = mod(op2Code,4)>1?1:0;
const int op2S3 = mod(op2Code,8)>3?1:0;
const int op4S1 = mod(op4Code,2)>0?1:0;
const int op4S2 = mod(op4Code,4)>1?1:0;
const int op4S3 = mod(op4Code,8)>3?1:0;

//user-define params parameters
const double op1S1Fail=0.05; //failure probability of service 1 op1
const double op1S2Fail=0.06; //failure probability of service 2 op1
const double op1S3Fail=0.13; //failure probability of service 3 op1

const double op2S1Fail=0.09; //failure probability of service 1 op1
const double op2S2Fail=0.14; //failure probability of service 2 op1
const double op2S3Fail=0.03; //failure probability of service 3 op1

const double op4S1Fail=0.12; //failure probability of service 1 op1
const double op4S2Fail=0.08; //failure probability of service 2 op1
const double op4S3Fail=0.05; //failure probability of service 3 op1

const int STEPMAX  = 4;

/////////////
//Workflow
/////////////
module forex
	//local state
	state : [0..15] init 0;
	//Init
	[fxStart]	state = 0 	->	0.66 : (state'=1) + 0.34 : (state'=9);

	//Op1: Market watch
	[startOp1]		state = 1	->	1.0  : (state'=2);  	//invoke op1
	[endOp1Fail]	state = 2 	->	1.0  : (state'=5);	//failed op1
	[endOp1Succ] 	state = 2	->	1.0  : (state'=3) ;  	//succ   op1

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


/////////////
//Operation 1: Market watch
/////////////

//SEQ
evolve module strategyOp1
	operation1 : [0..6] init 0;
	stepOp1	 : [1..4] init 1;

	[startOp1]	operation1 = 0		-> 1.0 : (operation1'=1);

	[check11]	operation1 = 1 & stepOp1 = 1	-> ((seqOp1=1 | seqOp1=2)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=3 | seqOp1=4)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=5 | seqOp1=6)? 1 : 0) : (operation1'=4) ;

	[check12]	operation1 = 1 & stepOp1 = 2	-> ((seqOp1=3 | seqOp1=5)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=1 | seqOp1=6)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=2 | seqOp1=4)? 1 : 0) : (operation1'=4) ;

	[check13]	operation1 = 1 & stepOp1 = 3	-> ((seqOp1=4 | seqOp1=6)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=2 | seqOp1=5)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=1 | seqOp1=3)? 1 : 0) : (operation1'=4) ;
	[check14]	operation1 = 1 & stepOp1 > 3 -> 1.0 : (operation1'=5);

	[runS11]	operation1 = 2	-> (op1S1=1?1.0:0)*(1-op1S1Fail) : (operation1'=6) + (op1S1=0?1.0:op1S1Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));
	[runS12]	operation1 = 3	-> (op1S2=1?1.0:0)*(1-op1S2Fail) : (operation1'=6) + (op1S2=0?1.0:op1S2Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));
	[runS13]	operation1 = 4	-> (op1S3=1?1.0:0)*(1-op1S3Fail) : (operation1'=6) + (op1S3=0?1.0:op1S3Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));	

	[endOp1Fail]	operation1 = 5	->	1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 6	->	1.0 : (operation1'=0); //succ
endmodule


//PAR
evolve module strategyOp1
	operation1 : [0..8] init 0;

	[startOp1]	operation1 = 0		-> 1.0 : (operation1'=1); //get into the construct
			
	[checkS11]	operation1 = 1		-> (op1S1) : (operation1'=2) + (1-op1S1) : (operation1'=5);
	[runS11]	operation1 = 2		-> 1-op1S1Fail : (operation1'=8) + op1S1Fail : (operation1'=5);

	[checkS12]	operation1 = 5		-> (op1S2) : (operation1'=3) + (1-op1S2) : (operation1'=6);
	[runS12]	operation1 = 3		-> 1-op1S2Fail : (operation1'=8) + op1S2Fail : (operation1'=6);

	[checkS13]	operation1 = 6		-> (op1S3) : (operation1'=4) + (1-op1S3) : (operation1'=7);
	[runS13]	operation1 = 4		-> 1-op1S3Fail : (operation1'=8) + op1S3Fail : (operation1'=7);

	[endOp1Fail]	operation1 = 7		-> 1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 8		-> 1.0 : (operation1'=0);//succ	
endmodule


//PROB
evolve module strategyOp1
	operation1 : [0..8] init 0;

	//select a service probabilistically
	[startOp1] 	operation1 = 0 	->	probOp1 : (operation1'=1) + probOp1 : (operation1'=5) + probOp1 : (operation1'=6); 

	[checkS11]	operation1 = 1	->	(op1S1) : (operation1'=2) + (1-op1S1) : (operation1'=7); //service1 start
	[runS11]     	operation1 = 2  ->	1-op1S1Fail : (operation1'=8) + op1S1Fail : (operation1'=7); 		

	[checkS12]	operation1 = 5	->	(op1S2) : (operation1'=3) + (1-op1S2) : (operation1'=7); //service1 start
	[runS12]     	operation1 = 3  ->	1-op1S2Fail : (operation1'=8) + op1S2Fail : (operation1'=7); 		

	[checkS13]	operation1 = 6	->	(op1S3) : (operation1'=4) + (1-op1S3) : (operation1'=7); //service1 start
	[runS13]     	operation1 = 4  ->	1-op1S3Fail : (operation1'=8) + op1S3Fail : (operation1'=7); 		

	[endOp1Fail]	operation1 = 7	->	1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 8	->	1.0 : (operation1'=0); //succ
endmodule


/////////////
//Operation 2: Technical Analysis
/////////////


//SEQ
evolve module strategyOp2
	operation2 : [0..6] init 0;
	stepOp2	 : [1..4] init 1;

	[startOp2]	operation2 = 0			-> 1.0 : (operation2'=1);

	[check21]	operation2 = 1 & stepOp2 = 1	-> ((seqOp2=1 | seqOp2=2)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=3 | seqOp2=4)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=5 | seqOp2=6)? 1 : 0) : (operation2'=4) ;

	[check22]	operation2 = 1 & stepOp2 = 2	-> ((seqOp2=3 | seqOp2=5)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=1 | seqOp2=6)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=2 | seqOp2=4)? 1 : 0) : (operation2'=4) ;

	[check23]	operation2 = 1 & stepOp2 = 3	-> ((seqOp2=4 | seqOp2=6)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=2 | seqOp2=5)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=1 | seqOp2=3)? 1 : 0) : (operation2'=4) ;
	[check24]	operation2 = 1 & stepOp2 > 3 -> 1.0 : (operation2'=5);

	[runS21]	operation2 = 2		-> (op2S1=1?1.0:0)*(1-op2S1Fail) : (operation2'=6) + (op2S1=0?1.0:op2S1Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS22]	operation2 = 3		-> (op2S2=1?1.0:0)*(1-op2S2Fail) : (operation2'=6) + (op2S2=0?1.0:op2S2Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS23]	operation2 = 4		-> (op2S3=1?1.0:0)*(1-op2S3Fail) : (operation2'=6) + (op2S3=0?1.0:op2S3Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));	

	[endOp2Fail]	operation2 = 5		->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 6		->	1.0 : (operation2'=0); //succ
endmodule


//PAR
evolve module strategyOp2
	operation2 : [0..8] init 0;

	[startOp2]	operation2 = 0		-> 1.0 : (operation2'=1); //get into the construct
			
	[checkS21]	operation2 = 1		-> (op2S1) : (operation2'=2) + (1-op2S1) : (operation2'=5);
	[runS21]	operation2 = 2		-> 1-op2S1Fail : (operation2'=8) + op2S1Fail : (operation2'=5);

	[checkS22]	operation2 = 5		-> (op2S2) : (operation2'=3) + (1-op2S2) : (operation2'=6);
	[runS22]	operation2 = 3		-> 1-op2S2Fail : (operation2'=8) + op2S2Fail : (operation2'=6);

	[checkS23]	operation2 = 6		-> (op2S3) : (operation2'=4) + (1-op2S3) : (operation2'=7);
	[runS23]	operation2 = 4		-> 1-op2S3Fail : (operation2'=8) + op2S3Fail : (operation2'=7);

	[endOp2Fail]	operation2 = 7		-> 1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 8		-> 1.0 : (operation2'=0);//succ	
endmodule


//PROB
evolve module strategyOp2 
	operation2 : [0..8] init 0;

	//select a service probabilistically
	[startOp2] 	operation2 = 0 	->	probOp2 : (operation2'=1) + probOp2 : (operation2'=5) + probOp2 : (operation2'=6); 

	[checkS21]	operation2 = 1	->	(op2S1) : (operation2'=2) + (1-op2S1) : (operation2'=7); //service1 start
	[runS21]     	operation2 = 2  ->	1-op2S1Fail : (operation2'=8) + op2S1Fail : (operation2'=7); 		

	[checkS22]	operation2 = 5	->	(op2S2) : (operation2'=3) + (1-op2S2) : (operation2'=7); //service1 start
	[runS22]     	operation2 = 3  ->	1-op2S2Fail : (operation2'=8) + op2S2Fail : (operation2'=7); 		

	[checkS23]	operation2 = 6	->	(op2S3) : (operation2'=4) + (1-op2S3) : (operation2'=7); //service1 start
	[runS23]     	operation2 = 4  ->	1-op2S3Fail : (operation2'=8) + op2S3Fail : (operation2'=7); 		

	[endOp2Fail]	operation2 = 7	->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 8	->	1.0 : (operation2'=0); //succ
endmodule



/////////////
//Operation 3: Alarm
/////////////

//PROB
module strategyOp3
	operation3 : [0..2] init 0;

	//select a service probabilistically
	[startOp3] 	operation3 = 0 	->	0.0001 : (operation3'=1) + 0.9999 : (operation3'=2);

	[endOp3Fail]	operation3 = 1	->	1.0 : (operation3'=0);//failed
	[endOp3Succ]	operation3 = 2	->	1.0 : (operation3'=0); //succ
endmodule





/////////////
//Operation 4: Fundamental Analysis
/////////////


//SEQ
evolve module strategyOp4
	operation4 : [0..6] init 0;
	stepOp4	 : [1..4] init 1;

	[startOp4]	operation4 = 0			-> 1.0 : (operation4'=1);

	[check41]	operation4 = 1 & stepOp4 = 1	-> ((seqOp4=1 | seqOp4=2)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=3 | seqOp4=4)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=5 | seqOp4=6)? 1 : 0) : (operation4'=4) ;

	[check42]	operation4 = 1 & stepOp4 = 2	-> ((seqOp4=3 | seqOp4=5)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=1 | seqOp4=6)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=2 | seqOp4=4)? 1 : 0) : (operation4'=4) ;

	[check43]	operation4 = 1 & stepOp4 = 3	-> ((seqOp4=4 | seqOp4=6)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=2 | seqOp4=5)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=1 | seqOp4=3)? 1 : 0) : (operation4'=4) ;
	[check44]	operation4 = 1 & stepOp4 > 3 -> 1.0 : (operation4'=5);

	[runS41]	operation4 = 2		-> (op4S1=1?1.0:0)*(1-op4S1Fail) : (operation4'=6) + (op4S1=0?1.0:op4S1Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));
	[runS42]	operation4 = 3		-> (op4S2=1?1.0:0)*(1-op4S2Fail) : (operation4'=6) + (op4S2=0?1.0:op4S2Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));
	[runS43]	operation4 = 4		-> (op4S3=1?1.0:0)*(1-op4S3Fail) : (operation4'=6) + (op4S3=0?1.0:op4S3Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));	

	[endOp4Fail]	operation4 = 5		->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 6		->	1.0 : (operation4'=0); //succ
endmodule


//PAR
evolve module strategyOp4
	operation4 : [0..8] init 0;

	[startOp4]	operation4 = 0		-> 1.0 : (operation4'=1); //get into the construct
			
	[checkS41]	operation4 = 1		-> (op4S1) : (operation4'=2) + (1-op4S1) : (operation4'=5);
	[runS41]	operation4 = 2		-> 1-op4S1Fail : (operation4'=8) + op4S1Fail : (operation4'=5);

	[checkS42]	operation4 = 5		-> (op4S2) : (operation4'=3) + (1-op4S2) : (operation4'=6);
	[runS42]	operation4 = 3		-> 1-op4S2Fail : (operation4'=8) + op4S2Fail : (operation4'=6);

	[checkS43]	operation4 = 6		-> (op4S3) : (operation4'=4) + (1-op4S3) : (operation4'=7);
	[runS43]	operation4 = 4		-> 1-op4S3Fail : (operation4'=8) + op4S3Fail : (operation4'=7);

	[endOp4Fail]	operation4 = 7		-> 1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 8		-> 1.0 : (operation4'=0);//succ	
endmodule


//PROB
evolve module strategyOp4
	operation4 : [0..8] init 0;

	//select a service probabilistically
	[startOp4] 	operation4 = 0 	->	probOp4 : (operation4'=1) + probOp4 : (operation4'=5) + probOp4 : (operation4'=6); 

	[checkS41]	operation4 = 1	->	(op4S1) : (operation4'=2) + (1-op4S1) : (operation4'=7); //service1 start
	[runS41]     	operation4 = 2  ->	1-op4S1Fail : (operation4'=8) + op4S1Fail : (operation4'=7); 		

	[checkS42]	operation4 = 5	->	(op4S2) : (operation4'=3) + (1-op4S2) : (operation4'=7); //service1 start
	[runS42]     	operation4 = 3  ->	1-op4S2Fail : (operation4'=8) + op4S2Fail : (operation4'=7); 		

	[checkS43]	operation4 = 6	->	(op4S3) : (operation4'=4) + (1-op4S3) : (operation4'=7); //service1 start
	[runS43]     	operation4 = 4  ->	1-op4S3Fail : (operation4'=8) + op4S3Fail : (operation4'=7); 		

	[endOp4Fail]	operation4 = 7	->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 8	->	1.0 : (operation4'=0); //succ
endmodule






/////////////
//Operation 5: Make order 
/////////////

//PROB
module strategyOp5
	operation5 : [0..2] init 0;

	//select a service probabilistically
	[startOp5] 		operation5 = 0 	->	0.0001 : (operation5'=1) + 0.9999 : (operation5'=2); 

	[endOp5Fail]	operation5 = 1	->	1.0 : (operation5'=0);//failed
	[endOp5Succ]	operation5 = 2	->	1.0 : (operation5'=0); //succ
endmodule



/////////////
//Operation 6: Notify trader
/////////////

//PROB
module strategyOp6
	operation6 : [0..2] init 0;

	//select a service probabilistically
	[startOp6] 		operation6 = 0 	->	0.0001 : (operation6'=1) + 0.9999 : (operation6'=2); 

	[endOp6Fail]	operation6 = 1	->	1.0 : (operation6'=0);//failed
	[endOp6Succ]	operation6 = 2	->	1.0 : (operation6'=0); //succ
endmodule




////////////
//Rewards
////////////
rewards "time"
	//OP1
	operation1 = 2 : 1.0 *op1S1;
	operation1 = 3 : 1.3 *op1S2;
	operation1 = 4 : 1.5 *op1S3;
	//OP2
	operation2 = 2 : 1.8 *op2S1;
	operation2 = 3 : 2.2 *op2S2;
	operation2 = 4 : 2.7 *op2S3;
	//OP3
	operation3 = 1 | operation3 = 2 : 1.5 ;
	//OP4
	operation4 = 2 : 4.5  *op4S1;
	operation4 = 3 : 4.6  *op4S2;
	operation4 = 4 : 5.4  *op4S3;
	//OP5
	operation5 = 1 | operation5 = 2 : 3.0 ;
	//OP3
	operation6 = 1 | operation6 = 2 : 2.5 ;
endrewards

rewards "cost"
	//OP1: SEQ OR PROB cost
	operation1 = 2 & (STRATEGYOP1>1) : 20 *op1S1;
	operation1 = 3 & (STRATEGYOP1>1) : 15 *op1S2;
	operation1 = 4 & (STRATEGYOP1>1) : 10 *op1S3;
	//OP1: PAR cost
	operation1 = 1 & STRATEGYOP1=1 : 20 * op1S1 + 15 * op1S2 + 10 * op1S3;
	
	//OP2: SEQ OR PROB cost
	operation2 = 2 & (STRATEGYOP2>1) : 10 *op2S1;
	operation2 = 3 & (STRATEGYOP2>1) : 05 *op2S2;
	operation2 = 4 & (STRATEGYOP2>1) : 15 *op2S3;
	//OP2: PAR cost
	operation2 = 1 & STRATEGYOP2=1 : 10 * op2S1 + 5 * op2S2 + 15 * op2S3;

	//OP3: 
	operation3 = 1 | operation3 = 2: 17;

	//OP4: SEQ OR PROB cost
	operation4 = 2 & (STRATEGYOP4>1) : 15 *op4S1;
	operation4 = 3 & (STRATEGYOP4>1) : 25 *op4S2;
	operation4 = 4 & (STRATEGYOP4>1) : 35 *op4S3;
	//OP4: PAR cost
	operation4 = 1 & STRATEGYOP4=1 : 15 * op4S1 + 25 * op4S2 + 35 * op4S3;

	//OP5:
	operation5 = 1 | operation5 = 2  : 30;

	//OP6:
	operation6 = 1 | operation6 = 2  : 11;

endrewards
