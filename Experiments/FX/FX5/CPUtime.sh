#!/bin/bash

for (( RUN=1; RUN <= 10; RUN++ ))
do
	echo $RUN
	(time java -cp . -jar ENTRUST_FX.jar resources/configFX5.properties) 2>> data/UUV_FX.csv
done
