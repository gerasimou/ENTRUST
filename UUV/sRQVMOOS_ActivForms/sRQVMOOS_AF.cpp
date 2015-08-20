//==========================================================================================//
//																						 	//
//	Author:                                                         						//
//	- Simos Gerasimou <simos@cs.york.ac.uk> (University of York)	     					//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Notes:                                                          						//
//																							//
//------------------------------------------------------------------------------------------//
//																							//
//	Remarks:                                                        						//
//																							//
//																							//
//==========================================================================================//

#include <iterator>
#include "MBUtils.h"
#include "sRQVMOOS_AF.h"


using namespace std;

//---------------------------------------------------------
// Constructor
//---------------------------------------------------------
RQVMOOS::RQVMOOS(): FIXED_DISTANCE(10), MIN_SPEED(0.1), ARRAY_SIZE(147), CSLPROPPERTIES(2),
					  SENSOR_1_OPERATING_COST(15), SENSOR_2_OPERATING_COST(10), SENSOR_3_OPERATING_COST(5)
{
  m_iterations = 0;
  m_timewarp   = 1;

  //Variables related to distance covered by the AUV
  m_first_reading		= false;
  m_current_x			= 0;
  m_current_y			= 0;
  m_previous_x			= 0;
  m_previous_y			= 0;
  m_current_distance	= 0;
  m_total_distance		= 0;

  m_maximum_power_consumption_per_iteration			= 0; //

  m_uuv_speed							= 0; // Current UUV speed
  m_uuv_speed_threshold					= 3.8;
  m_uuv_speed_maximum 					= 4.0;

  m_previous_sensor_configuration 		= 0;
  m_current_sensor_configuration 		= -1;
  M_COOLING_OFF_PERIOD					= 10;

  m_desired_sensors_configuration		= -1;
  m_desired_uuv_speed					= -1;
  m_desired_configuration_cost			= -1;
  m_desired_results_index				= -1;

  m_msgs.precision(5);
  sensor1_threshold = 3.5;
  sensor2_threshold = 3.0;
  sensor3_threshold = 2.5;

  m_active_behaviour = "";

  MIN_SUCC_READINGS	  		= 5;
  MAX_POWER_CONSUMPTION		= 200;

  m_previous_iterate_call   = MOOSTime(true) + 10;//GetAppStartTime();
  m_current_iterate_call    = MOOSTime(true);

  m_resultRQV = "";

    tempVariable = -1;
}



//---------------------------------------------------------
// Destructor
//---------------------------------------------------------
RQVMOOS::~RQVMOOS()
{

}



//---------------------------------------------------------
// Procedure: OnConnectToServer
//---------------------------------------------------------
bool RQVMOOS::OnConnectToServer()
{
   // register for variables here
   // possibly look at the mission file?
   // m_MissionReader.GetConfigurationParam("Name", <string>);
   // m_Comms.Register("VARNAME", 0);

   RegisterVariables();
   return(true);
}



//---------------------------------------------------------
// Procedure: OnStartUp()
//            happens before connection is open
//---------------------------------------------------------
bool RQVMOOS::OnStartUp()
{
	AppCastingMOOSApp::OnStartUp();

	list<string> sParams;
	m_MissionReader.EnableVerbatimQuoting(false);

	if(m_MissionReader.GetConfiguration(GetAppName(), sParams)) {
		list<string>::iterator p;
		for(p=sParams.begin(); p!=sParams.end(); p++) {
			string original_line = *p;
			string param = stripBlankEnds(toupper(biteString(*p, '=')));
			string value = stripBlankEnds(*p);

			  if(param == "SPEED_THRESHOLD") { // assign value to speed threshold variable
				  m_uuv_speed_threshold = atof(value.c_str());
			  }
			  else if(param == "SPEED_MAXIMUM") { // assign value to maximum speed variable
				  m_uuv_speed_maximum = atof(value.c_str());
			  }
			  else if (param == "COOLING_OFF_PERIOD"){
				  M_COOLING_OFF_PERIOD = atoi(value.c_str());
			  }
			  else if (param == "MIN_SUCC_READINGS"){
				 MIN_SUCC_READINGS = strtod(value.c_str(), NULL);
			  }
			  else if (param == "MAX_POWER_CONSUMPTION"){
				  MAX_POWER_CONSUMPTION = strtod(value.c_str(), NULL);
			  }
			  //ToDO: Add variables for CSL properties thresholds (i.e., successfull readings and power consumption)
		}
  }

  //Initialise JNIWrapper: createJVM, find class, method etc;
	initialiseClient();

  m_timewarp = GetMOOSTimeWarp();

  RegisterVariables();
  return(true);
}



