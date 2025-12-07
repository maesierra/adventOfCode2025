package net.maesierra.adventOfCode2025.solutions.day6;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;
import static net.maesierra.adventOfCode2025.utils.Logger.info;

public class Day6 implements Runner.Solution {

    sealed interface MathElement permits Number, Add, Multiply {
    }

    sealed interface Operation permits Add, Multiply {
        BigInteger apply(BigInteger op1, BigInteger op2);
        BigInteger startValue();
        default BigInteger apply(Stream<BigInteger> list) {
            return list.reduce(startValue(), this::apply, (acc1, acc2) -> apply(acc1, acc1));
        }
    }
    record Number(BigInteger value) implements MathElement {
        @Override
        public String toString() {
            return value.toString();
        }
    }

    record Add() implements MathElement,Operation {
        @Override
        public String toString() {
            return "+";
        }

        @Override
        public BigInteger apply(BigInteger op1, BigInteger op2) {
            return op1.add(op2);
        }

        @Override
        public BigInteger startValue() {
            return BigInteger.ZERO;
        }
    };
    record Multiply() implements MathElement,Operation {
        @Override
        public String toString() {
            return "*";
        }

        @Override
        public BigInteger apply(BigInteger op1, BigInteger op2) {
            return op1.multiply(op2);
        }

        @Override
        public BigInteger startValue() {
            return BigInteger.ONE;
        }
    };

    @Override
    public String part1(InputStream input, String... params) {
        Stream<List<MathElement>> stream = inputAsStream(input).map(s -> Stream.of(s.split(" "))
                .filter(StringUtils::isNotEmpty)
                .map(token -> (MathElement) switch (token) {
                    case "*" -> new Multiply();
                    case "+" -> new Add();
                    default -> new Number(new BigInteger(token));
                }).toList());
        Matrix<MathElement> mathSpreadsheet = new Matrix<>(stream);
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);
        mathSpreadsheet.columns().forEach(column -> {
            Operation operation = (Operation) column.column().getLast();
            Stream<BigInteger> numbers = column.column().subList(0, column.column().size() - 1).stream().map(e -> ((Number) e).value());
            BigInteger value = operation.apply(numbers);
            res.set(res.get().add(value));
        });
        return res.toString();
    }

    @Override
    public String part2(InputStream input, String... params) {
        List<String> lines = inputAsStream(input).toList();
        //The last line defines the columns
        String lastLine = lines.getLast();
        record ColumnDef(int size, Operation operation) {

        }
        List<ColumnDef> operations = new ArrayList<>();
        int columnSize = 0;
        Operation operation = null;
        for (int i = 0; i < lastLine.length(); i++) {
            if (lastLine.charAt(i) == ' ') {
                columnSize++;
            } else {
                Operation op = lastLine.charAt(i) == '+' ? new Add() : new Multiply();
                if (operation == null) {
                    operation = op;
                } else {
                    operations.add(new ColumnDef(columnSize, operation));
                    operation = op;
                    columnSize = 0;
                }
            }
        }
        operations.add(new ColumnDef(columnSize + 1, operation));
        Matrix<String> matrix = new Matrix<>(lines.subList(0, lines.size() - 1).stream()
                .map(s -> {
                    List<String> elements = new ArrayList<>();
                    int pos = 0;
                    for (var column:operations) {
                        elements.add(s.substring(pos, pos + column.size));
                        pos += column.size + 1;
                    }
                    return elements;
                }));
        info(matrix.toString(i -> i.value() + "|"));
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);
        matrix.columns().forEach(column -> {
            ColumnDef definition = operations.get(column.n());
            Matrix<Character> problemMatrix = new Matrix<>(column.column().stream().map(s -> s.chars().mapToObj(i -> (char) i).toList()));
            Stream<BigInteger> numbers = problemMatrix.columns().map(problemColumn -> new BigInteger(problemColumn.column().stream().filter(i -> i != ' ').map(Object::toString).collect(Collectors.joining())));
            res.set(res.get().add(definition.operation.apply(numbers)));
        });
        return res.toString();
    }

}
