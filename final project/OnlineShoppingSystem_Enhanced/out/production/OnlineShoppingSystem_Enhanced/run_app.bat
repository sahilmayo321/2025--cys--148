@echo off
cd /d "%~dp0"
java -jar OnlineShoppingSystemGUI.jar
if errorlevel 1 (
    echo.
    echo The application could not start. Make sure Java 17 or newer is installed.
    pause
)