//---------------------------------------------------------
// Procedure: RegisterVariables
//---------------------------------------------------------
void RQVMOOS::RegisterVariables()
{
  AppCastingMOOSApp::RegisterVariables();
  m_Comms.Register("NAV_X", 1); // (<name of MOOS variable>, <minimum interval time between notifications>)
  m_Comms.Register("NAV_Y", 1);
  m_Comms.Register("NAV_SPEED", 1);
  m_Comms.Register("SONAR_SENSOR_SONAR1",0);
  m_Comms.Register("SONAR_SENSOR_SONAR2",0);
  m_Comms.Register("SONAR_SENSOR_SONAR3",0);
  m_Comms.Register("ACTIVE_BEHAVIOUR",0);
}



//---------------------------------------------------------
// Procedure: OnNewMail
//---------------------------------------------------------
bool RQVMOOS::OnNewMail(MOOSMSG_LIST &NewMail)
{
  AppCastingMOOSApp::OnNewMail(NewMail);

  MOOSMSG_LIST::iterator p;

  for(p=NewMail.begin(); p!=NewMail.end(); p++) {
	    CMOOSMsg &msg = *p;

	#if 0 // Keep these around just for template
	    string key   = msg.GetKey();
	    string comm  = msg.GetCommunity();
	    double dval  = msg.GetDouble();
	    string sval  = msg.GetString();
	    string msrc  = msg.GetSource();
	    double mtime = msg.GetTime();
	    bool   mdbl  = msg.IsDouble();
	    bool   mstr  = msg.IsString();
	#endif

	    string key 	= msg.GetKey();
		double time	= msg.GetTime();

		if (key == "NAV_X"){
//			m_previous_x = m_current_x;
			m_current_x  = msg.GetDouble();
		}
		else if (key == "NAV_Y"){
//			m_previous_y = m_current_y;
			m_current_y  = msg.GetDouble();
		}
		else if (key == "NAV_SPEED"){
			m_uuv_speed = msg.GetDouble();
		}
		else if (std::string::npos != key.find("SONAR_SENSOR")){
			vector<string> svector = parseString(msg.GetString(),","); // svector[0] = "name=sonar", svector[1] = "reading_rate=5"
			vector<string> vparams;
			for (vector<string>::const_iterator iterator=svector.begin(); iterator!=svector.end(); iterator++){
				vparams.push_back((parseString(*iterator, "="))[1]);
			}
			appendToSensorsMap(vparams);
		}
		else if (key == "ACTIVE_BEHAVIOUR"){
			m_active_behaviour = msg.GetString();
		}
  }
  return(true);
}



//---------------------------------------------------------
// Procedure: Iterate()
//            happens AppTick times per second
//---------------------------------------------------------
bool RQVMOOS::Iterate() {

    static bool firstTimeIterate = true;

    AppCastingMOOSApp::Iterate();
    m_iterations++;

    if (firstTimeIterate){
        resetSensorsAverageReadingRate();
        m_previous_iterate_call = MOOSTime(true);
        firstTimeIterate = false;
        return true;
    }

    if (m_current_iterate_call-m_previous_iterate_call > M_COOLING_OFF_PERIOD){

        //perform RQV
	    double timeBefore = MOOSTime(true);
	    invokeManagingSystem();
	    updateSensorsState();
	    double timeAfter = MOOSTime(true);

	    //Create and write log data
	    string logData 	= createLogData(timeAfter-timeBefore);
	    logToFile(logData);

	    //send notifications
        sendNotifications(timeAfter-timeBefore);

	    //Reset Sensors reading rates after iteration
	    resetSensorsAverageReadingRate();

	    m_previous_iterate_call = m_current_iterate_call;
    }

    m_current_iterate_call = MOOSTime(true);

    AppCastingMOOSApp::PostReport();
    return(true);
}




