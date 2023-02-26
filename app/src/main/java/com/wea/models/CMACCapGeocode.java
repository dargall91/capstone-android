package com.wea.models;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "CMAC_cap_geocode")
public class CMACCapGeocode {
    @PropertyElement(name = "valueName")
    private String valueName;
    @PropertyElement(name = "value")
    private String value;

    public CMACCapGeocode() {
    }

    public String getValueName() {
        return valueName;
    }

    public String getValue() {
        return value;
    }
}