package nl.trifork.blog.ilp.planning;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IlpSolver {

    public void solve() {
        List<String> pois = IntStream.range(0, 1000)
                .mapToObj(i -> "poi" + i)
                .collect(Collectors.toList());

        List<String> timeslots = IntStream.range(0, 6)
                .mapToObj(i -> "timeslot" + i)
                .collect(Collectors.toList());


        MPSolver solver = createSolver();
        List<MPVariable> allVariables = new ArrayList<>();
        Map<String, List<MPVariable>> variablesGroupedByPoi = new HashMap<>();
        Map<String, List<MPVariable>> variablesGroupedByTimeslot = new HashMap<>();
        for (String timeslot : timeslots) {
            for (String poi : pois) {
                MPVariable mpVariable = solver.makeIntVar(0.0, MPSolver.infinity(), timeslot + "_" + poi);

                allVariables.add(mpVariable);

                // Keep track of the variables per poi
                if (!variablesGroupedByPoi.containsKey(poi)) {
                    variablesGroupedByPoi.put(poi, new ArrayList<>());
                }
                variablesGroupedByPoi.get(poi).add(mpVariable);

                // Keep track of the variables per timeslot
                if (!variablesGroupedByTimeslot.containsKey(timeslot)) {
                    variablesGroupedByTimeslot.put(timeslot, new ArrayList<>());
                }
                variablesGroupedByTimeslot.get(timeslot).add(mpVariable);
            }
        }

        // Create objective function
        MPObjective objective = solver.objective();
        Random random = new Random();
        for (MPVariable mpVariable : allVariables) {
            int randomRecommendationScore = random.nextInt(10000) + 1;
            // Assign a random recommendation score to the variable
            objective.setCoefficient(mpVariable, randomRecommendationScore);
        }
        // Maximize the outcome of the object function
        objective.setMaximization();

        // Set the constraints to visit poi at most once in all timeslots
        for (Map.Entry<String, List<MPVariable>> mpVariablesPerPoi : variablesGroupedByPoi.entrySet()) {
            double constraintMinimumValue = 0D;
            double constraintMaximumValue = 1D;
            String constraintDescription = "visit poi " + mpVariablesPerPoi.getKey() + " at most once";
            MPConstraint constraint = solver.makeConstraint(
                    constraintMinimumValue,
                    constraintMaximumValue,
                    constraintDescription);

            for (MPVariable mpVariable : mpVariablesPerPoi.getValue()) {
                double multiplierForVariable = 1D;
                constraint.setCoefficient(mpVariable, multiplierForVariable);
            }
        }

        // Set the constraints to use timeslot at most once for all pois
        for (Map.Entry<String, List<MPVariable>> mpVariablesPerTimeslot : variablesGroupedByTimeslot.entrySet()) {
            double constraintMinimumValue = 0D;
            double constraintMaximumValue = 1D;
            String constraintDescription = "use timeslot " + mpVariablesPerTimeslot.getKey() + " at most once";
            MPConstraint constraint = solver.makeConstraint(
                    constraintMinimumValue,
                    constraintMaximumValue,
                    constraintDescription);

            for (MPVariable mpVariable : mpVariablesPerTimeslot.getValue()) {
                double multiplierForVariable = 1D;
                constraint.setCoefficient(mpVariable, multiplierForVariable);
            }
        }

        MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            System.err.println("The problem does not have an optimal solution!");
            return;
        }

        // Print solution
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
        System.out.println("Optimal objective value = " + solver.objective().value());
        for (MPVariable mpVariable : allVariables) {
            if (mpVariable.solutionValue() > 0) {
                System.out.println("Visit: " + mpVariable.name() + ", weight: " + objective.getCoefficient(mpVariable));
            }
        }
    }

    private MPSolver createSolver() {
        try {
            return new MPSolver("LinearProgrammingExample", MPSolver.OptimizationProblemType.GLOP_LINEAR_PROGRAMMING);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void main(String args[]){
        try {
            System.load(new ClassPathResource("lib/" + args[0] + "/libjniortools.so").getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load or tools library. Please specify platform as argument. " +
                    "Available platforms are: linux64, mac64 or win64");
        }

        IlpSolver ilpSolver = new IlpSolver();
        ilpSolver.solve();
    }
}