//---------------------------------------------------------
// Procedure: sensdNotifications()
//            happens AppTick times per second
//---------------------------------------------------------
void RQVMOOS::sendNotifications(double looptime){

    //Publish results
    Notify("TOTAL_DISTANCE", m_total_distance);
    Notify("LOOP_TIME", looptime);
    Notify("DESIRED_SENSOR_CONFIGURATION", m_desired_sensors_configuration+1);
    Notify("DESIRED_UUV_SPEED", m_desired_uuv_speed);
    Notify("DESIRED_CONFIGURATION_COST", m_desired_configuration_cost);
    Notify("M_ITERATIONS", m_iterations);

    string sensorsRates;
    double timeStamp = m_current_iterate_call - m_previous_iterate_call;
    sensorsRates =doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR1")->second.readingRateTimes / timeStamp) +" - "+
                doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR2")->second.readingRateTimes / timeStamp) +" - "+
                doubleToString(m_sensor_map.find("SONAR_SENSOR_SONAR3")->second.readingRateTimes / timeStamp);
    Notify("SENSORS_RATES", sensorsRates);




    //Visualation in pMarineViewer

    //Sensor1
    Sensor sensor1 = m_sensor_map["SONAR_SENSOR_SONAR1"];
    int sensor1NewState = sensor1.newState;
    string sensor1color = "";
    if (sensor1NewState == -1){
        sensor1color = "red";
    }
    else if (sensor1NewState == 0){
        sensor1color = "white";
    }
    else if (sensor1NewState == 1){
        sensor1color = "orange";
    }
    else if (sensor1NewState == 2){
        sensor1color = "green";
    }
    Notify("VIEW_MARKER", "type=circle,x=25,y=-50,scale=2,label=Sensor_1,color=" + sensor1color + ",width=12");
    Notify("VIEW_MARKER", "type=circle,x=25,y=-65,scale=2,msg=r1:" + doubleToString(sensor1.readingRateAvg).substr(0,5) +",label=r1,color=darkblue,width=1");


    //Sensor2
    Sensor sensor2 	  = m_sensor_map["SONAR_SENSOR_SONAR2"];
    int sensor2NewState = sensor2.newState;
    string sensor2color = "";
    if (sensor2NewState == -1){
        sensor2color = "red";
    }
    else if (sensor2NewState == 0){
        sensor2color = "white";
    }
    else if (sensor2NewState == 1){
        sensor2color = "orange";
    }
    else if (sensor2NewState == 2){
        sensor2color = "green";
    }
    Notify("VIEW_MARKER", "type=circle,x=75,y=-50,scale=2,label=Sensor_2,color=" + sensor2color + ",width=12");
    Notify("VIEW_MARKER", "type=circle,x=75,y=-65,scale=2,msg=r2:" + doubleToString(sensor2.readingRateAvg).substr(0,5) +",label=r2,color=darkblue,width=1");


	//Sensor3
    Sensor sensor3 	  =  m_sensor_map["SONAR_SENSOR_SONAR3"];
    int sensor3NewState =sensor3.newState;
    string sensor3color = "";
    if (sensor3NewState == -1){
        sensor3color = "red";
    }
    else if (sensor3NewState == 0){
        sensor3color = "white";
    }
    else if (sensor3NewState == 1){
        sensor3color = "orange";
    }
    else if (sensor3NewState == 2){
        sensor3color = "green";
    }
    Notify("VIEW_MARKER", "type=circle,x=125,y=-50,scale=2,label=Sensor_3,color=" + sensor3color + ",width=12");
    Notify("VIEW_MARKER", "type=circle,x=125,y=-65,scale=2,msg=r3:" + doubleToString(sensor3.readingRateAvg).substr(0,5) +",label=r3,color=darkblue,width=1");


    //speed
    Notify("UPDATES_BHV_CONSTANT_SPEED", "speed="+doubleToString(m_desired_uuv_speed));
    Notify("VIEW_MARKER", "type=circle,x=75,y=-80,scale=2,msg=Speed:" + doubleToString(m_desired_uuv_speed).substr(0,4) +",label=speed,color=darkblue,width=1");

}



