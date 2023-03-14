package com.wea.models;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "CMAC_Alert_Text")
public class CMACAlertText {
    @PropertyElement(name = "CMAC_short_text_alert_message")
    private String shortMessage;
    @PropertyElement(name = "CMAC_long_text_alert_message")
    private String longMessage;

    public CMACAlertText() { }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public void setLongMessage(String longMessage) {
        this.longMessage = longMessage;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getLongMessage() {
        return longMessage;
    }
}