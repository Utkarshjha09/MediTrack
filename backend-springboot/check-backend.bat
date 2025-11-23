@echo off
echo.
echo ========================================
echo   Medicine Tracker - Backend Status
echo ========================================
echo.

:CHECK
curl -s http://localhost:8080/api/medicines >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Backend is ONLINE and ready!
    echo.
    echo You can now:
    echo  - Open backend-tester.html to test APIs
    echo  - Start your React frontend
    echo  - Access Swagger UI: http://localhost:8080/swagger-ui.html
    echo.
    pause
    exit
) else (
    echo [X] Backend is NOT running...
    echo.
    echo Starting backend server in new window...
    echo Please wait 30-40 seconds for startup...
    echo.
    
    start "" powershell -NoExit -Command "cd '%~dp0'; $env:JAVA_HOME='C:\Users\utkar\.jdk\jdk-21.0.8'; $env:PATH='C:\Users\utkar\.jdk\jdk-21.0.8\bin;'+$env:PATH; .\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev"
    
    echo.
    echo Waiting for backend to start...
    timeout /t 10 /nobreak >nul
    
    :WAIT
    curl -s http://localhost:8080/api/medicines >nul 2>&1
    if %errorlevel% == 0 (
        echo.
        echo [✓] Backend is NOW ONLINE!
        echo.
        pause
        exit
    ) else (
        echo Still waiting...
        timeout /t 3 /nobreak >nul
        goto WAIT
    )
)