//------------------------------------------------------
// Procedure: build report
//------------------------------------------------------
bool RQVMOOS::buildReport()
{
	Sensor sensor1= m_sensor_map["SONAR_SENSOR_SONAR1"];
	Sensor sensor2= m_sensor_map["SONAR_SENSOR_SONAR2"];
	Sensor sensor3= m_sensor_map["SONAR_SENSOR_SONAR3"];

	m_msgs << "Sensors Data: " << endl;
	m_msgs << "Name\tRate\tS.Prob.\tAVGRate\t\tC.State\tN.State\tWait/Fail\n";
	m_msgs << "S1)\t"<< sensor1.readingRate <<"\t"<< "\t"<< sensor1.readingRateAvg <<"\t"<< (sensor1.readingRateAvg<5*0.75 ? "Down!" : "") <<"\t"<< sensor1.currentState <<"\t"<< sensor1.newState <<"\t"<< sensor1.waitFailedTimes <<endl;
	m_msgs << "S2)\t"<< sensor2.readingRate <<"\t"<< "\t"<< sensor2.readingRateAvg <<"\t"<< (sensor2.readingRateAvg<4*0.75 ? "Down!" : "") <<"\t"<< sensor2.currentState <<"\t"<< sensor2.newState <<"\t"<< sensor2.waitFailedTimes <<endl ;
	m_msgs << "S3)\t"<< sensor3.readingRate <<"\t"<< "\t"<< sensor3.readingRateAvg <<"\t"<< (sensor3.readingRateAvg<4*0.75 ? "Down!" : "") <<"\t"<< sensor3.currentState <<"\t"<< sensor3.newState <<"\t"<< sensor3.waitFailedTimes <<endl;

	m_msgs << "\nSensors Configuration" << endl;
	m_msgs << "Previous Sensors Configuration:\t" << m_previous_sensor_configuration << endl ;
	m_msgs << "Current Sensors Configuration:\t"  << m_current_sensor_configuration  << endl ;

	m_msgs << "\n\nDesired sensors configuration and speed:\n----------------------------------------\n";
	m_msgs << "Sensors configuration:\t" 	<<	m_desired_sensors_configuration <<endl;
	m_msgs << "UUV Speed:\t\t"  			<< 	m_desired_uuv_speed <<endl;

	m_msgs << "\nThresholds:\n-----------------------------------";
	m_msgs << "\nMinimum Successful Readings:\t" << MIN_SUCC_READINGS;
	m_msgs << "\nMaximum Power Consumption:\t"   << MAX_POWER_CONSUMPTION;

	m_msgs << "\n\nActiveForms Result:\t" << m_resultRQV << endl;

	m_msgs << "\n\nActiveForms Result Vector:\t" << m_resultFromManagingSystem << endl << endl;

	m_msgs << MOOSTime(true) <<" - "<< GetAppStartTime() <<" = "<< MOOSTime(true) - GetAppStartTime() << endl;

	m_msgs << "\n\n\nEND" << endl;
	return true;
//
//	m_msgs << "\n\nConfiguration Result\n";
//	double configurationResult;
//	for (int counter=0; counter<ARRAY_SIZE; counter++){
//		configurationResult = ( (m_RQV_results_array[counter][0]>MAX_POWER_CONSUMPTION) || (m_RQV_results_array[counter][1]<MIN_SUCC_READINGS)) ? -1 : m_RQV_results_array[counter][2];
//			m_msgs << configurationResult << "\t";
//			if ((counter+1)%21==0)
//				m_msgs << endl;
//		}
}



