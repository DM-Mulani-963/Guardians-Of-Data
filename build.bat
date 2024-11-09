@echo off
echo DATA Rakshak Build Script
echo ========================

:menu
echo.
echo 1. Clean Project
echo 2. Build Debug APK
echo 3. Build Release APK
echo 4. Install Debug APK
echo 5. Run Tests
echo 6. Exit
echo.

set /p choice="Enter your choice (1-6): "

if "%choice%"=="1" goto clean
if "%choice%"=="2" goto debug
if "%choice%"=="3" goto release
if "%choice%"=="4" goto install
if "%choice%"=="5" goto test
if "%choice%"=="6" goto end

:clean
echo Cleaning project...
call gradlew clean
goto menu

:debug
echo Building debug APK...
call gradlew assembleDebug
echo Debug APK location: app\build\outputs\apk\debug\app-debug.apk
goto menu

:release
echo Building release APK...
call gradlew assembleRelease
echo Release APK location: app\build\outputs\apk\release\app-release.apk
goto menu

:install
echo Installing debug APK...
call gradlew installDebug
goto menu

:test
echo Running tests...
call gradlew test
goto menu

:end
echo Exiting...
exit /b 0 