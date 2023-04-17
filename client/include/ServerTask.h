//
// Created by spl211 on 31/12/2021.
//

#ifndef BOOST_ECHO_CLIENT_SERVERTASK_H
#define BOOST_ECHO_CLIENT_SERVERTASK_H

#include "connectionHandler.h"

class ServerTask {
public:
    ServerTask(ConnectionHandler *connectionHandlere, bool* shouldTerminate, bool* maybeShouldTerminate);
    void run();
private:
    ConnectionHandler *connectionHandlere;
    short bytesToShort(char* bytesArr);
    bool* shouldTerminate;
    bool* maybeShouldTerminate;

};


#endif //BOOST_ECHO_CLIENT_SERVERTASK_H
