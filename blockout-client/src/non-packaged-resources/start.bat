@echo off

java -Djava.library.path=natives/ -jar ${project.build.finalName}.jar
