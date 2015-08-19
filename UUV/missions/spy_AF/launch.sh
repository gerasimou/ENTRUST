#!/bin/bash 
COMMUNITY="spy"

##-------------------------------------------------------
#  Part 1: Check for and handle command-line arguments
#-------------------------------------------------------
TIME_WARP=1
JUST_MAKE="no"
for ARGI; do
    if [ "${ARGI}" = "--help" -o "${ARGI}" = "-h" ] ; then
	printf "%s [SWITCHES] [time_warp]   \n" $0
	printf "  --help, -h         \n" 
	exit 0;
    elif [ "${ARGI//[^0-9]/}" = "$ARGI" -a "$TIME_WARP" = 1 ]; then 
        TIME_WARP=$ARGI
	elif [ "${ARGI}" = "--just_make" -o "${ARGI}" = "-j" ]; then
		JUST_MAKE="yes"
    else
		printf "Bad Argument: %s \n" $ARGI
		exit 0
    fi
done

#printf "$TIME_WARP\n"

WD=`pwd`


# #-------------------------------------------------------
# #  Part 2: Create the .moos and .bhv files. 
# #-------------------------------------------------------
# #Vehicle 1
# VEHICLE_NAME="APOLLO"			
# START_POSITION="-80,-35"
# LOITER_X="0" LOITER_Y="-100"
# SENSOR1_TICK="5"		SENSOR1_FAIL=""	  	SENSOR1_DEGRADATION=""
# SENSOR2_TICK="4" 		SENSOR2_FAIL="" 	SENSOR2_DEGRADATION=""
# #normal
# #SENSOR3_TICK="4.5"	
# 	# SENSOR3_FAIL="300:400,1000:1100,4000:4200"	
# 		# SENSOR3_DEGRADATION="0.55,0.2,0.55"
# #no failure
# SENSOR3_TICK="4.5"	SENSOR3_FAIL="" 		SENSOR3_DEGRADATION="0.25,0.2,0.55"

# #Create moos file for vehicle
# nsplug meta_vehicle.moos targ_${VEHICLE_NAME}.moos -f WARP=$TIME_WARP  												\
# 	VEHICLE_NAME=${VEHICLE_NAME}					SERVER_PORT="900${INDEX}" 											\
# 	START_POSITION=${START_POSITION}				RQV_PORT_NUMBER="920${INDEX}"										\
# 	LOITER_X=${LOITER_X}							LOITER_Y=${LOITER_Y}		   										\
# 	SENSOR1_TICK=${SENSOR1_TICK}					SENSOR1_NAME="SONAR_SENSOR_SONAR1" 									\
# 	SENSOR2_TICK=${SENSOR2_TICK}					SENSOR2_NAME="SONAR_SENSOR_SONAR2" 									\
# 	SENSOR3_TICK=${SENSOR3_TICK}					SENSOR3_NAME="SONAR_SENSOR_SONAR3" 									\
# 	SENSOR1_FAIL_PATTERN=${SENSOR1_FAIL_PATTERN} 	SENSOR1_DEGRADATION_VALUE_PATTERN=${SENSOR1_DEGRADATION_PATTERN}	\
# 	SENSOR2_FAIL_PATTERN=${SENSOR2_FAIL_PATTERN}	SENSOR2_DEGRADATION_VALUE_PATTERN=${SENSOR2_DEGRADATION_PATTERN}	\
# 	SENSOR3_FAIL_PATTERN=${SENSOR3_FAIL_PATTERN}	SENSOR3_DEGRADATION_VALUE_PATTERN=${SENSOR3_DEGRADATION_PATTERN}	\
# 	SENSOR_DEGRADATION_VALUE="1"	

# #Create behaviour file for vehicle 1
# nsplug meta_vehicle.bhv targ_${VEHICLE_NAME}.bhv -f 		\
# 	VEHICLE_NAME={VEHICLE_NAME}								\
# 	START_POSITION=${START_POSITION}						\
# 	LOITER_X=${LOITER_X}									\
# 	LOITER_Y=${LOITER_Y}					

# #Create moos file for shoreside
# nsplug meta_shoreside.moos targ_shoreside.moos -f WARP=$TIME_WARP 	   \
# 		COMMUNITY_NAME="shoreside"			SERVER_PORT="9000"		   \
# 											SHARE_LISTEN=$SHORE_LISTEN


# if [ ${JUST_MAKE} = "yes" ]; then
# 	printf "Target files built successfully. Exit!\n"
# 	exit 0
# fi



# #-------------------------------------------------------
# #  Part 3: Launch the processes
# #-------------------------------------------------------
# printf "Launching the shoreside MOOS Community (WARP=%s) \n" $TIME_WARP
# pAntler targ_shoreside.moos >& /dev/null &
# printf "Launching ${VEHICLE_NAME} MOOS Community (WARP=%s) \n" $TIME_WARP
# pAntler targ_${VEHICLE_NAME}.moos >& /dev/null &
# printf "Done \n"


# uMAC $targ_shoreside.moos

# printf "Killing all processes ... \n"
# kill %1 %2 %3 % %5
# printf "Done killing processes.   \n"



#-------------------------------------------------------
#  Part 2: Launch the processes
#-------------------------------------------------------
printf "Launching the %s MOOS Community (WARP=%s) \n"  $COMMUNITY $TIME_WARP
pAntler $COMMUNITY.moos --MOOSTimeWarp=$TIME_WARP >& /dev/null &

uMAC $COMMUNITY.moos

printf "Killing all processes ... \n"
kill %1 
printf "Done killing processes.   \n"

