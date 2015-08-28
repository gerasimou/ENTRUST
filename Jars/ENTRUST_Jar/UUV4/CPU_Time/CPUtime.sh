#!/bin/bash 

for (( RUN=1; RUN <= 20; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 4 4 4) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 4 1 4) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 4 4 4) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 1 4 4) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 4 4 2) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 1 1 4) 2>> data/UUV4_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV4.jar main.ENTRUST 5 4 4 1) 2>> data/UUV4_CPU.csv
done
