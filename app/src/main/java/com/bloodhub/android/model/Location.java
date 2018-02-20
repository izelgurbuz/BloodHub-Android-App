package com.bloodhub.android.model;

/**
 * Created by mustafaculban on 15.02.2018.
 */

public class Location {

    private String placeName;
    private double latitute;
    private double longitude;

    public Location(){

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
