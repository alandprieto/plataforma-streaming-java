@echo off
setlocal
set OUT=bin
if not exist "%OUT%" mkdir "%OUT%"
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -cp "lib/*;src" -d "%OUT%" @sources.txt
set RESULT=%ERRORLEVEL%
if exist sources.txt del sources.txt
if %RESULT% EQU 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
    pause
)
endlocal