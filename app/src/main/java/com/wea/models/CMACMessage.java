package com.wea.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Xml(name = "CMAC_Alert_Attributes")
public class CMACMessage {
    @PropertyElement(name = "CMAC_message_number")
    private String messageNumber;
    @PropertyElement(name = "CMAC_cap_identifier")
    private String capIdentifier;
    @PropertyElement(name = "CMAC_sent_date_time")
    private OffsetDateTime sentDateTime;
    @Element(name = "CMAC_alert_info")
    private CMACAlertInfo alertInfo;

    public CMACMessage() { }

    public String getMessageNumber() {
        return messageNumber;
    }

    public String getCapIdentifier() {
        return capIdentifier;
    }

    public OffsetDateTime getSentDateTime() {
        return sentDateTime.withOffsetSameInstant(ZoneOffset.UTC);
    }

    public CMACAlertInfo getAlertInfo() {
        return alertInfo;
    }
}
