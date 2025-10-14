@echo off
echo Compiling Week 5 files...
javac *.java payment/*.java
if %errorlevel% == 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)

