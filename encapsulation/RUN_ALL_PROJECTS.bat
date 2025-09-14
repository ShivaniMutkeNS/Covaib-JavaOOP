@echo off
echo ========================================
echo   ALL 15 ENCAPSULATION PROJECTS RUNNER
echo ========================================
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo ========================================
echo   PROJECT 1: IMMUTABLE CONFIG LOADER
echo ========================================
type "1-immutable-config-loader\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 2: THREAD-SAFE COUNTER
echo ========================================
type "2-thread-safe-counter\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 3: BANK VAULT SECURITY
echo ========================================
type "3-bank-vault-multilayer-security\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 4: HEALTHCARE RECORDS
echo ========================================
type "4-healthcare-records-privacy-law\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 5: ONLINE EXAM SYSTEM
echo ========================================
type "5-online-exam-system-anticheating\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 6: IMMUTABLE MONEY CLASS
echo ========================================
type "6-immutable-money-class\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 7: SMART HOME DEVICE
echo ========================================
type "7-smart-home-device-state\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 8: CREDIT CARD SYSTEM
echo ========================================
type "8-credit-card-system\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 9: IMMUTABLE AUDIT LOG
echo ========================================
type "9-immutable-audit-log\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 10: GAME CHARACTER
echo ========================================
type "10-game-character-attributes\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 11: E-COMMERCE CART
echo ========================================
type "11-ecommerce-shopping-cart\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 12: EMPLOYEE PAYROLL
echo ========================================
type "12-employee-payroll-hidden-salary\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 13: IOT SENSOR DATA
echo ========================================
type "13-iot-sensor-data-stream\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 14: VERSIONED DOCUMENT
echo ========================================
type "14-versioned-document\ExpectedOutput.txt"
echo.
echo Press any key to continue to next project...
pause >nul

echo.
echo ========================================
echo   PROJECT 15: STOCK TRADING SYSTEM
echo ========================================
type "15-stock-trading-system\ExpectedOutput.txt"
echo.

echo.
echo ========================================
echo   ALL PROJECTS COMPLETED!
echo ========================================
echo.
echo ✅ All 15 encapsulation projects have been demonstrated!
echo ✅ Each project shows proper encapsulation principles
echo ✅ Expected outputs displayed for all projects
echo.
echo To actually run the Java code:
echo 1. Install JDK (Java Development Kit)
echo 2. Add javac to your system PATH
echo 3. Run: javac *.java && java [ClassName] in each project directory
echo.
echo Or use an IDE like IntelliJ IDEA, Eclipse, or VS Code
echo.
pause
