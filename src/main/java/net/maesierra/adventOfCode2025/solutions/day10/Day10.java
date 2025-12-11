package net.maesierra.adventOfCode2025.solutions.day10;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.CollectionHelpers.findCombinations;
import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

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
                List<List<Button>> candidates = findCombinations(buttons, size);
                for (var candidate:candidates){
                    if (check(candidate)) {
                        return candidate;
                    }
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
            Logger.info(pushedButtons.toString());
            res += pushedButtons.size();
        }
        return Integer.toString(res);
    }

    @Override
    public String part2(InputStream input, String... params) {
        return "";
    }

}
