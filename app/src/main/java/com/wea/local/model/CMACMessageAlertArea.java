package com.wea.local.model;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.TextContent;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name = "CMAC_Alert_Area")
public class CMACMessageAlertArea {
    @Xml(name = "CMAC_cmas_geocode")
    public static class CMACGeocode {
        @TextContent
        private String geocode;

        public String getGeocode() {
            return geocode;
        }

        public void setGeocode(String geocode) {
            this.geocode = geocode;
        }
    }
    @PropertyElement(name = "CMAC_area_description")
    private String areaDescription;

    @PropertyElement(name = "CMAC_polygon")
    private String polygon;

    @PropertyElement(name = "CMAC_circle")
    private String circle;

    @Element(name = "CMAC_cmas_geocode")
    private List<CMACGeocode> geocodeList;

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public List<CMACGeocode> getGeocodeList() {
        return geocodeList;
    }

    public void setGeocodeList(List<CMACGeocode> geocodeList) {
        this.geocodeList = geocodeList;
    }
}
