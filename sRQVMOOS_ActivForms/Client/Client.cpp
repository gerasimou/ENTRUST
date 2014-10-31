#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdlib.h>
#include <cstring>
#include <unistd.h>
#include <iostream>
#include <fstream>


using namespace std;

int sockfd, portno, n;

struct sockaddr_in serv_addr;
struct hostent *server;

void error(const char *msg)
{
    perror(msg);
    exit(0);
}


/**
 * Initialises the client
 */
int initialiseClient(){
	const char * argv[]={"socket","localhost", "56567"};
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");


    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }


    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) < 0)
        error("ERROR connecting");

    return (1);
}


//---------------------------------------------------------
// Procedure: writeToFile
//---------------------------------------------------------
/**General function that writes to a given filename the message passed as a parameter **/
bool writeToFile(string fileName, string message){
	static bool firstTimeFlag2 = true;
	ofstream myfile;
	if (firstTimeFlag2){
		myfile.open ("logfile/"+fileName);
		firstTimeFlag2 = false;
	}
	else{
		myfile.open ("logfile/"+fileName, ios::app);
	}

	myfile << message;
	myfile.close();
	return true;
}



/**
 * Invokes the the server side passing a set of parameters
 */
float runPrism(char *variables){
		writeToFile("socket.txt", variables);
        n = write(sockfd,variables,strlen(variables));
        if (n < 0)
            error("ERROR writing to socket");
		memset(variables, 0, sizeof(char)*256);
        n = read(sockfd, variables, 255);
        return (1.0);
}
