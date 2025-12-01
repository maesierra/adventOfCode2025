package net.maesierra.adventOfCode2025.solutions.day1;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.IOHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

public class Day1 implements Runner.Solution {

    enum Direction {
        LEFT,
        RIGHT;

        static Direction of(String value) {
            return switch (value) {
                case "R" -> RIGHT;
                case "L" -> LEFT;
                default -> throw new IllegalStateException("Invalid value");
            };
        }
    }

    private class Dial {
        private int position = 50;
        private final int min = 0;
        private final int max = 99;
        private int zeroHitCounter = 0;

        public void rotate(Direction direction, int value) {
            int step = direction == Direction.RIGHT ? 1 : -1;
            for (var i = 0; i < value; i++) {
                this.position = this.position + step;
                switch (this.position) {
                    case 0 -> this.zeroHitCounter++;
                    case -1 ->  this.position = 99;
                    case 100 -> {
                        this.position = 0;
                        this.zeroHitCounter++;
                    }
                }
            }
        }

    }

    @Override
    public String part1(InputStream input, String... params) {
        Dial dial = new Dial();
        AtomicInteger counter = new AtomicInteger(0);
        IOHelper.inputAsStream(input, Pattern.compile("([LR])(\\d+)")).forEach(strings -> {
            dial.rotate(Direction.of(strings[0]), Integer.parseInt(strings[1]));
            System.out.printf("The dial is rotated %s%s to point at %d.%n", strings[0], strings[1], dial.position);
            if (dial.position == 0) {
                counter.incrementAndGet();
            }
        });

        return Integer.toString(counter.get());
    }

    @Override
    public String part2(InputStream input, String... params) {
        Dial dial = new Dial();
        AtomicInteger counter = new AtomicInteger(0);
        IOHelper.inputAsStream(input, Pattern.compile("([LR])(\\d+)")).forEach(strings -> {
            dial.rotate(Direction.of(strings[0]), Integer.parseInt(strings[1]));
            System.out.printf("The dial is rotated %s%s to point at %d.%n", strings[0], strings[1], dial.position);
        });

        return Integer.toString(dial.zeroHitCounter);
    }
}
