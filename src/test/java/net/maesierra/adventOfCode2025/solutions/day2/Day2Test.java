package net.maesierra.adventOfCode2025.solutions.day2;

import net.maesierra.adventOfCode2025.solutions.day2.Day2.Range;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part1;
import static net.maesierra.adventOfCode2025.testUtils.TestHelper.part2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class Day2Test {

    @Test
    void testInvalidSimpleMode() {
        assertThat(new Range(11, 22).getInvalid(false), is(List.of(11L, 22L)));
        assertThat(new Range(95, 115).getInvalid(false), is(List.of(99L)));
        assertThat(new Range(998, 1012).getInvalid(false), is (List.of(1010L)));
        assertThat(new Range(1188511880, 1188511890).getInvalid(false), is (List.of(1188511885L)));
        assertThat(new Range(222220, 222224).getInvalid(false), is (List.of(222222L)));
        assertThat(new Range(1698522, 1698528).getInvalid(false), is (List.of()));
        assertThat(new Range(446443, 446449).getInvalid(false), is (List.of(446446L)));
        assertThat(new Range(38593856, 38593862).getInvalid(false), is (List.of(38593859L)));
    }

    @Test
    void testInvalidComplexMode() {
        assertThat(new Range(95,115).getInvalid(true), is(List.of(99L, 111L)));
        assertThat(new Range(11,22).getInvalid(true), is(List.of(11L, 22L)));
        assertThat(new Range(998,1012).getInvalid(true), is(List.of(999L, 1010L)));
        assertThat(new Range(1188511880,1188511890).getInvalid(true), is(List.of(1188511885L)));
        assertThat(new Range(222220,222224).getInvalid(true), is(List.of(222222L)));
        assertThat(new Range(1698522,1698528).getInvalid(true), is(List.of()));
        assertThat(new Range(446443,446449).getInvalid(true), is(List.of(446446L)));
        assertThat(new Range(38593856,38593862).getInvalid(true), is(List.of(38593859L)));
        assertThat(new Range(565653,565659).getInvalid(true), is(List.of(565656L)));
        assertThat(new Range(824824821,824824827).getInvalid(true), is(List.of(824824824L)));
        assertThat(new Range(2121212118,2121212124).getInvalid(true), is(List.of(2121212121L)));

    }

    @Test
    void  testPart1() {
        String expected = "1227775554";
        assertThat(part1(new Day2(), "input_2"), equalTo(expected));
    }

    @Test
    void  testPart2() {
        String expected = "4174379265";
        assertThat(part2(new Day2(), "input_2"), equalTo(expected));
    }
}