package com.bloodhub.android.model;

/**
 * Created by mustafaculban on 15.02.2018.
 */

public class Location {

    private String placeName;
    private String latitute;
    private String longitude;
    private int id;
    private String city;

    public Location(int id, String city, String placeName, String latitute, String longitude){
        this.id = id;
        this.city = city;
        this.placeName = placeName;
        this.latitute = latitute;
        this.longitude = longitude;
    }

    public Location(){}

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

}
