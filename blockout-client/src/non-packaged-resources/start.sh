#!/bin/sh

java -Djava.library.path=natives/ -jar ${project.build.finalName}.jar
