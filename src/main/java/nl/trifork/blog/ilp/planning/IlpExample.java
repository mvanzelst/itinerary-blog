package nl.trifork.blog.ilp.planning;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import nl.trifork.blog.ilp.planning.util.NativeLibraryLoader;

public class IlpExample {

    public void solve() {
        MPSolver solver = new MPSolver("LinearProgrammingExample", MPSolver.OptimizationProblemType.GLOP_LINEAR_PROGRAMMING);

        // Create integer variables a,b,c and d with a lower limit of 0 and an upper limit of 1
        MPVariable rijksmuseumMorning = solver.makeIntVar(0, 1, "a");
        MPVariable rijksmuseumAfternoon = solver.makeIntVar(0, 1, "b");
        MPVariable vanGoghMuseumMorning = solver.makeIntVar(0, 1, "c");
        MPVariable vanGoghMuseumAfternoon = solver.makeIntVar(0, 1, "d");

        // Create objective function
        MPObjective objective = solver.objective();

        // Set the variables and their multipliers (recommendation scores)
        objective.setCoefficient(rijksmuseumMorning, 6);
        objective.setCoefficient(rijksmuseumAfternoon, 9);
        objective.setCoefficient(vanGoghMuseumMorning, 5);
        objective.setCoefficient(vanGoghMuseumAfternoon, 5);

        // The goal is to maximize outcome of the objective function
        objective.setMaximization();

        // Make a constraint with a minimum outcome of 0 and a maximum outcome of 1
        MPConstraint constraint1 = solver.makeConstraint(0, 1, "Only visit only one museum in the morning");
        constraint1.setCoefficient(rijksmuseumMorning, 1);
        constraint1.setCoefficient(vanGoghMuseumMorning, 1);

        // Make a constraint with a minimum outcome of 0 and a maximum outcome of 1
        MPConstraint constraint2 = solver.makeConstraint(0, 1, "Only visit only one museum in the afternoon");
        constraint2.setCoefficient(rijksmuseumAfternoon, 1);
        constraint2.setCoefficient(vanGoghMuseumAfternoon, 1);

        // Make a constraint with a minimum outcome of 0 and a maximum outcome of 1
        MPConstraint constraint3 = solver.makeConstraint(0, 1, "Only visit the Rijksmuseum once");
        constraint3.setCoefficient(rijksmuseumMorning, 1);
        constraint3.setCoefficient(rijksmuseumAfternoon, 1);

        // Make a constraint with a minimum outcome of 0 and a maximum outcome of 1
        MPConstraint constraint4 = solver.makeConstraint(0, 1, "Only visit the van Gogh museum once");
        constraint4.setCoefficient(vanGoghMuseumMorning, 1);
        constraint4.setCoefficient(vanGoghMuseumAfternoon, 1);

        solver.solve();

        System.out.println("Optimal objective value = " + (int) solver.objective().value());

        System.out.println("Visit the Rijksmuseum in the morning " + (int) rijksmuseumMorning.solutionValue() + " times");
        System.out.println("Visit the Rijksmuseum in the afternoon " + (int) rijksmuseumAfternoon.solutionValue() + " times");
        System.out.println("Visit the van Gogh museum in the morning " + (int) vanGoghMuseumMorning.solutionValue() + " times");
        System.out.println("Visit the van Gogh museum in the afternoon " + (int) vanGoghMuseumAfternoon.solutionValue() + " times");
    }

    public static void main(String args[]){
        NativeLibraryLoader.loadOrTools();

        IlpExample ilpExample = new IlpExample();
        ilpExample.solve();
    }
}
