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

#ifndef RQVMOOS_HEADERS
#define RQVMOOS_HEADERS

#include "Client/Client.h"
#include "MOOS/libMOOS/Thirdparty/AppCasting/AppCastingMOOSApp.h"
#include <map>
#include <cstring>
#include <math.h>
#include <vector>
#include <cmath>

using namespace std;

class RQVMOOS : public AppCastingMOOSApp
{
 public:
   RQVMOOS();
   ~RQVMOOS();


 protected:
   bool OnNewMail(MOOSMSG_LIST &NewMail);
   bool Iterate();
   bool OnConnectToServer();
   bool OnStartUp();
   void RegisterVariables();
   bool buildReport();

 private:
   bool 	appendToSensorsMap(vector<string> vparams);
   bool		resetSensorsAverageReadingRate();

   bool 	quantitativeVerification();
   bool		invokeManagingSystem();
   bool 	findBestConfiguration();
   void		updateSensorsState();

   bool		estimateDistanceCovered();
   double 	estimateSuccessRate(double speed_threshold, double speed, double alpha, double beta);
//   bool		estimateReadingRate(double &sensor1AvgReadingRate, double &sensor2AvgReadingRate, double &sensor3AvgReadingRate);
  double	estimateReadingRate(string sensorName, double sensorNormalOperatingRate, int sensorConfigurationActive);
  void		sendNotifications(double looptime);

   string	createLogData(double loopTime);
   bool 	logToFile(string message);

 private: // Configuration variables

 private: // State variables
   unsigned int m_iterations;
   double       m_timewarp;

   //Variables related to distance covered by the AUV
   bool 	m_first_reading;
   double	m_current_x;
   double	m_current_y;
   double	m_previous_x;
   double	m_previous_y;
   double	m_total_distance;	// total distance covered
   double	m_current_distance; // distance covered between consecutive iterations


   //Sensor Variables
   double m_maximum_power_consumption_per_iteration			; // Sensor accuracy as estimated by PRISM

   //UUV Speed Related Variables
   double m_uuv_speed				; // Current UUV speed
   double m_uuv_speed_threshold		; // Speed threshold - to be passed as a parameter via .moos file
   double m_uuv_speed_maximum		; // Maximum UUV speed -  to be passed as a parameter via .moos file


   struct Sensor{
   	std::string name;
   	double 		readingRate;
   	double 		readingRateSum;
   	int			readingRateTimes;
   	double		readingRateAvg;
   	int			sensorIdleTimes;
   	int			currentState;		// (-1)->FAIL, (0)->IDLE, (1)->RETRY, (2)->ON
   	int 		newState;
   };
   typedef std::map<string, Sensor> sensorMap;
   sensorMap m_sensor_map;



   double m_RQV_results_array[147][3];	//keeps the results of model checking
//	int		m_RQV_speed_flag_array[21];   //indicates whether a certain speed value does not satisfy a property --> excluded from next model checks
   int	  m_RQV_best_configuration_per_sensor[7];

   int m_previous_sensor_configuration;
   int m_current_sensor_configuration;


   //Constants
	const int 		FIXED_DISTANCE;
	const int 		ARRAY_SIZE 	 ;
	const int 		CSLPROPPERTIES;
	const double 	MIN_SPEED  ;

	//Parameters given in MOOS file
	double	MIN_SUCC_READINGS;
	double 	MAX_POWER_CONSUMPTION;
	double 	SENSOR_1_OPERATING_COST;
	double	SENSOR_2_OPERATING_COST;
	double	SENSOR_3_OPERATING_COST;
	int 	M_COOLING_OFF_PERIOD;			//time between successive invocations for quantitative verification


	int 	m_desired_sensors_configuration;
	double	m_desired_uuv_speed;
	double	m_desired_configuration_cost;
	int		m_desired_results_index;
	string	m_resultFromManagingSystem;

	double sensor1_threshold;
	double sensor2_threshold;
	double sensor3_threshold;

    double tempVariable;

	string m_active_behaviour;
	std::stringstream sstm;

	double m_previous_iterate_timestamp;
	double m_current_iterate_timestamp;

//	double m_previous_reading_rate_estimation_timestamp;
//	double m_current_reading_rate_estimation_timestamp;

	string m_resultRQV;

};

bool writeToFile(string s, string ss);

#endif
