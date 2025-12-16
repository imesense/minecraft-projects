@echo off
chcp 65001 > nul

set JAVA_HOME=%HOMEDRIVE%\Users\%USERNAME%\.jdks\openjdk-1.8.0_382
call .\gradlew.bat build

pause
