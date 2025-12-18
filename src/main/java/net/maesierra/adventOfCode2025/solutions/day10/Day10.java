package net.maesierra.adventOfCode2025.solutions.day10;

import net.maesierra.adventOfCode2025.Runner;
import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.StringUtils;
import org.paukov.combinatorics3.Generator;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;
import static net.maesierra.adventOfCode2025.utils.Logger.info;
import static net.maesierra.adventOfCode2025.utils.MathHelper.ReducedRowEchelon.reducedRowEchelon;
import static net.maesierra.adventOfCode2025.utils.MathHelper.nonZero;

public class Day10 implements Runner.Solution {


    static class Lights extends HashMap<Integer, Boolean> {
        public Lights(List<Boolean> lights) {
            super();
            for (int i = 0; i <lights.size(); i++) {
                put(i, lights.get(i));
            }
        }

        void toggle(Integer pos) {
            put(pos, !get(pos));
        }
    }
    static class Button extends ArrayList<Integer> {
        public Button(Collection<? extends Integer> c) {
            super(c);
        }
    }

    record State(Lights expectedLights, List<Button> buttons) {

        boolean check(List<Button> buttons) {
            Lights lights = new Lights(this.expectedLights.values().stream().map(ignored -> false).toList());
            for (var button:buttons) {
                button.forEach(lights::toggle);
            }
            return this.expectedLights.equals(lights);
        }

        @Override
        public String toString() {
            return "State{" +
                    ", buttons=" + buttons +
                    ", expectedLights=" + expectedLights +
                    '}';
        }

        List<Button> resolve() {
            int size = 1;
            while (size <= buttons.size()) {
                //Generate all the buttons combinations for the given size, and return it if any of them get the right light state
                //if not, try with a bigger size
                Optional<List<Button>> found = Generator.combination(buttons)
                        .simple(size)
                        .stream()
                        .filter(this::check)
                        .findFirst();
                if (found.isPresent()) {
                    return found.get();
                }
                size++;
            }
            throw new IllegalStateException();
        }

    }

    @Override
    public String part1(InputStream input, String... params) {
        var states = inputAsStream(input, Pattern.compile("\\[(.*)] (\\(.*\\)) \\{(.*)}$")).map(parts -> {
            Lights expectedLights = new Lights(parts[0].chars().mapToObj(i -> (char) i == '#').toList());
            List<Button> buttons = Stream.of(parts[1].split(" "))
                    .map(numbers -> new Button(Stream.of(numbers.split("[(),]")).filter(StringUtils::isNotEmpty).map(Integer::parseInt).toList()))
                    .toList();
            return new State(expectedLights, buttons);
        }).toList();
        int res = 0;
        for (var state:states) {
            List<Button> pushedButtons = state.resolve();
            info(pushedButtons.toString());
            res += pushedButtons.size();
        }
        return Integer.toString(res);
    }
    record Joltage(int index, int value) {}

    static final class Machine {
        private final List<Button> buttons;
        private final List<Joltage> joltages;
        private final Map<CacheKey, List<Map<Button, Integer>>> combinationsCache = new HashMap<>();

        record CacheKey(List<Button> buttons, int n) {

        }

        Machine(List<Button> buttons, List<Joltage> joltages) {
            this.buttons = buttons;
            this.joltages = joltages;

        }

        public boolean check(Map<Integer, Double> pushedButtons) {
            double[] counters = new double[joltages.size()];
            Arrays.fill(counters, 0);
            pushedButtons.forEach((nButton, nTimes) -> {
                Button button = this.buttons().get(nButton);
                for (var pos:button) {
                    counters[pos] += nTimes;
                }
            });
            for (var joltage : joltages) {
                double currentValue = counters[joltage.index()];
                if (Math.abs(currentValue - joltage.value()) > 0.0001) {
                    return false;
                }
            }
            return true;
        }


        public List<Button> buttons() {
            return buttons;
        }

        public List<Joltage> joltages() {
            return joltages;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Machine) obj;
            return Objects.equals(this.buttons, that.buttons) &&
                    Objects.equals(this.joltages, that.joltages);
        }

        @Override
        public int hashCode() {
            return Objects.hash(buttons, joltages);
        }

