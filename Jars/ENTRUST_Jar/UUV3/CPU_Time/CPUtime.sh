#!/bin/bash 

for (( RUN=1; RUN <= 10; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 4 4) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 4 1) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 4 4) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 1 4) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 4 4) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 1 1) 2>> data/UUV3_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV3.jar main.ENTRUST 5 4 4) 2>> data/UUV3_CPU.csv
done
