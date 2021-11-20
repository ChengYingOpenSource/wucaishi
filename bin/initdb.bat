@echo off

for %%x in ("%MYSQL_HOME%") do set MYSQL_HOME=%%~sx
for %%x in ("%LUOSHU_HOME%") do set LUOSHU_HOME=%%~sx

%MYSQL_HOME%\bin\mysql.exe -h localhost -uroot -proot luoshu < %LUOSHU_HOME%\bin\luoshu.sql