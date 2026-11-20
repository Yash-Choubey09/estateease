@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"
"%~dp0..\apache-maven-3.9.9\bin\mvn.cmd" %*
