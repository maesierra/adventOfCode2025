package net.maesierra.adventOfCode2025.solutions.day2;

import net.maesierra.adventOfCode2025.Runner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

public class Day2 implements Runner.Solution {

    public static final String REGEX_TEMPLATE_MULTIPLE_MATCHES = "(%s)(%s)+";
    public static final String REGEX_TEMPLATE_SINGLE_MATCH = "(%s)(%s)";

    record Range(long min, long max) {

        public List<Long> getInvalid(boolean multipleMatches) {
            List<Long> res = new ArrayList<>();
            for (long i = min; i <= max; i++) {
                String number = Long.toString(i);
                for (int len = 1; len <= number.length() / 2; len++) {
                    String block = number.substring(0, len);
                    if (number.matches((multipleMatches ? REGEX_TEMPLATE_MULTIPLE_MATCHES : REGEX_TEMPLATE_SINGLE_MATCH).formatted(block, block))) {
                        res.add(i);
                        break;
                    }
                }
            }
            return res;
        }
    }


    @Override
    public String part1(InputStream input, String... params) {
        long res = inputAsStream(input)
                .flatMap(s -> Stream.of(s.split(",")))
                .flatMapToLong(s -> {
                    String[] numbers = s.split("-");
                    Range range = new Range(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
                    return range.getInvalid(false).stream().mapToLong(l -> l);
                }).sum();

        return Long.toString(res);
    }

    @Override
    public String part2(InputStream input, String... params) {
        long res = inputAsStream(input)
                .flatMap(s -> Stream.of(s.split(",")))
                .flatMapToLong(s -> {
                    String[] numbers = s.split("-");
                    Range range = new Range(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
                    return range.getInvalid(true).stream().mapToLong(l -> l);
                }).sum();

        return Long.toString(res);
    }
}
