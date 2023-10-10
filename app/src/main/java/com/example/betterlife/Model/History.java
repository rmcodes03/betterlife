package com.example.betterlife.Model;

public class History {
    private String donor;
    private String expiryDate;
    private String medicineName;
    private String quantity;
    private String type;
    private String historyId;
    private String date;

    public History() {
    }

    public History(String donor, String expiryDate, String medicineName, String quantity, String type, String historyId, String date) {
        this.donor = donor;
        this.expiryDate = expiryDate;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.type = type;
        this.historyId = historyId;
        this.date = date;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
