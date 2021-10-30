package com.vijay.wifiindoorpositioning.model;

import android.support.annotation.NonNull;

public class MatchedResultModel  implements Comparable<MatchedResultModel>{
    private  String bssid,wpid;
    private Double  lat, lon, calResult;

    public MatchedResultModel() {
    }

    public MatchedResultModel(String bssid, String wpid, Double lat, Double lon, Double calResult) {
        this.bssid = bssid;
        this.wpid = wpid;
        this.lat = lat;
        this.lon = lon;
        this.calResult = calResult;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getWpid() {
        return wpid;
    }

    public void setWpid(String wpid) {
        this.wpid = wpid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getCalResult() {
        return calResult;
    }

    public void setCalResult(Double calResult) {
        this.calResult = calResult;
    }

    @Override
    public int compareTo(@NonNull MatchedResultModel obj) {
        //ascending
        if (calResult == obj.getCalResult())
            return 0;
        else if (calResult > obj.getCalResult())
            return 1;
        else
            return -1;
    }
}
