#!/bin/sh
java -classpath "libs/*:bin" -javaagent:libs/jacocoagent.jar org.junit.platform.console.ConsoleLauncher execute --scan-class-path
java -jar libs/jacococli.jar report jacoco.exec --classfiles bin --xml jacoco.xml --name CoverageReport --sourcefiles src/main/tcd3/connectfour/