//---------------------------------------------------------
// Procedure: appendToMap
//---------------------------------------------------------
bool RQVMOOS::appendToSensorsMap(vector<string> vparams){
	std::map<string,Sensor>::iterator it;

	it =m_sensor_map.find(vparams[0]);
	if (it==m_sensor_map.end()){		// if this is a new sensor --> add it to the map
		Sensor newSensor;
		newSensor.name 				= vparams[0];
		newSensor.readingRate 		= atof(vparams[1].c_str());
		newSensor.readingRateSum	= atof(vparams[1].c_str());
		newSensor.readingRateTimes	= 1;
//		newSensor.readingRateAvg	= atof(vparams[1].c_str());
		newSensor.waitFailedTimes	= 0;
		newSensor.currentState		= 0;
		newSensor.newState = 0;
		m_sensor_map.insert(std::pair<string,Sensor>(newSensor.name, newSensor));
	}
	else{								//else make the changes and modify it
		it->second.readingRate 		= 	atof(vparams[1].c_str());
		it->second.readingRateSum	+=	atof(vparams[1].c_str());
		it->second.readingRateTimes	++;
//		it->second.sensorIdleTimes 	= 0;
//		it->second.sensorRecovered	= 0;
//		it->second.readingRateAvg	= 	it->second.readingRateSum / it->second.readingRateTimes;
	}
	return true;
}



//---------------------------------------------------------
// Procedure: appendToMap
//---------------------------------------------------------
bool RQVMOOS::resetSensorsAverageReadingRate(){

	for (sensorMap::iterator it=m_sensor_map.begin(); it!=m_sensor_map.end(); it++){
	  it->second.readingRateSum		= 0;
	  it->second.readingRateTimes	= 0;
	}
	return true;
}



//---------------------------------------------------------
// Procedure: invokeManagingSystem
//---------------------------------------------------------
bool RQVMOOS::invokeManagingSystem(){

	double sensor1AvgReadingRate 	;
	double sensor2AvgReadingRate 	;
	double sensor3AvgReadingRate 	;

	//Calculate sensors average rates
//	estimateReadingRate(sensor1AvgReadingRate, sensor2AvgReadingRate, sensor3AvgReadingRate);
    estimateReadingRate("SONAR_SENSOR_SONAR1", 5, 2, sensor1AvgReadingRate);
    estimateReadingRate("SONAR_SENSOR_SONAR2", 4, 4, sensor2AvgReadingRate);
    estimateReadingRate("SONAR_SENSOR_SONAR3", 4, 8, sensor3AvgReadingRate);

	//Invoke ActiveForms using sockets
	char variables[256] = "5,4,4,95,90,85,1,5,3.5,0\n";

	std::ostringstream ss;
	ss	<< sensor1AvgReadingRate <<","<< sensor2AvgReadingRate <<","<< sensor3AvgReadingRate <<"\n";
//		<< "1.0" <<","<< "1.0" <<","<< "1.0" <<","<< m_current_sensor_configuration <<","<< "1"  <<","<< "1.0"  <<","<< 0 <<"\n";
//	if (sensor1AvgReadingRate<=0.1 && sensor2AvgReadingRate<=0.1 && sensor3AvgReadingRate<=0.1){
//		string str = "5,4,4\n";
//		strcpy(variables, str.c_str());
//	}
//	else{
    memset(variables, 0, sizeof(char)*256);
    strcpy(variables, (ss.str().c_str()));
//	}

	runPrism(variables);
	m_resultRQV.assign(variables);

	writeToFile("receivedFromServer.txt", m_resultRQV);

	int sensor1 = m_current_sensor_configuration % 2 > 0 ? 1 : 0;
	int sensor2 = m_current_sensor_configuration % 4 > 1 ? 1 : 0;
	int sensor3 = m_current_sensor_configuration % 8 > 3 ? 1 : 0;

	vector<string> svector = parseString(m_resultRQV, ",");
	int tempSensor1 = atoi(((string)svector[0]).c_str());
	sensor1 = tempSensor1!=-1 ? tempSensor1 : sensor1;
	int tempSensor2 = atoi(((string)svector[1]).c_str());
	sensor2 = tempSensor2!=-1 ? tempSensor2 : sensor2;
	int tempSensor3 = atoi(((string)svector[2]).c_str());
	sensor3 = tempSensor3!=-1 ? tempSensor3 : sensor3;

	int bestSensorsConfiguration = 0;
	bestSensorsConfiguration += sensor1==1 ? 1 : 0;
	bestSensorsConfiguration += sensor2==1 ? 2 : 0;
	bestSensorsConfiguration += sensor3==1 ? 4 : 0;

	double tempSpeed = atof(((string)svector[3]).c_str());
	double bestSpeed = tempSpeed!=-1 ? tempSpeed : m_desired_uuv_speed;

	m_desired_sensors_configuration = bestSensorsConfiguration;
	m_desired_uuv_speed				= bestSpeed;
	m_previous_sensor_configuration	= m_current_sensor_configuration;
	m_current_sensor_configuration 	= m_desired_sensors_configuration;

	m_resultFromManagingSystem = svector[0] +":"+ svector[1] +":"+ svector[2] +":"+ svector[3];

	return true;
}




