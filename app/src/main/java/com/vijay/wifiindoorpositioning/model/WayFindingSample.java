package com.vijay.wifiindoorpositioning.model;

public class WayFindingSample {
    Integer WPID;
    String BSSID;
    Double Frq;
    Double Level;
    Double Lat;
    Double Lon;

    public Integer getWPID() {
        return WPID;
    }

    public void setWPID(Integer WPID) {
        this.WPID = WPID;
    }

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

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }
}
