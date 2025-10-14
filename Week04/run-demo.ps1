Write-Host "Compiling Cafe POS Demo..." -ForegroundColor Cyan
javac -cp ".;payment" CafePOSDemo.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "Starting Cafe POS Demo..." -ForegroundColor Green
Write-Host ""
java -cp ".;payment" CafePOSDemo


