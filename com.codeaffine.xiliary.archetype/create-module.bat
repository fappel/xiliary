@echo off

echo Start creation of module projects
echo.

rem Input plugin id and compute test- and feature-id
set /p artifactId="Please input the plug-in id and press enter to continue: "
set testArtifactId=%artifactId%.test
set featureArtifactId=%artifactId%.feature

rem Input plugin name and compute test- and feature-name
set /p bundleName="Please input the plug-in name and press enter to continue: "
set testName=%bundleName% Tests
set featureName=%bundleName%

rem Input plugin version used for maven and bundle/feature
set /p version="Please input the plug-in version and press enter to continue: "

rem configure common settings
set groupId=com.codeaffine.xiliary
set javaEnvironment=JavaSE-1.6

rem configure maven archetypes
set archetypeGroupId=com.codeaffine.xiliary
set archetypeVersion=0.1.0-SNAPSHOT
set archetypePlugin=com.codeaffine.xiliary.archetype-eclipse-plugin
set archetypeTestFragment=com.codeaffine.xiliary.archetype-eclipse-test-fragment
set archetypeFeature=com.codeaffine.xiliary.archetype-eclipse-feature

echo. 
echo [CREATE] Plug-in: %artifactId%, %bundleName%
echo.

call %M2_HOME%\bin\mvn ^
  archetype:generate ^
  -DarchetypeGroupId=%archetypeGroupId% ^
  -DarchetypeArtifactId=%archetypePlugin% ^
  -DarchetypeVersion=%archetypeVersion% ^
  -DgroupId=%groupId% ^
  -DartifactId=%artifactId% ^
  -DbundleName="%bundleName%" ^
  -Dversion=%version% ^
  -DjavaEnvironment=%javaEnvironment% ^
  -DinteractiveMode=false

echo.
echo [CREATE] Test-Fragment: %testArtifactId% , %testName%
echo.

call %M2_HOME%\bin\mvn ^
  archetype:generate ^
  -DarchetypeGroupId=%archetypeGroupId% ^
  -DarchetypeArtifactId=%archetypeTestFragment% ^
  -DarchetypeVersion=%archetypeVersion% ^
  -DgroupId=%groupId% ^
  -DartifactId=%testArtifactId% ^
  -DbundleName="%testName%" ^
  -DpluginId=%artifactId% ^
  -Dversion=%version% ^
  -DjavaEnvironment=%javaEnvironment% ^
  -DinteractiveMode=false

echo.
echo [CREATE] Feature: %featureArtifactId%, %featureName%
echo.

call %M2_HOME%\bin\mvn ^
  archetype:generate ^
  -DarchetypeGroupId=%archetypeGroupId% ^
  -DarchetypeArtifactId=%archetypeFeature% ^
  -DarchetypeVersion=%archetypeVersion% ^
  -DgroupId=%groupId% ^
  -DartifactId=%featureArtifactId% ^
  -DbundleName="%featureName%" ^
  -DpluginId=%artifactId% ^
  -Dversion=%version% ^
  -DinteractiveMode=false
  
echo.
echo Module creation done!
echo Do not forget to complement the parent 'pom' and the category.xml in the xiliary releng project. 
