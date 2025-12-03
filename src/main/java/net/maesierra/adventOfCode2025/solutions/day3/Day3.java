package net.maesierra.adventOfCode2025.solutions.day3;

import net.maesierra.adventOfCode2025.Runner;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;

public class Day3 implements Runner.Solution {

    record Rank(List<Integer> batteries) {
        public Rank(String numbersList) {
                this(numbersList.codePoints().mapToObj(c -> Integer.parseInt(String.valueOf((char) c))).toList());
            }

            public long largestJoltage(int nDigits) {
                return largestJoltage(nDigits, this.batteries);
            }

            public long largestJoltage(int nDigits, List<Integer> batteries) {
                if (nDigits == 1) {
                    return batteries.stream().max(Comparator.naturalOrder()).orElseThrow();
                }
                if (batteries.size() == nDigits) {
                    long res = 0;
                    for (int i = 0; i < nDigits; i++) {
                        long multiplier = (long) (Math.pow(10, nDigits - i - 1));
                        res += multiplier * batteries.get(i);
                    }
                    return res;
                }

                long max = Long.MIN_VALUE;
                long multiplier = (long) (Math.pow(10, nDigits - 1));
                for (int i = 0; i < batteries.size(); i++) {
                    int number = batteries.get(i);
                    if (number * multiplier < max) {
                        //no point in trying
                        continue;
                    }
                    List<Integer> remaining = batteries.subList(i + 1, batteries.size());
                    if (remaining.size() + 1 < nDigits) {
                        continue;
                    }
                    max = Math.max(max, (number * multiplier) + largestJoltage(nDigits - 1, remaining));
                }
                return max;
            }

        }

    private static String calculateLargestJoltage(InputStream input, int nDigits) {
        List<BigInteger> joltages = inputAsStream(input)
                .map(Rank::new)
                .map(rank -> {
                    long largestJoltage = rank.largestJoltage(nDigits);
                    System.out.println(largestJoltage);
                    return BigInteger.valueOf(largestJoltage);
                })
                .toList();
        BigInteger res = BigInteger.ZERO;
        for (BigInteger joltage:joltages) {
            res = res.add(joltage);
        }
        return res.toString();
    }

    @Override
    public String part1(InputStream input, String... params) {
        return calculateLargestJoltage(input, 2);
    }

    @Override
    public String part2(InputStream input, String... params) {
        return calculateLargestJoltage(input, 12);
    }
}
