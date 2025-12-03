package net.maesierra.adventOfCode2025.solutions.day3;

import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day3Test {

    @Test
    void  testPart1() {
        String expected = "357";
        assertThat(part1(new Day3(), "input_3"), equalTo(expected));
    }

    @Test
    void  testPart2() {
        String expected = "3121910778619";
        assertThat(part2(new Day3(), "input_3"), equalTo(expected));
    }
}