//---------------------------------------------------------
// Procedure: updateSensorsState
//---------------------------------------------------------
void RQVMOOS::updateSensorsState(){
    int bestSensorsConfiguration = m_current_sensor_configuration;
    sensorMap::iterator itS1 	= m_sensor_map.find("SONAR_SENSOR_SONAR1");
    bool sensor1ON 				= bestSensorsConfiguration%2>0;
    int sensor1State			= itS1->second.currentState;
    if (sensor1ON)
        itS1->second.newState = 2; 													//ON
    else if (sensor1State==-1 || sensor1State==1)
        itS1->second.newState = itS1->second.currentState;	                        //FAIL or RETRY
    else
        itS1->second.newState = 0;													//IDLE

    sensorMap::iterator itS2 	= m_sensor_map.find("SONAR_SENSOR_SONAR2");
    bool sensor2ON 				= bestSensorsConfiguration%4>1;
    int sensor2State			= itS2->second.currentState;
    if 	(sensor2ON)
        itS2->second.newState = 2; 													//ON
    else if (sensor2State==-1 || sensor2State==1)
        itS2->second.newState = itS2->second.currentState;	                        //FAIL or RETRY
    else
        itS2->second.newState = 0;													//IDLE

    sensorMap::iterator itS3 	= m_sensor_map.find("SONAR_SENSOR_SONAR3");
    bool sensor3ON 				= bestSensorsConfiguration%8>3;
    int sensor3State			= itS3->second.currentState;
    if  (sensor3ON>0)
        itS3->second.newState = 2; 													//ON
    else if (sensor3State==-1 || sensor3State==1)
        itS3->second.newState = itS3->second.currentState;	                        //FAIL or RETRY
    else
        itS3->second.newState = 0;													//IDLE
}



