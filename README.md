# itinerary-blog
Example code accompanying itinerary blog article

For more information see the blog article at:
https://blog.trifork.com/2016/02/01/personalised-city-trip-itinerary-using-integer-linear-programming/

## Instructions

### Install the google or tools library

mvn install:install-file -DgroupId=com.google.ortools -DartifactId=ortools -Dversion=0.3300 -Dpackaging=jar -Dfile=lib/com.google.ortools.jar

### Running the examples

Run LargeItineraryILP.java with:

mvn clean package exec:java -Dexec.mainClass=”nl.trifork.blog.ilp.itinerary.LargeItineraryILP”

Run SmallItineraryILP.java with:

mvn clean package exec:java -Dexec.mainClass=”nl.trifork.blog.ilp.itinerary.SmallItineraryILP”

Run LargeItineraryBruteForce.java with:

mvn clean package exec:java -Dexec.mainClass=”nl.trifork.blog.ilp.itinerary.LargeItineraryBruteForce”

## Native libraries

I’ve included the native libraries for windows, linux and mac. The program will try to detect your os to load the correct library. I’ve only included the 64 bit libraries. If you need libraries for a different os or architecture you can find the libraries under the java section at the OR tools website: https://developers.google.com/optimization/installing