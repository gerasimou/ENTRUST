#!/bin/bash 

processID=`ps -ef | awk '/[E]NTRUST/{print $2}'`

printf "PID: %s \n" $processID

printf "Running JConsole\n"

jconsole -interval=1 -J-Dcom.sun.management.jmxremote.authenticate=false -J-Dcom.sun.management.jmxremote.ssl=false $processID

#com.sun.management.jmxremote.authenticate=false
#com.sun.management.jmxremote.ssl=false