//---------------------------------------------------------
// Procedure: estimateReadingRate
//---------------------------------------------------------
bool RQVMOOS::estimateReadingRate(string sensorName, double sensorNormalOperatingRate, int sensorConfigurationActive,
                                  double &sensorAvgReadingRate){
	double readingRate;
	int currentSensorConfiguration = m_current_sensor_configuration;
    double timeStamp = m_current_iterate_call - m_previous_iterate_call;

    double failurePercentage = 0.1;

    //Sensor1
    sensorMap::iterator itS 		=  m_sensor_map.find(sensorName);
    double sensorAVGReadingRate = itS->second.readingRateTimes / timeStamp;
    tempVariable = sensorAVGReadingRate;

    if (sensorAVGReadingRate < failurePercentage*sensorNormalOperatingRate) {       //FAILURE
        if (++itS->second.waitFailedTimes==5){                                      //RETRY	--> ORANGE
            itS->second.currentState    = 1;
            itS->second.waitFailedTimes = 0;
        }
        else{
            itS->second.currentState 	= -1;							            //FAIL   --> RED
        }
        sensorAvgReadingRate 			= 0.002;
    }
    else{
        if (itS->second.waitFailedTimes==0){
            if (currentSensorConfiguration%sensorConfigurationActive>0) {           //ON
                sensorAvgReadingRate 				= sensorAVGReadingRate;
                itS->second.waitFailedTimes 		= 0;
                itS->second.currentState			= 2;
            }
            else if (currentSensorConfiguration%sensorConfigurationActive<=0){      //IDLE
                sensorAvgReadingRate 				= sensorNormalOperatingRate;
                itS->second.waitFailedTimes 		= 0;
                itS->second.currentState			= 0;						    //IDLE	--> WHITE
            }
        }
        else if (++itS->second.waitFailedTimes<5){                                  //FAIL   --> RED
            itS->second.currentState 		= -1;
            sensorAvgReadingRate 			= 0.003;
        }
        else if (itS->second.waitFailedTimes==5) {                                    //RETRY	--> ORANGE
            itS->second.currentState	  	= 1;
            itS->second.waitFailedTimes     = 0;
            sensorAvgReadingRate 			= 0.004;
        }
    }
    itS->second.readingRateAvg              = sensorAvgReadingRate;

//    if (sensor1AVGReadingRate < 0.5*sensorNormalOperatingRate){ 			        //FAILURE
//		 if (currentSensorConfiguration%2<=0 && itS1->second.waitFailedTimes==0){
//			 sensor1AvgReadingRate 			= sensorNormalOperatingRate;
//			 itS1->second.currentState		= 0;
//		 }
//		 else
//		 if (++itS1->second.waitFailedTimes==5){					                //RETRY	--> ORANGE
//			 itS1->second.currentState	  	= 1;
//			 itS1->second.waitFailedTimes 	= 0;
//			 sensor1AvgReadingRate 			= 0.001;
//		 }
//		 else{
//			 itS1->second.currentState 		= -1;							        //FAIL   --> RED
//			 sensor1AvgReadingRate 			= 0.001;
//		 }
//	 }
//	 else {
//		 if (itS1->second.waitFailedTimes==0){
//			 if (currentSensorConfiguration%2>0){ //ON
//				 sensor1AvgReadingRate 				= sensor1AVGReadingRate;
//				 itS1->second.waitFailedTimes 		= 0;
//				 itS1->second.currentState			= 2;						    //ON	--> GREEN
//			 }
//			 else if (currentSensorConfiguration%2<=0){ //IDLE
//				 sensor1AvgReadingRate 				= 5;
//				 itS1->second.waitFailedTimes 		= 0;
//				 itS1->second.currentState			= 0;						    //IDLE	--> WHITE
//			 }
//		 }
//		 else if (++itS1->second.waitFailedTimes<5){
//			 itS1->second.currentState 		= -1;							        //FAIL   --> RED
//			 sensor1AvgReadingRate 			= 0.001;
//		 }
//		 else if (itS1->second.waitFailedTimes==5){					                //RETRY	--> ORANGE
//			 itS1->second.currentState	  	= 1;
//			 itS1->second.waitFailedTimes 	= 0;
//			 sensor1AvgReadingRate 			= 0.001;
//		 }
//	 }
//	 itS1->second.readingRateAvg = sensor1AvgReadingRate;


	return true;
}



///////////////////////////////////////////////////////////////////////////
/* 			UTILITY FUNCTIONS				*/
//////////////////////////////////////////////////////////////////////////

