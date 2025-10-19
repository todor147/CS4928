@echo off
cd /d "%~dp0"
javac Money.java Product.java Priced.java SimpleProduct.java 2>nul
javac -cp . pricing\*.java 2>nul
javac -cp . decorator\*.java 2>nul
javac -cp . factory\*.java 2>nul
javac -cp . payment\*.java 2>nul
javac -cp . smells\*.java 2>nul
javac -cp . tests\*.java 2>nul
javac -cp . Receipt*.java ReceiptPrinter.java CheckoutService.java 2>nul
echo Compilation complete.

