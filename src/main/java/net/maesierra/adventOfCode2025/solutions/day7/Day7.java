package net.maesierra.adventOfCode2025.solutions.day7;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Matrix;
import net.maesierra.adventOfCode2025.utils.Position;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsCharMatrix;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;


public class Day7 implements Runner.Solution {

    enum State {
        START,
        SPLITTER,
        EMPTY;

        static State of(Character s) {
            return switch (s) {
                case 'S' -> START;
                case '^' -> SPLITTER;
                default -> EMPTY;
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case EMPTY -> ".";
                case SPLITTER -> "^";
                case START -> "S";
            };
        }
    }

    public static class Beam extends ArrayList<Position> {

        public Beam() {
            super();
        }
        public Beam(Collection<? extends Position> c) {
            super(c);
        }

        @Override
        public String toString() {
            return toString(2);
        }
        public String toString(int padSize) {
            return stream().map(p -> StringUtils.leftPad(Integer.toString(p.row()), padSize, "0") + "," + StringUtils.leftPad(Integer.toString(p.col()), padSize, "0")).collect(Collectors.joining("|"));
        }
    }

    private static class TachyonManifold {
        private final Matrix<State> grid;
        private final Position startingPosition;

        public TachyonManifold(Matrix<State> grid) {
            this.grid = grid;
            this.startingPosition = this.grid.row(0).itemList().stream().filter(i -> i.value() == State.START).findFirst().orElseThrow().position();
        }

        public Beam startBeam() {
            Beam beam = new Beam();
            beam.add(startingPosition);
            return beam;
        }

        public boolean isBeamAtEnd(Beam beam) {
            return beam.getLast().row() == grid.nRows() - 1;
        }


        public Beam moveUntilSplit(Beam beam) {
            Beam newBeam = new Beam(beam);
            boolean splitterFound = false;
            while (!splitterFound) {
                var itemBellow = grid.at(newBeam.getLast()).orthogonalNeighbours().south();
                if (itemBellow == null) {
                    return newBeam;
                }
                if (itemBellow.value() == State.EMPTY) {
                    newBeam.add(itemBellow.position());
                } else if (itemBellow.value() == State.SPLITTER) {
                    splitterFound = true;
                }
            }
            return newBeam;
        }

        public List<Beam> moveBeam(Beam beam) {
            var itemBellow = grid.at(beam.getLast()).orthogonalNeighbours().south();
            if (itemBellow == null) {
                return List.of(beam);
            }
            return switch (itemBellow.value()) {
                case EMPTY -> {
                    beam.add(itemBellow.position());
                    yield List.of(beam);
                }
                case SPLITTER -> splitBeam(beam, itemBellow);
                default -> List.of(beam);
            };
        }

        public List<Beam> splitBeam(Beam beam, Position position) {
            return splitBeam(beam, grid.at(position));
        }

        private List<Beam> splitBeam(Beam beam, Matrix.Item<State> itemBellow) {
            var neighbours = itemBellow.orthogonalNeighbours();
            var left = neighbours.west();
            List<Beam> split = new ArrayList<>();
            if (left != null) {
                Beam newBeam = new Beam(beam);
                newBeam.add(left.position());
                split.add(newBeam);
            }
            var right = neighbours.east();
            if (right != null) {
                Beam newBeam = new Beam(beam);
                newBeam.add(right.position());
                split.add(newBeam);
            }
            return split;
        }

        @Override
        public String toString() {
            return grid.toString();
        }

        public String print(Collection<Beam> beams) {
            Set<Position> withBeams = beams.stream().flatMap(Collection::stream).collect(Collectors.toSet());
            return this.grid.toString(i -> {
                if (withBeams.contains(i.position())) {
                    return "|";
                }
                return i.value().toString();
            });
        }
    }



    private TachyonManifold createManifold(InputStream input) {
        return new TachyonManifold(inputAsCharMatrix(input).map(i -> State.of(i.value())));
    }

    @Override
    public String part1(InputStream input, String... params) {
        TachyonManifold manifold = createManifold(input);
        debug(manifold.toString());
        Queue<Beam> beams = new LinkedBlockingQueue<>();
        beams.add(manifold.startBeam());
        long splitBeams = 0;
        while (beams.stream().anyMatch(b -> !manifold.isBeamAtEnd(b))) {
            Beam beam = beams.poll();
            Set<Position> withBeams = beams.stream().flatMap(Collection::stream).collect(Collectors.toSet());
            List<Beam> after = manifold.moveBeam(beam);
            if (after.size() == 2) {
                splitBeams ++;
            }
            //Only add the new beams
            after.stream()
                    .filter(b -> !withBeams.contains(b.getLast()))
                    .forEach(beams::add);
            debug(StringUtils.repeat('-', manifold.grid.nCols()));
            debug(manifold.print(beams));
        }
        return Long.toString(splitBeams);
    }


    @Override
    public String part2(InputStream input, String... params) {
        TachyonManifold manifold = createManifold(input);
        return Long.toString(calculateTimelines(manifold, manifold.startBeam(), new HashMap<>()));
    }

    private static long calculateTimelines(TachyonManifold manifold, Beam beam, Map<Position, Long> cache) {
        beam = manifold.moveUntilSplit(beam);
        Position beamLast = beam.getLast();
        if (cache.containsKey(beamLast)) {
            return cache.get(beamLast);
        }
        if (manifold.isBeamAtEnd(beam)) {
            return 1;
        }
        List<Beam> split = manifold.splitBeam(beam, beamLast);
        long res = calculateTimelines(manifold, split.get(0), cache) + calculateTimelines(manifold, split.get(1), cache);
        cache.put(beamLast, res);
        return res;
    }

}
