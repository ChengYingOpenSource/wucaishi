@echo off

for %%x in ("%JAVA_HOME%") do set JAVA_HOME=%%~sx
for %%x in ("%LUOSHU_HOME%") do set LUOSHU_HOME=%%~sx

if "%1" == "start" (
    echo start Luoshu Server
    start "Luoshu Server" java -Dfile.encoding=UTF-8 -cp .;%JAVA_HOME%\lib\*;%LUOSHU_HOME%\lib\*; com.cy.onepush.App --spring.config.additional-location=file:%LUOSHU_HOME%\config\application.yml
) else if "%1" == "stop" (
    echo stop Luoshu Server
    taskkill /fi "WINDOWTITLE eq Luoshu Server"
) else (
    echo please use "run.bat start" or "run.bat stop"
)
