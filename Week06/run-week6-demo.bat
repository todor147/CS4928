@echo off
echo Week6 Demo Runner
echo ==================
echo.

cd Week06

echo Compiling if needed...
javac -cp "src\main\java" -d target\classes src\main\java\com\cafepos\demo\Week6Demo.java src\main\java\com\cafepos\common\Money.java src\main\java\com\cafepos\factory\ProductFactory.java src\main\java\com\cafepos\pricing\*.java src\main\java\com\cafepos\smells\OrderManagerGod.java 2>nul

echo.
echo Running Week6Demo...
echo.
java -cp "target\classes;src\main\java" com.cafepos.demo.Week6Demo

pause





