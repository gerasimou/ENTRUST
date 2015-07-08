

#clean
mvn clean

#remove existing java files & aar
AAR_NAME=Forex.aar
if [ -f "aar/$AAR_NAME" ]; then
	rm $AAR_NAME
	printf "Removed: $AAR_NAME\n"
fi

CLASSES_DIR=aar/soar
if [ -d "$CLASSES_DIR" ]; then
	rm -r $CLASSES_DIR
	printf "Removed: $CLASSES_DIR\n"
fi

sleep 1s

#compile
mvn compile

#copy generated class files to aar directory
cp -r target/classes/soar aar

#copy services xml
cp src/resources/services.xml aar/META-INF/

#swithc to aar
cd aar

#create aar
printf "Create aar $AAR_NAME\n"
jar cvf $AAR_NAME soar/ws/fx/* META-INF

#move aar to axis2 directory at Apache Tomcat server
DEST_AAR=/Library/TOMCAT/webapps/axis2/WEB-INF/services
if [ -f "$DEST_AAR/$AAR_NAME" ]; then
	rm $DEST_AAR/$AAR_NAME
	printf "Removed: $DEST_AAR/$AAR_NAME\n"
fi
cp Forex.aar /Library/TOMCAT/webapps/axis2/WEB-INF/services/
