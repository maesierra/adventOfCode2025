package net.maesierra.adventOfCode2025.solutions.day9;

import net.maesierra.adventOfCode2025.Runner;
import net.maesierra.adventOfCode2025.utils.Position;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import static net.maesierra.adventOfCode2025.utils.IOHelper.inputAsStream;
import static net.maesierra.adventOfCode2025.utils.Logger.info;

public class Day9 implements Runner.Solution {

    @Override
    public String part1(InputStream input, String... params) {
        List<Position> redTiles = readRedTiles(input);
        long max = Long.MIN_VALUE;
        for (int i = 0; i < redTiles.size(); i++) {
            info("Calculating distance %d of %d", i, redTiles.size());
            for (int j = i + 1; j < redTiles.size(); j++) {
                Rectangle rectangle = new Rectangle(redTiles.get(i), redTiles.get(j));
                max = Math.max(max, rectangle.area());
            }
        }
        return Long.toString(max);
    }

    @Override
    public String part2(InputStream input, String... params) {
        List<Position> positions = readRedTiles(input);
        Rectangle biggest = biggestRectangleInShape(positions);
        return Long.toString(biggest.area());
    }

    private static List<Position> readRedTiles(InputStream input) {
        return inputAsStream(input).map(s -> s.split(",")).map(parts -> new Position(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]))).toList();
    }

    record Rectangle(Position p1, Position p2) {

        long area() {
            return (Math.abs((long) p1.row() - p2.row()) + 1) * (Math.abs((long) p1.col() - p2.col()) + 1);
        }

        boolean inShape(Polygon shape) {
            if (!shape.contains(p1.col(), p2.row())) {
                return false;
            }
            int maxRow = Math.max(p1.row(), p2.row());
            int minRow = Math.min(p1.row(), p2.row());
            for (int i = minRow; i < maxRow ; i++) {
                if (!shape.contains(p2.col(), i)) {
                    return false;
                }
            }
            return true;
        }


    }
    private static Pair<Position, Position> calculateCentralPoints(List<Position> positions) {
        int nTiles = positions.size();
        int maxCol = positions.stream().mapToInt(Position::col).max().orElseThrow();
        Position point1 = null;
        for (int i = 0; i < nTiles; i++) {
            Position p1 = positions.get(i);
            Position p2 = positions.get((i + 1) % nTiles);
            if (p1.row() == p2.row() && (Math.abs(p2.col() - p1.col())) > (maxCol / 2)) {
                Position point = (p1.col() > p2.col()) ? p1 : p2;
                if (point1 == null) {
                    point1 = point;
                } else {
                    if (point1.row() > point.row()) {
                        return Pair.of(point, point1);
                    } else {
                        return Pair.of(point1, point);
                    }
                }
            }
        }
        throw new IllegalStateException("No central points found");
    }
    private static Rectangle biggestRectangleInShape(List<Position> positions) {
        Polygon shape = new Polygon();
        for (var pos: positions) {
            shape.addPoint(pos.col(), pos.row());
        }
        //Locate the 2 points that are the end of the big horizontal lines at the middle of the circle
        Pair<Position, Position> centralPoints = calculateCentralPoints(positions);
        long max = Long.MIN_VALUE;
        Rectangle points = null;
        //For p1 we want all the points higher and to the left
        for (var pos: positions.stream().filter(p -> p.row() < centralPoints.getLeft().row() && p.col() < centralPoints.getLeft().col()).toList()) {
            Rectangle rectangle = new Rectangle(centralPoints.getLeft(), pos);
            if (rectangle.inShape(shape)) {
                long area = rectangle.area();
                if (area > max) {
                    max = area;
                    points = rectangle;
                }
            }
        }
        //For p2 we want all the points lower and to the left
        for (var pos: positions.stream().filter(p -> p.row() > centralPoints.getRight().row() && p.col() < centralPoints.getRight().col()).toList()) {
            Rectangle rectangle = new Rectangle(centralPoints.getRight(), pos);
            if (rectangle.inShape(shape)) {
                long area = rectangle.area();
                if (area > max) {
                    max = area;
                    points = rectangle;
                }
            }
        }
        return points;
    }

    public Runner.VisualiseProperties visualiseProperties() {
        return new Runner.VisualiseProperties(3000, 1300, 1300);
    }

    @Override
    public Function<Graphics2D, Boolean> visualisePart2(InputStream input, String... params) {
        List<Position> positions = readRedTiles(input);
        Rectangle biggest = biggestRectangleInShape(positions);

        BigInteger factor = BigInteger.valueOf(83);

        return graphics -> {
            Polygon polygon = new Polygon();
            for (var pos:positions) {
                Point point = positionToPoint(pos, factor);
                polygon.addPoint(point.x, point.y);
            }
            graphics.setBackground(Color.BLACK);
            graphics.setColor(Color.GREEN);
            graphics.fillPolygon(polygon);
            graphics.setColor(Color.RED);
            graphics.drawPolygon(polygon);
            graphics.setColor(Color.BLUE);

            Point point1 = positionToPoint(biggest.p1, factor);
            Point point2 = positionToPoint(biggest.p2, factor);
            Polygon rectangle = new Polygon();
            rectangle.addPoint(point1.x, point1.y);
            rectangle.addPoint(point2.x, point1.y);
            rectangle.addPoint(point2.x, point2.y);
            rectangle.addPoint(point1.x, point2.y);
            graphics.draw(rectangle.getBounds());
            return false;
        };
    }


    private static Point positionToPoint(Position pos, BigInteger factor) {
        BigInteger x = BigInteger.valueOf(pos.col()).divide(factor);
        BigInteger y = BigInteger.valueOf(pos.row()).divide(factor);
        return new Point(x.intValue(), y.intValue());
    }

}
