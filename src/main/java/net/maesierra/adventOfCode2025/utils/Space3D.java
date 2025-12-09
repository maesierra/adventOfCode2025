package net.maesierra.adventOfCode2025.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

public class Space3D {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    public record Coordinate3D(BigDecimal x, BigDecimal y, BigDecimal z) implements Comparable<Coordinate3D> {


        @Override
        public int compareTo(Coordinate3D o) {
            return comparing(Coordinate3D::x)
                    .thenComparing(Coordinate3D::y)
                    .thenComparing(Coordinate3D::z)
                    .compare(this, o);
        }

        public BigDecimal euclideanDistance(Coordinate3D other) {
            //sqrt((this.x - other.x)^2 + (this.y - other.y)^2 + (this.z - other.z)^2)
            return this.x.subtract(other.x).pow(2)
                    .add(this.y.subtract(other.y).pow(2))
                    .add(this.z.subtract(other.z).pow(2))
                    .sqrt(MATH_CONTEXT);
        }

        public static Comparator<Coordinate3D> comparingEuclideanDistanceTo(Coordinate3D coord) {
            return comparing(coord::euclideanDistance);
        }
    }
}
