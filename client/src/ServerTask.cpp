//
// Created by spl211 on 31/12/2021.
//

#include "../include/ServerTask.h"
#include "../include/connectionHandler.h"
using namespace std;
#include <boost/algorithm/string.hpp>
#include "boost/lexical_cast.hpp"

#include "../include/Main.h"

ServerTask::ServerTask(ConnectionHandler *connectionHandlere, bool* shouldTerminate, bool* maybeShouldTerminate) : connectionHandlere(connectionHandlere), shouldTerminate(shouldTerminate), maybeShouldTerminate(maybeShouldTerminate){};

short ServerTask::bytesToShort(char* bytesArr){
    return (short)((bytesArr[0]&0xff)<<8)+(short)((bytesArr[1]&0xff));
}

void ServerTask::run() {
    while (!*shouldTerminate) {
        char opcode[2];
        std::string msg;
        connectionHandlere->getBytes(opcode, 2);
        connectionHandlere->getLine(msg);
        msg = msg.substr(0, msg.length()-1);
        std::string someString;
        std::string someString2;
        string opcodeString = "";
        opcodeString.push_back(opcode[0]);
        opcodeString.push_back(opcode[1]);
        int opcodeInt = stoi(opcodeString);
        if (opcodeInt == 9) {
            someString = ((int) (msg.at(0) - '0')) == 0 ? "PM" : "Public";
            msg = msg.substr(1, someString2.length() - 3);
            std::cout << "NOTIFICATION " << someString <<" "<< msg << endl;
        } else if (opcodeInt == 10) {
            std::cout << "ACK " << msg << std::endl;
            if (((int) (msg.at(0) - '0')) == 3) {
                *shouldTerminate=true;
                *maybeShouldTerminate=true;
                continue;
            }
        } else if (opcodeInt == 11) {
            std::cout << "ERROR " << msg << std::endl;
            *maybeShouldTerminate=true;
        }
    }
}

