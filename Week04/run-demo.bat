@echo off
echo Compiling Cafe POS Demo...
javac -cp ".;payment" CafePOSDemo.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Starting Cafe POS Demo...
echo.
java -cp ".;payment" CafePOSDemo
pause


