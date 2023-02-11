package com.wea.local;

import java.util.ArrayList;

public class DistanceOutsidePolygon {
    private double[] point;
    private String polygon;

    DistanceOutsidePolygon(double[] point, String polygon){
        this.point = point;
        this.polygon = polygon;
    }

    private double vectorLength(double[] vector){
        return Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
    }

    private double[] vectorNegate(double[] vector){
        double[] negative = {-vector[0], -vector[1]};
        return negative;
    }

    private double[] vectorAdd(double[] vector1, double[] vector2){
        double[] addition = {vector1[0] + vector2[0], vector1[1] + vector2[1]};
        return addition;
    }

    private double[] vectorSubtract(double[] vector1, double[] vector2){
        double[] subtraction = {vector1[0] - vector2[0], vector1[1] - vector2[1]};
        return subtraction;
    }

    private double[] vectorScale(double[] vector, double factor) {
        double[] scaled = {vector[0] * factor, vector[1] * factor};
        return scaled;
    }

    private double[] vectorNormalization(double[] vector) {
        double[] normalized = {-vector[1], vector[0]};
        return normalized;
    }

    public double[] findDistance(){
        String[] coordsSplit = polygon.split(" ");

        double[] x = new double[coordsSplit.length];
        double[] y = new double[coordsSplit.length];

        for (int i = 0; i < coordsSplit.length; i++){
            String[] s = coordsSplit[i].split(",");
            x[i] = (Double.parseDouble(s[0]));
            y[i] = (Double.parseDouble(s[1]));
        }



        double[] shortestPoint = new double[2];
        return shortestPoint;
    }
}
