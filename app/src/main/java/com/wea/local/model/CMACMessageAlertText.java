package com.wea.local.model;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "CMAC_Alert_Text")
public class CMACMessageAlertText {
    @PropertyElement(name = "CMAC_text_language")
    private String language;
    @PropertyElement(name = "CMAC_short_text_alert_message")
    private String shortMessage;
    @PropertyElement(name = "CMAC_long_text_alert_message")
    private String longMessage;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    /**
     * Returns the long message with all excess whitepsace removed
     * @return CMAC_long_text_alert_message
     */
    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage.replaceAll("\\s+", " ");
    }

    /**
     * Returns the long message with all excess whitepsace removed
     * @return CMAC_long_text_alert_message
     */
    public String getLongMessage() {
        return longMessage.replaceAll("\\s+", " ");
    }

    public void setLongMessage(String longMessage) {
        this.longMessage = longMessage;
    }
}
