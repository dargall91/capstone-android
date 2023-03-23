package com.wea.models;

public class CollectedDeviceData {
    private int messageNumber;
    private String capIdentifier;
    private String timeReceived;
    private String timeDisplayed;
    private boolean receivedInside;
    private boolean messagePresented;
    private boolean locationAvailable;
    private float distanceFromPolygon;
    private boolean optedOut;

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

    public String getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

    public String getTimeDisplayed() {
        return timeDisplayed;
    }

    public void setTimeDisplayed(String timeDisplayed) {
        this.timeDisplayed = timeDisplayed;
    }

    public boolean isReceivedInside() {
        return receivedInside;
    }

    public void setReceivedInside(boolean receivedInside) {
        this.receivedInside = receivedInside;
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

    public boolean isOptedOut() {
        return optedOut;
    }

    public void setOptedOut(boolean optedOut) {
        this.optedOut = optedOut;
    }
}
