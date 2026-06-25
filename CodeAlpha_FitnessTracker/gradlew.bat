@ECHO OFF
SETLOCAL
SET DIRNAME=%~dp0
IF "%DIRNAME%"=="" SET DIRNAME=.
SET APP_HOME=%DIRNAME%
SET CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

IF DEFINED JAVA_HOME GOTO findJavaFromJavaHome
SET JAVACMD=java.exe
%JAVACMD% -version >NUL 2>&1
IF %ERRORLEVEL% EQU 0 GOTO execute
ECHO ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
EXIT /B 1

:findJavaFromJavaHome
SET JAVACMD=%JAVA_HOME%\bin\java.exe
IF EXIST "%JAVACMD%" GOTO execute
ECHO ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
EXIT /B 1

:execute
"%JAVACMD%" %JAVA_OPTS% %GRADLE_OPTS% -Dorg.gradle.appname=gradlew -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
ENDLOCAL
EXIT /B %ERRORLEVEL%
