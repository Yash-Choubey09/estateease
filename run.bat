@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
echo Starting EstateEase OPMS on http://localhost:8080 ...
"%~dp0..\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
