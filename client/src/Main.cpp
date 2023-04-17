//
// Created by spl211 on 01/01/2022.
//

#include "../include/Main.h"
#include "../include/connectionHandler.h"
#include "../include/ServerTask.h"
#include "../include/ClientTask.h"
using namespace std;
#include <thread>





int main(int argc, char *argv[]) {
    std::string usage =argv[0];
    std::string host =argv[1];
    short port =std::atoi(argv[2]);
    ConnectionHandler connectionHandler(host,port);
    if(!connectionHandler.connect()){
        std::cout<<"ERROR IN CONNECTING"<<endl;
        return -1;
    }
    bool* shouldTerminate = new bool(false);
    bool* maybeShouldTerminate = new bool(false);
    ClientTask clientTask(&connectionHandler, shouldTerminate,maybeShouldTerminate);
    ServerTask serverTask(&connectionHandler, shouldTerminate,maybeShouldTerminate);
    thread cT(&ClientTask::run, &clientTask);
    thread sT(&ServerTask::run, &serverTask);
    cT.join();
    sT.join();

    delete shouldTerminate;
    delete maybeShouldTerminate;

    return 0;
}