#!/bin/sh
javac -d bin/WEB-INF/classes -classpath "libs/*" src/main/tcd3/connectfour/*
jar -cvf ConnectFour.war -C src/webapp . -C bin .
