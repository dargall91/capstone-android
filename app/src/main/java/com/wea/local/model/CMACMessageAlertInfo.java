package com.wea.local.model;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name = "CMAC_alert_info")
public class CMACMessageAlertInfo {
    @PropertyElement(name = "CMAC_category")
    private String category;
    @PropertyElement(name = "CMAC_severity")
    private String severity;
    @PropertyElement(name = "CMAC_urgency")
    private String urgency;
    @PropertyElement(name = "CMAC_certainty")
    private String certainty;
    @PropertyElement(name = "CMAC_expires_date_time")
    private String expires;

    @PropertyElement(name = "CMAC_sender_name")
    private String senderName;

    @Element(name = "CMAC_Alert_Area")
    private List<CMACMessageAlertArea> alertAreaList;

    @Element(name = "CMAC_Alert_Text")
    private List<CMACMessageAlertText> alertTextList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getCertainty() {
        return certainty;
    }

    public void setCertainty(String certainty) {
        this.certainty = certainty;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<CMACMessageAlertArea> getAlertAreaList() {
        return alertAreaList;
    }

    public void setAlertAreaList(List<CMACMessageAlertArea> alertAreaList) {
        this.alertAreaList = alertAreaList;
    }

    public List<CMACMessageAlertText> getAlertTextList() {
        return alertTextList;
    }

    public void setAlertTextList(List<CMACMessageAlertText> alertTextList) {
        this.alertTextList = alertTextList;
    }

    /**
     * Gets the short message of the chosen language, or the english
     * message if the target language is not found
     *
     * @param language The target language
     * @return The CMAC_short_text_alert_message in the chosen language
     */
    public String getShortMessage(String language) {
        //indexing for english alert if target language is not found
        int english = 0;
        for (int i = 0; i < alertTextList.size(); i++) {
            if (alertTextList.get(i).getLanguage().equalsIgnoreCase(language)) {
                return alertTextList.get(i).getShortMessage();
            } else if (alertTextList.get(i).getLanguage().equalsIgnoreCase("english")) {
                english = i;
            }
        }

        return alertTextList.get(english).getShortMessage();
    }

    /**
     * Gets the long message of the chosen language, or the english
     * message if the target language is not found
     *
     * @param language The target language
     * @return The CMAC_long_text_alert_message in the chosen language
     */
    public String getLongMessage(String language) {
        //indexing for english alert if target language is not found
        int english = 0;
        for (int i = 0; i < alertTextList.size(); i++) {
            if (alertTextList.get(i).getLanguage().equalsIgnoreCase(language)) {
                return alertTextList.get(i).getLongMessage();
            } else if (alertTextList.get(i).getLanguage().equalsIgnoreCase("english")) {
                english = i;
            }
        }

        return alertTextList.get(english).getLongMessage();
    }
}
