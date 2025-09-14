@echo off
echo ========================================
echo   Java Encapsulation Project Runner
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
echo Checking for Java compiler...
where javac >nul 2>&1
if %errorlevel% neq 0 (
    echo WARNING: Java compiler (javac) not found in PATH
    echo.
    echo To run this project, you need to:
    echo 1. Install JDK (Java Development Kit) - not just JRE
    echo 2. Add javac to your system PATH
    echo 3. Or use an IDE like IntelliJ IDEA, Eclipse, or VS Code
    echo.
    echo Expected output for this project:
    type ExpectedOutput.txt
    echo.
    pause
    exit /b 0
)

echo.
echo Compiling Java files...
javac *.java
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Running ConfigDemo...
java ConfigDemo

echo.
echo Project completed successfully!
pause
