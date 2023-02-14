package com.wea.local;

import java.util.ArrayList;

public class DistanceOutsidePolygon {

    private static Double vectorLength(Double[] vector) {
        return Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
    }

    private static Double[] vectorNegate(Double[] vector) {
        Double[] negative = {-vector[0], -vector[1]};
        return negative;
    }

    private static Double[] vectorAdd(Double[] vector1, Double[] vector2) {
        Double[] addition = {vector1[0] + vector2[0], vector1[1] + vector2[1]};
        return addition;
    }

    private static Double[] vectorSubtract(Double[] vector1, Double[] vector2) {
        Double[] subtraction = {vector1[0] - vector2[0], vector1[1] - vector2[1]};
        return subtraction;
    }

    private static Double[] vectorScale(Double[] vector, Double factor) {
        Double[] scaled = {vector[0] * factor, vector[1] * factor};
        return scaled;
    }

    private static Double[] vectorNormalization(Double[] vector) {
        Double[] normalized = {-vector[1], vector[0]};
        return normalized;
    }

    public static Double[] closestPointOnPolygon(Double[] point, String coords) {

        String[] coordsSplit = coords.split(" ");

        ArrayList<Double[]> poly = new ArrayList<>();

        for(int i = 0; i < coordsSplit.length; i++){
            Double[] singlePoint = {Double.parseDouble(coordsSplit[0]), Double.parseDouble(coordsSplit[1])};
            poly.add(singlePoint);
        }

        Double shortestDist = Double.MAX_VALUE;
        Double[] closestPointOnPoly = poly.get(0);

        for (int i = 0; i < poly.size(); i++) {
            int prev = (i == 0 ? poly.size() : i) - 1;
            Double[] p1 = poly.get(i);
            Double[] p2 = poly.get(prev);
            Double[] line = vectorSubtract(p2, p1);

            if (vectorLength(line) == 0) return new Double[]{vectorLength(vectorSubtract(point, p1)), 0.0};

            Double[] norm = vectorNormalization(line);
            double x1 = point[0];
            double x2 = norm[0];
            double x3 = p1[0];
            double x4 = line[0];
            double y1 = point[1];
            double y2 = norm[1];
            double y3 = p1[1];
            double y4 = line[1];
            double j = (x3 - x1 - (x2 * y3) / y2 + (x2 * y1) / y2) / ((x2 * y4) / y2 - x4);

            Double[] currentPointToPoly;
            double currentDistanceToPoly;
            if (j < 0 || j > 1) {
                Double[] a = vectorSubtract(point, p1);
                double aLen = vectorLength(a);
                Double[] b = vectorSubtract(point, p2);
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

        return new Double[] {closestPointOnPoly[0], closestPointOnPoly[1], shortestDist};
    }

}
