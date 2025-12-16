package net.maesierra.adventOfCode2025.solutions.day11;

import org.junit.jupiter.api.Test;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class Day11Test {


    @Test
    void  testPart1() {
        String expected = "5";
        assertThat(part1(new Day11(), "input_11"), equalTo(expected));
    }


    /**
     *           svr
     *           / \
     *         aaa bbb
     *          |   |
     *         fft tty
     *          |   |
     *         ccc /
     *         / \
     *       ddd eee
     *        |   |
     *       hub  dac
     *         \  |
     *           fff
     *           / \
     *         ggg hhh
     *          |   |
     *         out out
     *
     *
     */
    @Test
    void  testPart2() {
        String expected = "2";
        //1906355968 too low
        assertThat(part2(new Day11(), "input_11_2"), equalTo(expected));
    }
}