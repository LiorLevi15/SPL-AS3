//
// Created by spl211 on 31/12/2021.
//

#include "../include/ClientTask.h"
#include "../include/Main.h"
#include "../include/connectionHandler.h"
#include <boost/algorithm/string.hpp>
#include "boost/lexical_cast.hpp"
#include <iostream>
#include <ctime>
#include <mutex>
#include <thread>

using namespace std;


ClientTask::ClientTask(ConnectionHandler *connectionHandlere, bool* shouldTerminate, bool* maybeShouldTerminate) : connectionHandlere(connectionHandlere), shouldTerminate(shouldTerminate), maybeShouldTerminate(maybeShouldTerminate){};

void ClientTask::shortToBytes(short num, char* bytesArr){
    bytesArr[0]=((num>>8)&0xFF);
    bytesArr[0]=((num)&0xFF);

}
void ClientTask::run() {
    const short bufferSize = 1024;
    string captcha = "1";
    int intOpcode=0;
    char charOpcode[2];

    while (!*shouldTerminate){
        char buffer[bufferSize];
        cin.getline(buffer , bufferSize);
        string line(buffer);
        vector<std::string> commends;
        boost::split(commends, line, boost::is_any_of(" "));

        string toSend;
        if(std::equal(commends[0].begin(), commends[0].end(), "REGISTER")){
            toSend="01"+'\0'+commends[1]+'\0'+commends[2]+'\0'+commends[3];
            intOpcode=1;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "LOGIN")){
            toSend="02"+'\0'+commends[1]+'\0'+commends[2]+'\0'+commends[3];
            intOpcode=2;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "LOGOUT")){
            toSend="03";
            intOpcode=3;
            connectionHandlere->sendLine(toSend);
            *maybeShouldTerminate=false;
            while(!*maybeShouldTerminate)
                std::this_thread::yield();
            *maybeShouldTerminate = false;
            continue;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "FOLLOW")){
            toSend="04"+'\0'+commends[1]+'\0'+commends[2];
            intOpcode=4;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "POST")){
            toSend="05"+'\0';
            intOpcode=0;
            for(int i=1; i<=((int)commends.size()-2); i=1+i){
                toSend=toSend+commends[i]+'\0';

            }
            toSend=toSend+commends[commends.size()-1];
            intOpcode=5;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "PM")){
            toSend="06"+'\0'+commends[1];
            for(int i=2; i<((int)commends.size()); i=1+i){
                toSend=toSend+'\0'+commends[i];
            }
            intOpcode=6;

        } else if (std::equal(commends[0].begin(), commends[0].end(), "LOGSTAT")){
            toSend="07";
            intOpcode=7;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "STAT")){
            toSend="08"+'\0';
            for(int i=1; i<=((int)commends.size()-2); i=1+i){
                toSend=toSend+commends[i]+',';
            }
            toSend=toSend+commends[commends.size()-1];
            intOpcode=8;
        } else if (std::equal(commends[0].begin(), commends[0].end(), "BLOCK")){
            toSend="12"+'\0'+commends[1];
            intOpcode=12;
        }else {
            continue;
        }
        shortToBytes(intOpcode,charOpcode);



        if (!*shouldTerminate) {
            connectionHandlere->sendLine(toSend);
        }
    }

}