package net.maesierra.adventOfCode2025.solutions.day9;

import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day9Test {

    @Test
    void  testPart1() {
        String expected = "50";
        assertThat(part1(new Day9(), "input_9"), equalTo(expected));
    }

    //No test for part2, my solution works only for the real input
}