@echo off
setlocal
set OUT=bin
rd /s /q "%OUT%" 2>nul
mkdir "%OUT%"
if defined JAVA_HOME (set "JAVAC=%JAVA_HOME%\bin\javac.exe") else (set "JAVAC=javac")
dir /s /b src\*.java > sources.txt
"%JAVAC%" -encoding UTF-8 -cp "lib/*;src" -d "%OUT%" @sources.txt
set RESULT=%ERRORLEVEL%
if exist sources.txt del sources.txt
if %RESULT% EQU 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
    pause
)
endlocal