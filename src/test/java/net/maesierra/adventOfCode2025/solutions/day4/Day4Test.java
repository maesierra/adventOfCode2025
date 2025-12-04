package net.maesierra.adventOfCode2025.solutions.day4;

import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day4Test {

    @Test
    void  testPart1() {
        String expected = "13";
        assertThat(part1(new Day4(), "input_4"), equalTo(expected));
    }

    @Test
    void  testPart2() {
        String expected = "43";
        assertThat(part2(new Day4(), "input_4"), equalTo(expected));
    }
}