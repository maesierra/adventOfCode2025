package net.maesierra.adventOfCode2025.solutions.day8;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Space3D.Coordinate3D;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;
import static net.maesierra.adventOfCode2025.utils.Logger.info;

public class Day8 implements Runner.Solution {

    record EuclideanDistance(BigDecimal value, Coordinate3D c1, Coordinate3D c2) implements Comparable<EuclideanDistance> {
        EuclideanDistance(Coordinate3D c1, Coordinate3D c2) {
            this(c1.euclideanDistance(c2), c1, c2);
        }

        @Override
        public int compareTo(EuclideanDistance o) {
            return this.value().compareTo(o.value());
        }
    }

    private static final AtomicInteger idGenerator = new AtomicInteger(2);

    record Circuit(int id, Set<Coordinate3D> junctionBoxes) implements Comparable<Circuit>{
        Circuit(Coordinate3D box) {
            this(idGenerator.incrementAndGet(), new HashSet<>(List.of(box)));
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Circuit circuit = (Circuit) o;
            return id() == circuit.id();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id());
        }

        public void add(Coordinate3D box) {
            this.junctionBoxes.add(box);
        }

        public void join(Circuit other) {
            this.junctionBoxes.addAll(other.junctionBoxes());
        }

        @Override
        public int compareTo(Circuit o) {
            return Integer.compare(this.junctionBoxes.size(), o.junctionBoxes.size());
        }

        public long size() {
            return this.junctionBoxes().size();
        }

        @Override
        public String toString() {
            return "id: %d [%d]".formatted(id, size());
        }
    }



    @Override
    public String part1(InputStream input, String... params) {
        int size = params.length == 1 ? Integer.parseInt(params[0]) : 1000;

        List<Coordinate3D> junctionBoxes = junctionBoxesFromInput(input);
        List<EuclideanDistance> distances = calculateDistances(junctionBoxes);
        Map<Coordinate3D, Circuit> circuitMap = createCircuits(junctionBoxes);

        for (int i = 0; i < size ; i++) {
            EuclideanDistance closest = distances.get(i);
            debug("Closest distance %s", closest.value.toString());
            join(closest.c1(), closest.c2(), circuitMap);
        }
        List<Circuit> circuits = circuitMap.values().stream().distinct().sorted(Collections.reverseOrder()).toList();
        long res = circuits.get(0).size() * circuits.get(1).size() * circuits.get(2).size();
        info(new HashSet<>(circuitMap.values()).toString());
        info("total %d", new HashSet<>(circuitMap.values()).stream().mapToLong(Circuit::size).sum());
        return Long.toString(res);
    }

    @Override
    public String part2(InputStream input, String... params) {
        List<Coordinate3D> junctionBoxes = junctionBoxesFromInput(input);
        List<EuclideanDistance> distances = calculateDistances(junctionBoxes);
        Map<Coordinate3D, Circuit> circuitMap = createCircuits(junctionBoxes);
        int i = 0;
        while (i < distances.size()) {
            EuclideanDistance closest = distances.get(i);
            debug("Closest distance %s (%d)", () -> new Object[] {closest.value().toString(), circuitMap.values().stream().distinct().count()});
            Circuit joined = join(closest.c1(), closest.c2(), circuitMap);
            if (joined.size() == junctionBoxes.size()) {
                break;
            }
            i++;
        }
        return distances.get(i).c1.x().multiply(distances.get(i).c2.x()).toString();
    }

    private static Map<Coordinate3D, Circuit> createCircuits(List<Coordinate3D> junctionBoxes) {
        return junctionBoxes.stream().collect(Collectors.toMap(
                b -> b,
                Circuit::new
        ));
    }

    private static List<EuclideanDistance> calculateDistances(List<Coordinate3D> junctionBoxes) {
        List<EuclideanDistance> distances = new ArrayList<>();
        for (int i = 0; i < junctionBoxes.size(); i++) {
            for (int j = i + 1; j < junctionBoxes.size(); j++) {
                distances.add(new EuclideanDistance(junctionBoxes.get(i), junctionBoxes.get(j)));
            }
        }
        distances.sort(naturalOrder());
        return distances;
    }

    private static List<Coordinate3D> junctionBoxesFromInput(InputStream input) {
        return inputAsStream(input).map(s -> {
            String[] parts = s.split(",");
            return new Coordinate3D(
                    new BigDecimal(parts[0]),
                    new BigDecimal(parts[1]),
                    new BigDecimal(parts[2])
            );
        }).toList();
    }

    private static Circuit join(Coordinate3D box1, Coordinate3D box2, Map<Coordinate3D, Circuit> circuitMap) {
        Circuit circuit1 = circuitMap.get(box1);
        Circuit circuit2 = circuitMap.get(box2);
        circuit1.join(circuit2);
        circuit2.junctionBoxes().forEach(c -> circuitMap.put(c, circuit1));
        debug(new HashSet<>(circuitMap.values()).toString());
        debug("total %d", new HashSet<>(circuitMap.values()).stream().mapToLong(Circuit::size).sum());
        return circuit1;
    }

}
