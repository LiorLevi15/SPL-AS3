CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: Main
	g++ -o bin/BGSclient bin/connectionHandler.o bin/Main.o bin/ClientTask.o bin/ServerTask.o $(LDFLAGS)

Main: bin/connectionHandler.o bin/Main.o bin/ClientTask.o bin/ServerTask.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/ServerTask.o: src/ServerTask.cpp
	g++ $(CFLAGS) -o bin/ServerTask.o src/ServerTask.cpp

bin/ClientTask.o: src/ClientTask.cpp
	g++ $(CFLAGS) -o bin/ClientTask.o src/ClientTask.cpp

bin/Main.o: src/Main.cpp
	g++ $(CFLAGS) -o bin/Main.o src/Main.cpp

.PHONY: clean
clean:
	rm -f bin/*