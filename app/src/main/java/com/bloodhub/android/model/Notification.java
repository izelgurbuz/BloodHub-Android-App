package com.bloodhub.android.model;

/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class Notification {

    public int id, sentUserID;
    public double latitude, longitude;
    String placeName;
    public String msg, type, title, datestamp;

    public Notification(){}

    public Notification(int id, int sentUserID, String msg, String type, String title, String datestamp){
        this.datestamp = datestamp;
        this.id = id;
        this.sentUserID = sentUserID;
        this.msg = msg;
        this.title = title;
        this.type = type;
    }

    public Notification(int id, int sentUserID, String msg, String type, String title, String datestamp, double latitude, double longitude, String placeName){
        this.datestamp = datestamp;
        this.id = id;
        this.sentUserID = sentUserID;
        this.msg = msg;
        this.title = title;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public int getId() {
        return id;
    }

    public int getSentUserID() {
        return sentUserID;
    }

    public String getMsg() {
        return msg;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public double getLatitude(){return latitude;}

    public double getLongitude(){return longitude;}

    public String getPlaceName() {
        return placeName;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setSentUserID(int sentUserID) {
        this.sentUserID = sentUserID;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public void setLatitude(double latitude){this.latitude = latitude;}

    public void setLongitude(double longitude){this.longitude = longitude;}

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
