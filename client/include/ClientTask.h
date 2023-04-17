//
// Created by spl211 on 31/12/2021.
//

#ifndef BOOST_ECHO_CLIENT_CLIENTTASK_H
#define BOOST_ECHO_CLIENT_CLIENTTASK_H

#include "connectionHandler.h"

class ClientTask {
public:
    ClientTask(ConnectionHandler *connectionHandlere, bool* shouldTerminate, bool* maybeShouldTerminate);
    void run();
private:
    ConnectionHandler *connectionHandlere;
    void shortToBytes(short num, char* bytesArr);
    bool* shouldTerminate;
    bool* maybeShouldTerminate;


};


#endif //BOOST_ECHO_CLIENT_CLIENTTASK_H
