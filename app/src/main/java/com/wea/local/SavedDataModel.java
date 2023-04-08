package com.wea.local;

public class SavedDataModel {
    private String messageNumber;
    private String uri;
    private String dateTime;

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber.toUpperCase();
        for (int i = 0; this.messageNumber.length() < 8; i++) {
            this.messageNumber = "0" + this.messageNumber;
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