        @Override
        public String toString() {
            return "Machine[" +
                    "buttons=" + buttons + ", " +
                    "joltages=" + joltages + ']';
        }

    }

    record Variable(int index, double coefficient){

    }

    record Equation(List<Variable> variables, double value) {

        Variable leadingVariable() {
            return Objects.requireNonNull(variables.getFirst());
        }

        Map<Integer, Double> solve(Map<Integer, Integer> values) {
            //the equation should be leadingVariable = -otherVariable1*coefficient - otherVariable2*coefficient + value
            double value = this.value;
            for (int i = 1; i < variables.size(); i++) {
                Variable variable = variables.get(i);
                if (!values.containsKey(variable.index)) {
                    throw new IllegalStateException("Value %d needs to be set to solve equation %s".formatted(variable.index, this.toString()));
                }
                value -= values.get(variable.index) * variable.coefficient;
            }
            return Map.of(leadingVariable().index, value);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("x%s ".formatted(leadingVariable().index));
            for (int i = 1; i < variables.size(); i++) {
                Variable variable = variables.get(i);
                String coefficient = Math.abs(variable.coefficient) == 1 ? "" : Double.toString(Math.abs(variable.coefficient));
                str.append(" %s %sx%s".formatted(variable.coefficient > 0 ? "+" : "-", coefficient, variable.index));
            }
            str.append(" = %f".formatted(this.value));
            return str.toString();
        }
    }


    record Solution(Map<Integer, Integer> values, int size) implements Comparable<Solution> {
        Solution(Map<Integer, Integer> values) {
            this(values, values.values().stream().mapToInt(i -> i).sum());
        }

        @Override
        public int compareTo(Solution o) {
            return Integer.compare(this.size, o.size);
        }
    }


    @Override
    public String part2(InputStream input, String... params) {
        var machines = inputAsStream(input, Pattern.compile("\\[(.*)] (\\(.*\\)) \\{(.*)}$")).map(parts -> {
            List<Button> buttons = Stream.of(parts[1].split(" "))
                    .map(numbers -> new Button(Stream.of(numbers.split("[(),]")).filter(StringUtils::isNotEmpty).map(Integer::parseInt).toList()))
                    .toList();
            List<Joltage> joltageList = new ArrayList<>();
            List<Integer> joltages = Stream.of(parts[2].split("[{},]")).filter(StringUtils::isNotEmpty).map(Integer::parseInt).toList();
            for (int i = 0; i < joltages.size(); i++) {
                joltageList.add(new Joltage(i, joltages.get(i)));
            }
            return new Machine(buttons, joltageList);
        }).toList();
        long result  = 0L;
        for (Machine machine : machines) {
            //Convert the buttons and required joltages to an augmented matrix
            //eg
            //(0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            //can be put as
            //b0 + b2 = 7
            //b3 + b4 = 5
            //b0 + b1 + b3 + b4 = 12
            //b0 + b1 + b4 = 7
            //b0 + b2 + b4 = 2
            double[][] matrix = new double[machine.joltages().size()][machine.buttons().size() + 1];
            for (int i = 0; i < machine.joltages().size(); i++) {
                Joltage joltage = machine.joltages().get(i);
                for (int j = 0; j < machine.buttons().size(); j++) {
                    Button button = machine.buttons.get(j);
                    if (button.contains(joltage.index())) {
                        matrix[i][j] = 1;
                    } else {
                        matrix[i][j] = 0;
                    }
                }
                matrix[i][machine.buttons.size()] = joltage.value();
            }
            //reduce
            double[][] reducedRowEchelon = reducedRowEchelon(matrix);
            List<Equation> equations = new ArrayList<>();
            for (double[] row : reducedRowEchelon) {
                List<Variable> variables = new ArrayList<>();
                for (int column = 0; column < row.length - 1; column++) {
                    if (nonZero(row[column])) {
                        variables.add(new Variable(column, row[column]));
                    }
                }
                if (!variables.isEmpty()) {
                    equations.add(new Equation(variables, row[row.length - 1]));
                }
            }
            //calculate the free variables
            List<Integer> freeVariables = new ArrayList<>();
            Set<Integer> leadingVariables = equations.stream().map(e -> e.leadingVariable().index).collect(Collectors.toSet());
            int nVariables = reducedRowEchelon[0].length;
            for (int index = 0; index < nVariables - 1; index++) {
                //If the index is not a leading variable in any of the equations -> it's free
                if (!leadingVariables.contains(index)) {
                    freeVariables.add(index);
                }
            }
            //we know all the solutions must be in the joltages range
            IntegerRange solutionsRange = IntegerRange.of(0, machine.joltages().stream().mapToInt(Joltage::value).max().orElseThrow());
            Queue<Solution> candidates = new ArrayDeque<>();
            candidates.add(new Solution(Map.of()));
            for (var variableIndex:freeVariables) {
                Queue<Solution> newCandidates = new ArrayDeque<>();
                while (!candidates.isEmpty()) {
                    Solution current = candidates.poll();
                    solutionsRange.toIntStream().forEach(value -> {
                        Map<Integer, Integer> solution = new HashMap<>(current.values);
                        solution.put(variableIndex, value);
                        newCandidates.add(new Solution(solution));
                    });
                }
                candidates = newCandidates;
            }
            int min = Integer.MAX_VALUE;
            while (!candidates.isEmpty()) {
                Solution solution = candidates.poll();
                Map<Integer, Double> solutionResult = new HashMap<>();
                solution.values.forEach((i, v) -> solutionResult.put(i, (double)v));
                for (var equation:equations) {
                    solutionResult.putAll(equation.solve(solution.values));
                }
                //We can only accept solutions that have positive values and within tolerance
                if (solutionResult.values().stream().anyMatch(i -> i < -0.0001)) {
                    continue;
                }
                if (solutionResult.values().stream().anyMatch(i -> {
                    double remainder = i % 1.0;
                    return remainder > 0.0001 && remainder < 0.9999;
                })) {
                    continue;
                }
                int solutionSize = (int) solutionResult.values().stream().mapToDouble(v -> v).sum();
                if (solutionSize >= min) {
                    continue;
                }
                if (machine.check(solutionResult)) {
                    min = solutionSize;
                }
            }
            info("Machine %s -> %d", machine.joltages.stream().map(Joltage::value).toList().toString(), min);
            result += min;

        }
        return Long.toString(result);
    }


}
