package net.maesierra.adventOfCode2025.solutions.day12;

import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day12Test {

    @Test
    void  testPart1() {
        assertThat(part1(new Day12(), "input_12_1"), equalTo("140"));
        assertThat(part1(new Day12(), "input_12_2"), equalTo("772"));
        assertThat(part1(new Day12(), "input_12_3"), equalTo("1930"));
    }

    @Test
    void  testPart2() {
        assertThat(part2(new Day12(), "input_12_1"), equalTo("80"));
    }
}