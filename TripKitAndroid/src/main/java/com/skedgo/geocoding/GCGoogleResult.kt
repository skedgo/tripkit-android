package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCGoogleResultInterface;

public class GCGoogleResult extends GCResult implements GCGoogleResultInterface {

    //  value in address field from google's json
    private String address;

    //    name is the value in name field in google's json
//    lat is the value on lat field in on location field in google's json
//    lng is the value on lat field in on location field in google's json
    public GCGoogleResult(String name, double lat, double lng, String address) {
        super(name, lat, lng);
        this.address = address;
    }

    public GCGoogleResult(String name) {
        super(name, null, null);
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
