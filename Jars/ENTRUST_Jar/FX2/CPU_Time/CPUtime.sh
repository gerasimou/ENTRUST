#!/bin/bash 

for (( RUN=1; RUN <= 10; RUN++ ))
do
	echo $RUN

	#no change
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST) 				2>> data/FX2_CPU_Time.csv

	#MW1 down
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST MW1 0.1) 		2>> data/FX2_CPU_Time.csv

	#MW1 up
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST MW1 0.976) 	2>> data/FX2_CPU_Time.csv

	#TA3 down
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST TA3 0.6) 		2>> data/FX2_CPU_Time.csv

	#TA3 up
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST TA3 0.985) 	2>> data/FX2_CPU_Time.csv

	#OR1 down
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST OR1 0.5) 		2>> data/FX2_CPU_Time.csv

	#OR1 up
	(time java -cp . -jar ENTRUST_Controller-FX2.jar main.ENTRUST OR1 0.995) 	2>> data/FX2_CPU_Time.csv

done

