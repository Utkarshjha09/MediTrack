@echo off
echo Starting Medicine Expiry Tracker Backend...
echo.

set JAVA_HOME=C:\Users\utkar\.jdk\jdk-21.0.8
set PATH=%JAVA_HOME%\bin;%PATH%

echo Using Java 21 at: %JAVA_HOME%
echo.

cd /d "%~dp0"

echo Building and starting Spring Boot application with H2 database...
echo.

call mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

pause
