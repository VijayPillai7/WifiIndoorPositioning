package com.vijay.wifiindoorpositioning.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class AccessPoint extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Date createdAt = new Date();
    private String bssid;//identifier
    private String ssid;
    private double frq;
    private double level;//for RP (-50 to -100)
//    High quality: 90% ~= -55db
//    Medium quality: 50% ~= -75db
//    Low quality: 30% ~= -85db
//    Unusable quality: 8% ~= -96db

    public AccessPoint() {
    }

    public AccessPoint(AccessPoint another) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Calendar.getInstance().getTime();
        this.bssid = another.bssid;
        this.ssid = another.ssid;
        this.frq = another.frq;
        this.level = another.level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public double getFrq() {
        return frq;
    }

    public void setFrq(double frq) {
        this.frq = frq;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public static Creator<AccessPoint> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(bssid);
        parcel.writeString(ssid);
        parcel.writeDouble(frq);
        parcel.writeDouble(level);
    }

    protected AccessPoint(Parcel in) {
        id = in.readString();
        bssid = in.readString();
        ssid = in.readString();
        frq = in.readDouble();
        level = in.readDouble();
    }

    public static final Creator<AccessPoint> CREATOR = new Creator<AccessPoint>() {
        @Override
        public AccessPoint createFromParcel(Parcel in) {
            return new AccessPoint(in);
        }

        @Override
        public AccessPoint[] newArray(int size) {
            return new AccessPoint[size];
        }
    };
}
