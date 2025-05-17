@REM SBT launcher script

@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (mandatory)
@REM SBT_OPTS  - JVM options (optional)
@REM Configuration:
@REM sbtconfig.txt found in the SBT_HOME.

@REM We need delayed expansion to build up CFG_OPTS later
@setlocal enabledelayedexpansion

@echo off
set SBT_HOME=%~dp0

rem Load the config file of extra options.
set FN=%SBT_HOME%\..\conf\sbtconfig.txt
set CFG_OPTS=
FOR /F "tokens=* eol=# usebackq delims=" %%i IN ("%FN%") DO (
  set DO_NOT_REUSE_ME=%%i
  rem Use delayed expansion for CFG_OPTS
  set CFG_OPTS=!CFG_OPTS! !DO_NOT_REUSE_ME!
)

rem Use the value of the JAVACMD environment variable if defined
set _JAVACMD=%JAVACMD%

if "%_JAVACMD%"=="" (
  if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem Use the value of the JAVA_OPTS environment variable if defined, otherwise use the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "%_JAVA_OPTS%"=="" set _JAVA_OPTS=%CFG_OPTS%

:run

"%_JAVACMD%" %_JAVA_OPTS% %SBT_OPTS% -cp "%SBT_HOME%sbt-launch.jar" xsbt.boot.Boot %*
if ERRORLEVEL 1 goto error
goto end

:error
@endlocal
exit /B 1

:end
@endlocal
exit /B 0

rem Limit memory usage to 256MB
java -Xmx256M -Xms256M -XX:MaxMetaspaceSize=256M -jar sbt-launch.jar %*
