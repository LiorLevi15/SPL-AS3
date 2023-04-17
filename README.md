# Assignment 3 for the Systems Programming course at BGU
the product of Lior and Emi

Grade: 100

Java and C++ written code

## The task is:
Utilize the design patterns Thread Per Client and Reactor to simulate a social network. The console-based client was written in C++ where as the server was written in Java. Our abilities in C++, Java, concurrency and threading, and server design patterns were put to the test in this task.

## How to run the code:

Reactor Server: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="7777 10"

TPC Server: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="7777"

### Client commands:

(1) REGISTER username password 01-02-0003

(2) LOGIN user pw 1

(3) LOGOUT

(4) FOLLOW 1 user

(5) POST content @user content content content

(6) PM user content

(7) LOGSTAT

(8) STAT user1 user2 user3

(12) BLOCK user
