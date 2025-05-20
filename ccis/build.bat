@echo off
echo Build and Create Installer Script
echo ================================

REM Set variables
set INNOSETUP_PATH="C:\Program Files (x86)\Inno Setup 6\ISCC.exe"
set SCRIPT_PATH=installer.iss

echo Step 1: Building Maven project...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed!
    pause
    exit /b 1
)
echo Maven build successful!

echo.
echo Step 2: Checking InnoSetup installation...
if not exist %INNOSETUP_PATH% (
    echo InnoSetup not found at %INNOSETUP_PATH%
    echo Please install InnoSetup or update this script with the correct path.
    pause
    exit /b 1
)
echo InnoSetup found!

echo.
echo Step 3: Creating installer...
%INNOSETUP_PATH% %SCRIPT_PATH%
if %ERRORLEVEL% NEQ 0 (
    echo Installer creation failed!
    pause
    exit /b 1
)
echo Installer created successfully!

echo.
echo Build and installer creation completed!
echo The installer can be found in the 'installer' directory.
pause