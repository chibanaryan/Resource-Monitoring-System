A Resource Monitoring System developed as a final project for P434 - Distributed Systems.

How to build
============

Go to the Project4 directory and type

mvn clean install assembly:single

This will build the resourcemonitor-jar-with-dependencies.jar inside target directory. Copy the resourcemonitor-jar-with-dependencies.jar file to the bin directory

How to run
==========

To start the daemon run the deamon.sh file in the bin directory

i.e deamon.sh 1

When you run the daemon.sh you have to give a unique id for each instance of the deamon you start.

To start the the client run the client.sh file in the bin directory



