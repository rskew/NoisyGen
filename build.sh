#! /bin/bash

# A very simple build script. To be extended into an Ant/Maven/Gradle build process.

javac -d bin -sourcepath src src/com/rowanskewes/filters/*.java

javac -d bin -sourcepath src src/com/rowanskewes/CLI/*.java
