@echo off
echo Compiling ShopEasy Online Shopping System...
javac *.java
if %errorlevel% neq 0 (
    echo Compilation failed. Check Java installation.
    pause
    exit /b 1
)
echo Compilation successful. Starting application...
java Main
pause
