package com.wea.local;

import com.wea.models.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class DistanceOutsidePolygon {

    private static Double vectorLength(Coordinate vector) {
        return Math.sqrt(vector.getLatitude() * vector.getLatitude() + vector.getLongitude() * vector.getLongitude());
    }

    private static Coordinate vectorNegate(Coordinate vector) {
        return new Coordinate(vector.getLatitude() * -1, vector.getLongitude() * -1);
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
        return new Coordinate(vector.getLongitude() * -1, vector.getLatitude());
    }

    public static double distanceFromPolygon(Coordinate point, String polygonString) {
        if (polygonString == null || polygonString.isEmpty() || point == null) {
            return 0.0;
        }

        List<Coordinate> polygon = new ArrayList<>();
        List<String> splitPolygonString = List.of(polygonString.split(" "));

        for (String coordinatePair : splitPolygonString) {
            List<String> coordinateString = List.of(coordinatePair.split(","));
            polygon.add(new Coordinate(Double.parseDouble(coordinateString.get(0)),
                    Double.parseDouble(coordinateString.get(1))));
        }

        double shortestDist = Double.MAX_VALUE;
        Coordinate closestPointOnPoly = polygon.get(0);

        for (int i = 1; i < polygon.size(); i++) {
            int prev = i - 1;
            Coordinate p1 = polygon.get(i);
            Coordinate p2 = polygon.get(prev);
            Coordinate line = vectorSubtract(p2, p1);
            
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

        return getDistanceInMiles(point, closestPointOnPoly);

    }

    private static double getDistanceInMiles(Coordinate location, Coordinate closestPoint) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(closestPoint.getLatitude() - location.getLatitude());  // deg2rad below
        double dLon = Math.toRadians(closestPoint.getLongitude() - location.getLongitude());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(location.getLatitude())) * Math.cos(Math.toRadians(closestPoint.getLatitude())) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 0.621;
    }
}