//---------------------------------------------------------
// Procedure: createLogData
//---------------------------------------------------------
string RQVMOOS::createLogData(double loopTime){
	std::stringstream stringStream;

    int newState =  m_desired_sensors_configuration;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR1"].newState == 1 ?  1 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR2"].newState == 1 ?  2 : 0;
	newState     += m_sensor_map["SONAR_SENSOR_SONAR3"].newState == 1 ?  4 : 0;

	stringStream << MOOSTime(true) - GetAppStartTime() <<",,"
                 << m_sensor_map["SONAR_SENSOR_SONAR1"].readingRateAvg << ","
                 << m_sensor_map["SONAR_SENSOR_SONAR2"].readingRateAvg << ","
                 << m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateAvg << ","
                 << m_desired_sensors_configuration                    <<","
                 << newState <<","
                 << m_desired_uuv_speed <<","
                 << loopTime <<endl;

//    for (int sensorNum=0; sensorNum<7; sensorNum++){
//		int index 					= m_RQV_best_configuration_per_sensor[sensorNum];//sensorNum*21+(int)((m_desired_uuv_speed-2.0)*10);
//		double successfulReadings	= m_RQV_results_array[index][1];
//		double powerConsumption		= m_RQV_results_array[index][0];
//		double	cost				= m_RQV_results_array[index][2];
//		double rate;
//		switch (sensorNum){
//			case 0 : {rate= m_sensor_map["SONAR_SENSOR_SONAR1"].readingRateAvg; break;}
//			case 1 : {rate= m_sensor_map["SONAR_SENSOR_SONAR2"].readingRateAvg; break;}
//			case 3 : {rate= m_sensor_map["SONAR_SENSOR_SONAR3"].readingRateAvg; break;}
//			default :{rate = -1; break;							}
//
//		}
//		stringStream << successfulReadings <<","<< powerConsumption <<","<< cost <<","<< rate <<",,";
//	}

//	int newState =  m_desired_sensors_configuration;
//	newState     += m_sensor_map["SONAR_SENSOR_SONAR1"].newState == 1 ?  1 : 0;
//	newState     += m_sensor_map["SONAR_SENSOR_SONAR2"].newState == 1 ?  2 : 0;
//	newState     += m_sensor_map["SONAR_SENSOR_SONAR3"].newState == 1 ?  4 : 0;

//	stringStream << m_desired_sensors_configuration <<","<< newState <<","
//				 << m_RQV_results_array[m_desired_results_index][1] <<","<<  m_RQV_results_array[m_desired_results_index][0]<<","
//				 << m_desired_configuration_cost <<","<< m_desired_uuv_speed <<","<<loopTime <<endl;
	return stringStream.str();
}



//---------------------------------------------------------
// Procedure: writeToFile
//---------------------------------------------------------
bool RQVMOOS::logToFile(std::string message){
  static bool firstTimeFlag = true;
  ofstream myfile;
  if (firstTimeFlag){
  	myfile.open ("logfile/output_RQVMOOS_log.csv");
    //append the headers
  	myfile <<   "Time,,S1,S2,S3,Best_Config,Best_newState,Best_Speed,LOOP\n";
	firstTimeFlag = false;
  }
  else{
  	myfile.open ("logfile/output_RQVMOOS_log.csv", ios::app);
  }

  myfile << message ;//<< "\n";
  myfile.close();
  return true;
}


//---------------------------------------------------------
// Procedure: writeToFile
//---------------------------------------------------------
/**General function that writes to a given filename the message passed as a parameter **/
bool writeToFile(string fileName, string message){
	static bool firstTimeFlag2 = true;
	ofstream myfile;
	if (firstTimeFlag2){
		myfile.open ("logfile/"+fileName);
		firstTimeFlag2 = false;
	}
	else{
		myfile.open ("logfile/"+fileName, ios::app);
	}

	myfile << message;
	myfile.close();
	return true;
}






