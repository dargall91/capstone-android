package com.wea.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "CMAC_Alert_Attributes")
public class CMACMessage {
    @PropertyElement(name = "Cmac_message_number")
    private String messageNumber;
    @PropertyElement(name = "Cmac_cap_identifier")
    private String capIdentifier;
    @PropertyElement(name = "Cmac_message_type")
    private String messageType;
    @PropertyElement(name = "CMAC_sent_date_time")
    private String sentDateTime;
    @Element(name = "CMAC_alert_info")
    private CMACAlertInfo alertInfo;

    public CMACMessage() { }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public void setCapIdentifier(String capIdentifier) {
        this.capIdentifier = capIdentifier;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setSentDateTime(String sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public void setAlertInfo(CMACAlertInfo alertInfo) {
        this.alertInfo = alertInfo;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public String getCapIdentifier() {
        return capIdentifier;
    }

    public String getSentDateTime() {
        return sentDateTime;
    }

    public CMACAlertInfo getAlertInfo() {
        return alertInfo;
    }
}
