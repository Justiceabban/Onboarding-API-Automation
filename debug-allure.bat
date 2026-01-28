@echo off
echo ========================================
echo ALLURE RESULTS TROUBLESHOOTING SCRIPT
echo ========================================
echo.

cd /d "%~dp0"

echo Step 1: Checking Maven installation...
call mvn --version
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    pause
    exit /b 1
)
echo.

echo Step 2: Cleaning project...
call mvn clean
echo.

echo Step 3: Compiling project...
call mvn compile test-compile
echo.

echo Step 4: Running a single test...
call mvn test -Dtest=PreferredJourneyTest#testGetPreferredJourney_Success
echo.

echo Step 5: Checking if target folder exists...
if exist "target" (
    echo ✅ TARGET FOLDER EXISTS
    dir target /AD
) else (
    echo ❌ TARGET FOLDER DOES NOT EXIST
)
echo.

echo Step 6: Checking if allure-results folder exists...
if exist "target\allure-results" (
    echo ✅ ALLURE-RESULTS FOLDER EXISTS
    dir target\allure-results
) else (
    echo ❌ ALLURE-RESULTS FOLDER DOES NOT EXIST
)
echo.

echo Step 7: Checking test results...
if exist "target\surefire-reports" (
    echo ✅ SUREFIRE-REPORTS FOLDER EXISTS
    dir target\surefire-reports
) else (
    echo ❌ SUREFIRE-REPORTS FOLDER DOES NOT EXIST
)
echo.

echo ========================================
echo TROUBLESHOOTING COMPLETE
echo ========================================
echo.
echo If allure-results folder exists, run:
echo mvn allure:serve
echo.
pause
