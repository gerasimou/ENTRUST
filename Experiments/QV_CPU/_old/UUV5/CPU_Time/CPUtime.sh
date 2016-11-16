#!/bin/bash 

for (( RUN=1; RUN <= 10; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 4 4 4 5) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 4 1 4 5) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 4 4 4 1) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 1 4 4 5) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 4 4 2 5) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 1 1 4 3) 2>> data/UUV5_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV5.jar main.ENTRUST 5 4 4 1 5) 2>> data/UUV5_CPU.csv
done
