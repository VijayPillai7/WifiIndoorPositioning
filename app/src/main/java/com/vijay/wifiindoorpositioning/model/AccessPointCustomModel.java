package com.vijay.wifiindoorpositioning.model;

public class AccessPointCustomModel {

    String BSSID;
    Double Frq;
    Double Level;

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public Double getFrq() {
        return Frq;
    }

    public void setFrq(Double frq) {
        Frq = frq;
    }

    public Double getLevel() {
        return Level;
    }

    public void setLevel(Double level) {
        Level = level;
    }
}

