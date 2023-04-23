package com.wea.mobileapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.wea.local.DistanceOutsidePolygon;
import com.wea.local.LocationUtils;
import com.wea.models.Coordinate;

import java.time.Duration;
import java.time.Instant;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PolygonUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void pointInsidePolygon() {

        String polygon = "38.47,-120.14 38.34,-119.95 38.52,-119.74 38.62,-119.89 38.47,-120.14";
        Double[] myPoint = {38.45, -119.99};

        boolean actual = LocationUtils.isInsideArea(polygon, myPoint);
        boolean expected = true;

        assertEquals(actual, expected);
    }

    @Test
    public void pointOutsidePolygon() {

        String polygon = "38.47,-120.14 38.34,-119.95 38.52,-119.74 38.62,-119.89 38.47,-120.14";
        Double[] myPoint = {38.45, -119.33};

        boolean actual = LocationUtils.isInsideArea(polygon, myPoint);
        boolean expected = false;

        assertEquals(actual, expected);
    }

    @Test
    public void distanceOutsideSmallPolygonShort() {

        String polygon = "38.47,-120.14 38.34,-119.95 38.52,-119.74 38.62,-119.89 38.47,-120.14";
        Coordinate myPoint = new Coordinate(38.45, -119.33);

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        double expected = 0.4;

        assertEquals(actual, expected, 0.3);
    }

    @Test
    public void distanceOutsideSmallPolygonLong() {

        String polygon = "38.47,-120.14 38.34,-119.95 38.52,-119.74 38.62,-119.89 38.47,-120.14";
        Coordinate myPoint = new Coordinate(99.45, -139.33);

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        double expected = 63.8;

        assertEquals(actual, expected, 0.3);
    }

    @Test
    public void distanceOutsideLargePolygonShort() {

        String polygon = "32.61,-84.36 32.74,-84.3 32.74,-84.29 32.75,-84.29 32.79,-84.27 32.74,-84.0 32.56,-84.07 32.61,-84.36";
        Coordinate myPoint = new Coordinate(32.45, -85.33);

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        double expected = 1.0;

        assertEquals(actual, expected, 0.3);
    }

    @Test
    public void distanceOutsideLargePolygonLong() {

        String polygon = "32.61,-84.36 32.74,-84.3 32.74,-84.29 32.75,-84.29 32.79,-84.27 32.74,-84.0 32.56,-84.07 32.61,-84.36";
        Coordinate myPoint = new Coordinate(38.45, -119.33);

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        double expected = 35.4;

        assertEquals(actual, expected, 0.3);
    }

    @Test
    public void distanceOutsidePolygonBenchmark() {

        double expected = 1.0;

        String polygon = "37.74,-121.04 37.87,-121.04 37.95,-120.83 37.74,-120.81 37.49,-120.76 37.48,-120.79 37.47,-120.8 37.47,-120.81 37.47,-120.82 37.46,-120.82 37.46,-120.83 37.46,-120.84 37.45,-120.85 37.45,-120.87 37.44,-120.88 37.46,-121.02 37.74,-121.04";
        Coordinate myPoint = new Coordinate(38.45, -119.33);

        Instant start = Instant.now();

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        Instant finish = Instant.now();

        double elapsed = Duration.between(start, finish).toMillis();

        boolean benchmark;
        if (elapsed < expected){
            benchmark = true;
        } else {
            benchmark = false;
        }

        assertTrue(benchmark);

    }
}