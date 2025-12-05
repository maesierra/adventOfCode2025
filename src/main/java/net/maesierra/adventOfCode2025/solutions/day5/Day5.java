package net.maesierra.adventOfCode2025.solutions.day5;

import net.maesierra.adventOfCode2025.Runner;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsTextBlocks;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;
import static net.maesierra.adventOfCode2025.utils.Logger.info;

public class Day5 implements Runner.Solution {
    record Range(BigInteger min, BigInteger max) implements Comparable<Range>{
        public Range(List<BigInteger> values) {
            this(values.get(0).min(values.get(1)), values.get(1).max(values.get(0)));
        }
        public boolean contains(BigInteger value) {
            return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        }

        public boolean overlaps(Range other) {
            return contains(other.min()) || contains(other.max()) || other.contains(this.min()) || other.contains(this.max());
        }

        @Override
        public int compareTo(Range o) {
            return min.compareTo(o.min());
        }

        Optional<Range> combine(Range other) {
            if (!overlaps(other)) {
                return Optional.empty();
            } else {
                return Optional.of(new Range(this.min.min(other.min), this.max.max(other.max)));
            }
        }
        public BigInteger size() {
            return this.max().subtract(this.min()).add(BigInteger.ONE);
        }
    }

    private static List<Range> createRanges(Stream<String> block) {
        List<Range> ranges = new ArrayList<>(block
                .map(str -> new Range(Stream.of(str.split("-")).map(BigInteger::new).sorted().toList()))
                .sorted()
                .toList());

        int i = 0;
        while (i < ranges.size() - 1) {
            Range r1 = ranges.get(i);
            Range r2 = ranges.get(i + 1);
            Optional<Range> split = r1.combine(r2);
            if (split.isPresent()) {
                ranges.remove(i);
                ranges.remove(i);
                ranges.add(i, split.get());
            } else {
                i++;
            }

        }
        return ranges;
    }

    @Override
    public String part1(InputStream input, String... params) {

        var blocks = inputAsTextBlocks(input);
        List<Range> ranges = createRanges(blocks[0]);
        AtomicInteger counter = new AtomicInteger(0);
        blocks[1].map(BigInteger::new).forEach(ingredient -> {
            info("Checking ingredient %s", ingredient);
            for (Range range:ranges) {
                if (range.min().compareTo(ingredient) > 0) {
                    return;
                }
                if (range.contains(ingredient)){
                    counter.incrementAndGet();
                    return;
                }
            }
        });
        return Integer.toString(counter.get());
    }

    @Override
    public String part2(InputStream input, String... params) {
        var blocks = inputAsTextBlocks(input);
        List<Range> ranges = createRanges(blocks[0]);
        return ranges.stream()
                .reduce(BigInteger.ZERO, (acc, r) -> acc.add(r.size()), BigInteger::add)
                .toString();
    }

}
