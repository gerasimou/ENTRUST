#!/bin/bash

for (( RUN=1; RUN <= 10; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_UUV.jar ) 2>> data/UUV_CPU.csv
done
