package net.maesierra.adventOfCode2025.solutions.day12;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day12Test {

    @Test
    @Disabled
    void  testPart1() {
        assertThat(part1(new Day12(), "input_12"), equalTo("2"));
    }

    @Test
    @Disabled
    void  testPart2() {
        assertThat(part2(new Day12(), "input_12_1"), equalTo("80"));
    }
}