#!/bin/bash 

for (( RUN=1; RUN <= 5; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 4 4 4 5 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 4 1 4 5 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 4 4 4 1 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 1 4 4 5 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 4 4 2 5 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 1 1 4 3 4) 2>> data/UUV6_CPU.csv
	(time java -cp . -jar ENTRUST_Controller-UUV6.jar main.ENTRUST 5 4 4 1 5 4) 2>> data/UUV6_CPU.csv
done
