package com.wea.models;


import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name = "CMAC_alert_info")
public class CMACAlertInfo {
    @PropertyElement(name = "CMAC_expires_date_time")
    private String expires;
    @Element(name = "CMAC_Alert_Area")
    private List<CMACAlertArea> alertAreaList = new ArrayList<>();
    @Element(name = "CMAC_Alert_Text")
    private List<CMACAlertText> alertTextList = new ArrayList<>();

    public CMACAlertInfo() { }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void setAlertAreaList(List<CMACAlertArea> alertAreaList) {
        this.alertAreaList = alertAreaList;
    }

    public void setAlertTextList(List<CMACAlertText> alertTextList) {
        this.alertTextList = alertTextList;
    }

    public String getExpires() {
        return expires;
    }

    public List<CMACAlertArea> getAlertAreaList() {
        return alertAreaList;
    }

    public List<CMACAlertText> getAlertTextList() {
        return alertTextList;
    }
}
