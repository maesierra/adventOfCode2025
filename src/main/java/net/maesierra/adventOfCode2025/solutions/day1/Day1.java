package net.maesierra.adventOfCode2025.solutions.day1;

import net.maesierra.adventOfCode2025.Runner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

public class Day1 implements Runner.Solution {

    public static final Pattern LINE_REGEXP = Pattern.compile("(\\d+) +(\\d+)");

    record Pair<T>(T left, T right) {}

    @Override
    public String part1(InputStream input, String... params) {
        return Integer.toString(0);
    }

    @Override
    public String part2(InputStream input, String... params) {
        return Integer.toString(0);
    }
}
