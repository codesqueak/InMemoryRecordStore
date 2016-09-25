# In Memory Record Storage

In Memory Record Storage is a library to allow class data defined as records to be efficiently stored into memory

## Build

(Windows)
gradlew clean build test

(Linux)
./gradlew clean build test


## Using Jenkins

The project includes a Jenkins file to control a pipeline build.  At present the available version of the Jacoco plugin (2.0.1 at time of writing) does not support a 'publisher'.  The build was tested using a hand built plugin from the master branch of the  [project](https://github.com/jenkinsci/jacoco-plugin)






