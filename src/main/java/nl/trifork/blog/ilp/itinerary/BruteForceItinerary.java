package nl.trifork.blog.ilp.itinerary;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BruteForceItinerary {

    public static void main(String args[]){
        // List of pois
        List<String> pois = IntStream.range(0, 1000)
                .mapToObj(i -> "poi" + i)
                .collect(Collectors.toList());

        // Weight per poi
        Map<String, Integer> weightPerPoi = new HashMap<>();
        Random random = new Random();
        for (String poi : pois) {
            weightPerPoi.put(poi, random.nextInt(10000) + 1);
        }


        BigInteger loopCounter = new BigInteger("0");
        BigInteger totalLoops = new BigInteger("1000000000000000000");
        BigInteger showProgressInterval = new BigInteger("10000000");
        long start = System.currentTimeMillis();
        int maxSum = 0;
        for (String poiTimeSlot1 : pois) {
            for (String poiTimeSlot2 : pois) {
                for (String poiTimeSlot3 : pois) {
                    for (String poiTimeSlot4 : pois) {
                        for (String poiTimeSlot5 : pois) {
                            for (String poiTimeSlot6 : pois) {
                                // Calculate the sum of recommendations
                                int sum = weightPerPoi.get(poiTimeSlot1) +
                                        weightPerPoi.get(poiTimeSlot2) +
                                        weightPerPoi.get(poiTimeSlot3) +
                                        weightPerPoi.get(poiTimeSlot4) +
                                        weightPerPoi.get(poiTimeSlot5) +
                                        weightPerPoi.get(poiTimeSlot6);

                                // Check if we have a new best solution
                                if(sum > maxSum){
                                    maxSum = sum;
                                    List<String> solution = Arrays.asList(poiTimeSlot1,
                                            poiTimeSlot2,
                                            poiTimeSlot3,
                                            poiTimeSlot4,
                                            poiTimeSlot5,
                                            poiTimeSlot6);
                                    System.out.println("Found new best solution: " + solution + ", sum:" + sum);
                                }

                                // Update the progress
                                loopCounter = loopCounter.add(BigInteger.ONE);

                                // Print estimation of total run time
                                if(loopCounter.mod(showProgressInterval).equals(BigInteger.ZERO)){
                                    BigDecimal ratioComplete = new BigDecimal(loopCounter).divide(new BigDecimal(totalLoops));
                                    BigDecimal multiplierTodo = BigDecimal.ONE.divide(ratioComplete, MathContext.DECIMAL128);
                                    BigDecimal totalDurationInMilliseconds = multiplierTodo.multiply(new BigDecimal(System.currentTimeMillis() - start));
                                    BigInteger durationInYears = totalDurationInMilliseconds.divide(new BigDecimal(1000L * 60L * 60L * 24L * 365L), MathContext.DECIMAL128).toBigInteger();
                                    System.out.println(String.format("Done %s out of %s which is %s %%", loopCounter, totalLoops,
                                            new BigDecimal(loopCounter)
                                                    .divide(new BigDecimal(totalLoops))
                                                    .multiply(new BigDecimal(100))
                                                    .toPlainString()));
                                    System.out.println(String.format("Total duration in years: %s", durationInYears));
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
