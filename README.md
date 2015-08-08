# MineMarker

## Environment requirements
* An installed JVM (ideally 1.8 and that is where it has been tested, but 1.6+ should suffice) and (to build) JDK
* On Windows, an environment variable called JAVA_HOME set to the JDK path.  This is necessary for the gradle java plugin to operate correctly, in order to find tools.jar (LINUX JDK installs set up the necessary paths but for some reason it needs manual setup on Windows). Typically the value will be something like 'C:\Program Files\Java\jdk1.8.0_45')

## Build
* To build on Windows `gradlew build`.  On LINUX `bash gradlew build`
* To run tests `gradlew test` (or `bash gradlew test` on LINUX)

## To use 
Copy the JAR file from build/libs/MineMarker.jar to your working directory (or set working directory there), then for full usage syntax run `java -jar MineMarker.jar` without parameters
