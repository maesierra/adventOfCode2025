package net.maesierra.adventOfCode2025.solutions.day4;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.Runner.VisualiseProperties;
import net.maesierra.adventOfCode2025.utils.Matrix;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.maesierra.adventOfCode2025.solutions.day4.Day4.State.*;
import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsCharMatrix;
import static net.maesierra.adventOfCode2025.utils.Logger.debug;

public class Day4 implements Runner.Solution {
    enum State {
        OCCUPIED,
        REMOVED,
        EMPTY
    }

    static class GridPosition {
        private State state;
        GridPosition(Character chr) {
            this.state = switch (chr) {
                case '@' -> OCCUPIED;
                case 'x' -> REMOVED;
                default -> EMPTY;
            };
        }

        @Override
        public String toString() {
            return switch (state) {
                case OCCUPIED -> "@";
                case REMOVED -> "x";
                case EMPTY -> ".";
            };
        }
        boolean isOccupied() {
            return state == OCCUPIED;
        }

        void remove() {
            this.state = REMOVED;
        }

    }

    @Override
    public String part1(InputStream input, String... params) {
        Matrix<GridPosition> grid = createGrid(input);
        var rollsToRemove = rollsToRemove(grid);
        removeRolls(rollsToRemove);
        debug(grid.toString());
        return Integer.toString(rollsToRemove.size());
    }

    @Override
    public String part2(InputStream input, String... params) {
        Matrix<GridPosition> grid = createGrid(input);
        int totalRemoved = 0;
        var rollsToRemove = rollsToRemove(grid);
        while (!rollsToRemove.isEmpty()) {
            removeRolls(rollsToRemove);
            debug(grid.toString());
            debug("-----------");
            totalRemoved += rollsToRemove.size();
            rollsToRemove = rollsToRemove(grid);
        }
        return Integer.toString(totalRemoved);
    }

    public VisualiseProperties visualiseProperties() {
        return new VisualiseProperties(50, 1200, 1200);
    }

    @Override
    public Function<Graphics2D, Boolean> visualisePart2(InputStream input, String... params) {
        enum Status {
            STARTED,
            FINISHED,
            NONE
        }
        AtomicReference<Status> status = new AtomicReference<>(Status.NONE);
        Matrix<GridPosition> grid = createGrid(input);
        int tileSize = 8;
        return graphics -> {
            switch (status.get()) {
                case NONE -> status.set(Status.STARTED);
                case STARTED -> {
                    var rollsToRemove = rollsToRemove(grid);
                    removeRolls(rollsToRemove);
                    if (rollsToRemove.isEmpty()) {
                        status.set(Status.FINISHED);
                    }
                }
            }
            boolean finished = status.get() == Status.FINISHED;
            grid.items().forEach( i -> {
                var position = i.position().multiply(tileSize);
                graphics.setColor(switch (i.value().state) {
                    case OCCUPIED -> finished ? new Color(139, 0, 0) :  Color.RED;
                    case EMPTY -> Color.DARK_GRAY;
                    case REMOVED -> Color.GRAY;
                });
                graphics.fillRect(position.col(), position.row(), tileSize, tileSize);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(position.col(), position.row(), tileSize, tileSize);
            });
            return finished;
        };
    }

    private Matrix<GridPosition> createGrid(InputStream input) {
        return inputAsCharMatrix(input).map(i -> new GridPosition(i.value()));
    }

    private static List<GridPosition> rollsToRemove(Matrix<GridPosition> grid) {
        List<GridPosition> toRemove = new ArrayList<>();
        grid.items().forEach(i -> {
            if (!i.value().isOccupied()) {
                return;
            }
            long nOccupied = i.directNeighbours().stream()
                    .filter(Objects::nonNull)
                    .filter(neighbour -> neighbour.value().isOccupied())
                    .count();

            if (nOccupied < 4) {
                toRemove.add(i.value());
            }
        });
        return toRemove;
    }


    private static void removeRolls(List<GridPosition> positions) {
        positions.forEach(GridPosition::remove);
    }


}
