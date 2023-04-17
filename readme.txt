1)How to run your code
1.1) Command line for each server. - it can be an argument or a different
execution file – you just need to explain how to do that:

Reactor Server:
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="7777 10"

TPC Server:
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="7777"

1.2)
(1) REGISTER NAME PASS 11-11-1111
(2) LOGIN USER PASS 1
(3) LOGOUT
(4.1) FOLLOW 0 USER
(4.3) FOLLOW 1 USER
(5) POST @USER TEXT TEXT TEXT
(6) PM USER TEXT
(7) LOGSTAT
(8) STAT USER1|USER2|USER3
(12) BLOCK USER

2) where in the code you store the filtered set of words:
the list located in the (bgu/spl/net/bidi/EncoderDecoder.java) calss in the decodeNctByte function,
the name of the list is toFilter (declared in line 74). 
