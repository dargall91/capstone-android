package com.wea.local;

import com.wea.models.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class DistanceOutsidePolygon {

    private static Double vectorLength(Coordinate vector) {
        return Math.sqrt(vector.getLatitude() * vector.getLatitude() + vector.getLongitude() * vector.getLongitude());
    }

    private static Coordinate vectorNegate(Coordinate vector) {
        return new Coordinate(-vector.getLatitude(), -vector.getLongitude());
    }

    private static Coordinate vectorAdd(Coordinate vector1, Coordinate vector2) {
        return new Coordinate(vector1.getLatitude() + vector2.getLatitude(),
                vector1.getLongitude() + vector2.getLongitude());
    }

    private static Coordinate vectorSubtract(Coordinate vector1, Coordinate vector2) {
        return new Coordinate(vector1.getLatitude() - vector2.getLatitude(),
                vector1.getLongitude() - vector2.getLongitude());
    }

    private static Coordinate vectorScale(Coordinate vector, Double factor) {
        return new Coordinate(vector.getLatitude() * factor, vector.getLongitude() * factor);
    }

    private static Coordinate vectorNormalization(Coordinate vector) {
        return new Coordinate(-vector.getLongitude(), vector.getLatitude());
    }

    public static double distanceFromPolygon(Coordinate point, String polygonString) {
        List<Coordinate> polygon = new ArrayList<>();
        List<String> splitPolygonString = List.of(polygonString.split(" "));

        for (String coordinatePair : splitPolygonString) {
            List<String> coordinateString = List.of(coordinatePair.split(","));
            polygon.add(new Coordinate(Double.parseDouble(coordinateString.get(0)),
                    Double.parseDouble(coordinateString.get(1))));
        }

        double shortestDist = Double.MAX_VALUE;
        Coordinate closestPointOnPoly = polygon.get(0);

        for (int i = 0; i < polygon.size(); i++) {
            int prev = (i == 0 ? polygon.size() : i) - 1;
            Coordinate p1 = polygon.get(i);
            Coordinate p2 = polygon.get(prev);
            Coordinate line = vectorSubtract(p2, p1);

            if (vectorLength(line) == 0) {
                return 0.0;
            }

            Coordinate norm = vectorNormalization(line);
            double x1 = point.getLatitude();
            double x2 = norm.getLatitude();
            double x3 = p1.getLatitude();
            double x4 = line.getLatitude();
            double y1 = point.getLongitude();
            double y2 = norm.getLongitude();
            double y3 = p1.getLongitude();
            double y4 = line.getLongitude();
            double j = (x3 - x1 - (x2 * y3) / y2 + (x2 * y1) / y2) / ((x2 * y4) / y2 - x4);

            Coordinate currentPointToPoly;
            double currentDistanceToPoly;
            if (j < 0 || j > 1) {
                Coordinate a = vectorSubtract(point, p1);
                double aLen = vectorLength(a);
                Coordinate b = vectorSubtract(point, p2);
                double bLen = vectorLength(b);
                if (aLen < bLen) {
                    currentPointToPoly = vectorNegate(a);
                    currentDistanceToPoly = aLen;
                } else {
                    currentPointToPoly = vectorNegate(b);
                    currentDistanceToPoly = bLen;
                }
            } else {
                double scale = (y3 + j * y4 - y1) / y2;

                currentPointToPoly = vectorScale(norm, scale);
                currentDistanceToPoly = vectorLength(currentPointToPoly);
            }

            if (currentDistanceToPoly < shortestDist) {
                closestPointOnPoly = vectorAdd(point, currentPointToPoly);
                shortestDist = currentDistanceToPoly;
            }
        }

        return shortestDist;
    }
}
