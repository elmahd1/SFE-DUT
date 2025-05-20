@echo off
title %~n0


REM Set application variables
set APP_NAME=ccisApp
set APP_VERSION=1.0.0
set MAIN_CLASS=ccis.App
set MIN_JAVA_VERSION=11

REM Check Java installation
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Java is not installed or not in PATH. Please install Java and try again.
    pause
    exit /b 1
)

REM Run the application from the JAR file


REM Set the path to your JavaFX SDK
set JAVAFX_SDK=%~dp0javafx-sdk-24.0.1

REM Run the application from the JAR file with JavaFX modules

java --module-path "%JAVAFX_SDK%\lib" --add-modules javafx.controls,javafx.fxml -jar "ccis-1.0-SNAPSHOT-fat.jar" %*
echo Application terminated.
pause