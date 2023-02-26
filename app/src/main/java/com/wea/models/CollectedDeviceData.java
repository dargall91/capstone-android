package com.wea.models;

import java.time.OffsetDateTime;

public class CollectedDeviceData {
    private int messageNumber;
    private String capIdentifier;
    private OffsetDateTime timeReceived;
    private OffsetDateTime timeDisplayed;
    private boolean receivedInside;
    private boolean displayedInside;
    private boolean messagePresented;
    private boolean locationAvailable;
    private float distanceFromPolygon;

    public CollectedDeviceData(CMACMessage message, boolean locationAvailable, boolean receivedInside) {
        messageNumber = Integer.parseInt(message.getMessageNumber(), 16);
        capIdentifier = message.getCapIdentifier();;
        this.locationAvailable = locationAvailable;
        this.receivedInside = receivedInside;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getCapIdentifier() {
        return capIdentifier;
    }

    public void setCapIdentifier(String capIdentifier) {
        this.capIdentifier = capIdentifier;
    }

    public OffsetDateTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(OffsetDateTime timeReceived) {
        this.timeReceived = timeReceived;
    }

    public OffsetDateTime getTimeDisplayed() {
        return timeDisplayed;
    }

    public void setTimeDisplayed(OffsetDateTime timeDisplayed) {
        this.timeDisplayed = timeDisplayed;
    }

    public boolean isReceivedInside() {
        return receivedInside;
    }

    public void setReceivedInside(boolean receivedInside) {
        this.receivedInside = receivedInside;
    }

    public boolean isDisplayedInside() {
        return displayedInside;
    }

    public void setDisplayedInside(boolean displayedInside) {
        this.displayedInside = displayedInside;
    }

    public boolean isMessagePresented() {
        return messagePresented;
    }

    public void setMessagePresented(boolean messagePresented) {
        this.messagePresented = messagePresented;
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }

    public void setLocationAvailable(boolean locationAvailable) {
        this.locationAvailable = locationAvailable;
    }

    public float getDistanceFromPolygon() {
        return distanceFromPolygon;
    }

    public void setDistanceFromPolygon(float distanceFromPolygon) {
        this.distanceFromPolygon = distanceFromPolygon;
    }
}
