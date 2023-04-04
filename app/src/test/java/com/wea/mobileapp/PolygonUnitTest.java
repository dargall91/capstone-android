package com.wea.mobileapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.wea.local.DistanceOutsidePolygon;
import com.wea.local.LocationUtils;
import com.wea.models.Coordinate;

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
    public void distanceOutsideSmallPolygon() {

        String polygon = "38.47,-120.14 38.34,-119.95 38.52,-119.74 38.62,-119.89 38.47,-120.14";
        Coordinate myPoint = new Coordinate(38.45, -119.33);

        double actual = DistanceOutsidePolygon.distanceFromPolygon(myPoint, polygon);

        double expected = 0.0;

        assertEquals(actual, expected, 0.0);
    }
}