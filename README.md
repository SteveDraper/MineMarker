# MineMarker

## Environment requirements
* An installed JVM (ideally 1.8 and that is where it has been tested, but 1.6+ should suffice) and JRE
* An environment variable called JAVA_HOME set to the JDK path (on LINUX a JRE install will normally have set this up, but on Windows it appears not to and must be manually set.  Typically the value will be something like 'C:\Program Files\Java\jdk1.8.0_45')

## Build
* To build on Windows `gradlew build`.  On LINUX `bash gradlew build`
* To run tests `gradlew test` (or `bash gradlew test` on LINUX)

## To use 
Copy the JAR file from build/libs/mineMarker.jar to your working directory (or set working directory there), then for full usage syntax run `mineMarker.jar` without parameters
