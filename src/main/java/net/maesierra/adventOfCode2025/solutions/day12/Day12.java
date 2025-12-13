package net.maesierra.adventOfCode2025.solutions.day12;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Matrix;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsTextBlocks;

public class Day12 implements Runner.Solution {

    record Shape(int index, Boolean[][] shape, int size) {

        public Shape(int index, Boolean[][] shape) {
            this(index, shape, (int) Stream.of(shape).flatMap(Stream::of).filter(t -> t).count());
        }

        @Override
        public String toString() {
            return Stream.of(shape).map(line -> Stream.of(line).map(i -> i ? "#" : ".").collect(Collectors.joining())).collect(Collectors.joining("\n"));
        }


    }

    record Region(Matrix<AtomicBoolean> grid, Map<Shape, Integer> requiredShapes) {


        boolean canFit() {
            int minSizeToMatch = requiredShapes.values().stream().mapToInt(i -> i).sum() * 9;
            return grid().nRows() * grid().nCols() >= minSizeToMatch;
        }
    }


    @Override
    public String part1(InputStream input, String... params) {
        Map<Integer, Shape> shapes = new HashMap<>();
        List<Stream<String>> blocks = Arrays.stream(inputAsTextBlocks(input)).toList();
        Pattern idPattern = Pattern.compile("(\\d+):");
        for (var  lines: blocks.subList(0, blocks.size() - 1).stream().map(Stream::toList).toList()) {
            Matcher m = idPattern.matcher(lines.getFirst());
            if (!m.matches()) {
                continue;
            }
            int id = Integer.parseInt(m.group(1));
            Boolean[][] shape = new Boolean[3][3];
            for (int i = 1; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    shape[i - 1][j] = lines.get(i).charAt(j) == '#';
                }
            }
            shapes.put(id, new Shape(id, shape));
        };
        Pattern regionPattern = Pattern.compile("(\\d+)x(\\d+): (.*)$");
        List<Region> regions = blocks.getLast().map(line -> {
            Matcher m = regionPattern.matcher(line);
            if (!m.matches()) {
                throw new IllegalStateException("No match found");
            }
            Matrix<AtomicBoolean> grid = new Matrix<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), () -> new AtomicBoolean(false));
            Map<Shape, Integer> requiredShapes = new HashMap<>();
            String[] parts = m.group(3).split(" ");
            for (int i = 0; i < parts.length; i++) {
                int count = Integer.parseInt(parts[i]);
                if (count > 0) {
                    requiredShapes.put(shapes.get(i), count);
                }
            }
            return new Region(grid, requiredShapes);
        }).toList();

        long res = 0L;
        for (var region:regions) {
            if (region.canFit()) {
                res++;
            }
        }
        return Long.toString(res);

    }

    @Override
    public String part2(InputStream input, String... params) {
        return "";
    }
}
