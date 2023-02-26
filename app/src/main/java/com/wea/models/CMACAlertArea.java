package com.wea.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name = "CMAC_Alert_Area")
public class CMACAlertArea {
    @PropertyElement(name = "CMAC_polygon")
    private String polygon;
    @PropertyElement(name = "CMAC_circle")
    private String circle;
    @Element(name = "CMAC_cmas_geocode")
    private List<String> geocodeList = new ArrayList<>();
    @Element(name = "CMAC_cap_geocode")
    private List<CMACCapGeocode> capGeocodeList = new ArrayList<>();

    public CMACAlertArea() { }

    public String getPolygon() {
        return polygon;
    }

    public String getCircle() {
        return circle;
    }

    public List<String> getGeocodeList() {
        return geocodeList;
    }

    public List<CMACCapGeocode> getCapGeocodeList() {
        return capGeocodeList;
    }
}
