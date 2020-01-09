#! /usr/bin/env sh

# A very simple build script. To be extended into an Ant/Maven/Gradle build process.

mkdir -p bin

javac -d bin -sourcepath src src/com/rowanskewes/filters/*.java

javac -d bin -sourcepath src src/com/rowanskewes/CLI/*.java
