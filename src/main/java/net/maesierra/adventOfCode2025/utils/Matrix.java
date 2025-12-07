package net.maesierra.adventOfCode2025.utils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Matrix<T> {

    public static class ItemList<T> {
        private final int n;
        private final List<T> items;
        private final Matrix<T> matrix;
        private final List<Item<T>> itemList;

        public ItemList(int n, List<T> itemList, BiFunction<Integer, Integer, Position> transpose, Matrix<T> matrix) {
            this.n = n;
            this.items = itemList;
            this.matrix = matrix;
            this.itemList = itemList.stream()
                    .reduce(
                            new ArrayList<>(),
                            (list, i) -> {
                                Position pos = transpose.apply(this.n, list.size());
                                list.add(new Item<>(pos.row(), pos.col(), i, this.matrix));
                                return list;
                            },
                            (a, b) -> a);

        }

        public List<Item<T>> itemList() {
            return itemList;
        }

        public int n() {
            return n;
        }

        public List<T> items() {
            return items;
        }

        public Matrix<T> matrix() {
            return matrix;
        }
        public Item<T> at(int col) {
            return itemList.get(col);
        }
    }

    public static class Row<T> extends ItemList<T>{

        public Row(int n, List<T> row, Matrix<T> matrix) {
            super(n, row, Position::new, matrix);
        }
        public List<T> row() {
            return items();
        }

    }

    public static class Column<T> extends ItemList<T>{

        public Column(int n, List<T> column, Matrix<T> matrix) {
            super(n, column, (r, c) -> new Position(c, r), matrix);
        }
        public List<T> column() {
            return items();
        }

    }

    public record Item<T>(int row, int column, T value, Matrix<T> matrix)  {
        public Position position() {
            return new Position(row, column);
        }

        public boolean isEdge() {
            return row    == 0 ||    row == matrix.nRows() - 1 ||
                   column == 0 || column == matrix.nCols() - 1;
        }

        public Directions<Item<T>> orthogonalNeighbours() {
            Item<T> north = null;
            Item<T> east = null;
            Item<T> south = null;
            Item<T> west = null;
            int i = 1;
            int row = this.row();
            int column = this.column();
            int rowTop = row - i;
            int rowBottom = row + i;
            int columnLeft = column - i;
            int columnRight = column + i;
            if (this.matrix.isIn(rowTop, column)) {
                north = matrix.at(rowTop, column);
            }
            if (this.matrix.isIn(row, columnRight)) {
                east = matrix.at(row, columnRight);
            }
            if (this.matrix.isIn(rowBottom, column)) {
                south = matrix.at(rowBottom, column);
            }
            if (this.matrix.isIn(row, columnLeft)) {
                west = matrix.at(row, columnLeft);
            }
            return new Directions<>(
                    null,
                    north,
                    null,
                    east,
                    null,
                    south,
                    null,
                    west
            );
        }
        public Directions<List<Item<T>>> neighbours(int radius) {
            List<Item<T>> northWest = new ArrayList<>(radius);
            List<Item<T>> north = new ArrayList<>(radius);
            List<Item<T>> northEast = new ArrayList<>(radius);
            List<Item<T>> east = new ArrayList<>(radius);
            List<Item<T>> southEast = new ArrayList<>(radius);
            List<Item<T>> south = new ArrayList<>(radius);
            List<Item<T>> southWest = new ArrayList<>(radius);
            List<Item<T>> west = new ArrayList<>(radius);
            for (int i = 0; i < radius; i++) {
                int row = this.row();
                int column = this.column();
                int rowTop = row - i;
                int rowBottom = row + i;
                int columnLeft = column - i;
                int columnRight = column + i;
                if (this.matrix.isIn(rowTop, columnLeft)) {
                    northWest.add(matrix.at(rowTop, columnLeft));
                }
                if (this.matrix.isIn(rowTop, column)) {
                    north.add(matrix.at(rowTop, column));
                }
                if (this.matrix.isIn(rowTop, columnRight)) {
                    northEast.add(matrix.at(rowTop, columnRight));
                }
                if (this.matrix.isIn(row, columnRight)) {
                    east.add(matrix.at(row, columnRight));
                }
                if (this.matrix.isIn(rowBottom, columnRight)) {
                    southEast.add(matrix.at(rowBottom, columnRight));
                }
                if (this.matrix.isIn(rowBottom, column)) {
                    south.add(matrix.at(rowBottom, column));
                }
                if (this.matrix.isIn(rowBottom, columnLeft)) {
                    southWest.add(matrix.at(rowBottom, columnLeft));
                }
                if (this.matrix.isIn(row, columnLeft)) {
                    west.add(matrix.at(row, columnLeft));
                }
            }
            return new Directions<>(
                    northWest,
                    north,
                    northEast,
                    east,
                    southEast,
                    south,
                    southWest,
                    west
            );
        }

        public Directions<Item<T>> directNeighbours() {
            Item<T> northWest = null;
            Item<T> north = null;
            Item<T> northEast = null;
            Item<T> east = null;
            Item<T> southEast = null;
            Item<T> south = null;
            Item<T> southWest = null;
            Item<T> west = null;
            int row = this.row();
            int column = this.column();
            int rowTop = row - 1;
            int rowBottom = row + 1;
            int columnLeft = column - 1;
            int columnRight = column + 1;
            if (this.matrix.isIn(rowTop, columnLeft)) {
                northWest = matrix.at(rowTop, columnLeft);
            }
            if (this.matrix.isIn(rowTop, column)) {
                north = matrix.at(rowTop, column);
            }
            if (this.matrix.isIn(rowTop, columnRight)) {
                northEast = matrix.at(rowTop, columnRight);
            }
            if (this.matrix.isIn(row, columnRight)) {
                east = matrix.at(row, columnRight);
            }
            if (this.matrix.isIn(rowBottom, columnRight)) {
                southEast = matrix.at(rowBottom, columnRight);
            }
            if (this.matrix.isIn(rowBottom, column)) {
                south = matrix.at(rowBottom, column);
            }
            if (this.matrix.isIn(rowBottom, columnLeft)) {
                southWest = matrix.at(rowBottom, columnLeft);
            }
            if (this.matrix.isIn(row, columnLeft)) {
                west = matrix.at(row, columnLeft);
            }
            return new Directions<>(
                    northWest,
                    north,
                    northEast,
                    east,
                    southEast,
                    south,
                    southWest,
                    west
            );
        }
    }

    public Item<T> at(int row, int col) {
        return this.rows.get(row).at(col);
    }
    public Item<T> at(Position pos) {
        return at(pos.row(), pos.col());
    }

    private final List<Row<T>> rows;
    private final int nRows;
    private final int nCols;


    public Matrix(Stream<List<T>> rows) {
        this.rows = rows.reduce(new ArrayList<>(), (r, s) -> {
            r.add(new Row<>(r.size(), s, this));
            return r;
        }, (a, b) -> a);
        this.nRows = this.rows.size();
        if (this.nRows > 0) {
            this.nCols = this.rows.get(0).row().size();
        } else {
            this.nCols = 0;
        }
    }
    public Matrix(Matrix<T> other) {
        this.rows = other.rows;
        this.nRows = other.nRows;
        this.nCols = other.nCols;
    }
    public Stream<Row<T>> rows() {
        return rows.stream();
    }
    public Stream<Column<T>> columns() {
        Map<Integer, List<Item<T>>> columns = new TreeMap<>();
        items().forEach(item -> {
            columns.computeIfAbsent(item.column(), k -> new ArrayList<>()).add(item);
        });
        return columns.entrySet().stream().map(entry -> new Column<>(entry.getKey(), entry.getValue().stream().map(Item::value).toList(), this));
    }



    public Row<T> row(int row) {
        return rows.get(row);
    }

    public Row<T> lastRow() {
        return rows.getLast();
    }




    public int nRows() {
        return nRows;
    }

    public int nCols() {
        return nCols;
    }

    public Stream<Item<T>> items() {
        return rows.stream().flatMap(r -> r.itemList().stream());
    }

    public boolean isIn(int row, int col) {
        return (row >= 0 && row < this.nRows) && (col >= 0 && col < this.nCols);
    }
    public boolean isIn(Position position) {
        return isIn(position.row(), position.col());
    }

    public <T2> Matrix<T2> map(Function<Item<T>, T2> mapper) {
        return new Matrix<>(this.rows()
                .map(r -> r.itemList().stream().map(mapper).toList())
        );
    }

    public String toString() {
        return toString(i -> i.value().toString());
    }
    public String toString(Function<Item<T>, String> formatter) {
        return this.rows().map(r -> {
                    return r.itemList().stream().map(formatter).collect(Collectors.joining());
                })
                .collect(Collectors.joining("\n"));
    }
    public static <T> Matrix<T> init(int nRows, int nCols, Supplier<T> initialValue) {
        List<List<T>> empty = new ArrayList<>(nRows);
        for (int i = 0; i < nRows; i++) {
            List<T> row = new ArrayList<>();
            for (int j = 0; j < nCols; j++) {
                row.add(initialValue.get());
            }
            empty.add(row);
        }
        return new Matrix<>(empty.stream());
    }

    public static <T> Matrix<T> init(int nRows, int nCols, T initialValue) {
        return init(nRows, nCols, () -> initialValue);
    }
}
