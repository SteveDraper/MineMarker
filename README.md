# MineMarker

## Environment requirements
* An installed JVM (ideally 1.8 which is where it has been tested, but 1.7+ should suffice) and (to build) JDK
* On Windows, an environment variable called JAVA_HOME set to the JDK path.  This is necessary for the gradle java plugin to operate correctly, in order to find tools.jar (LINUX JDK installs set up the necessary paths but for some reason it needs manual setup on Windows). Typically the value will be something like 'C:\Program Files\Java\jdk1.8.0_51')

## Build
* To build on Windows `gradlew build`.  On LINUX `bash gradlew build`
* To run tests `gradlew test` (or `bash gradlew test` on LINUX)

## To use 
Copy the JAR file from build/libs/MineMarker.jar to your working directory (or set working directory there), then for full usage syntax run `java -jar MineMarker.jar` without parameters

## Project Structure

* Source for the runtime JAR may be found in ./src/main/java
* Unit tests are in ./src/test/java
* Major design decisions are documented in ./designNotes.md
