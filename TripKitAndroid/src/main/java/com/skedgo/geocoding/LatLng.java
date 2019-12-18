package com.skedgo.geocoding;


import org.jetbrains.annotations.NotNull;

public class LatLng {

    public static final double NO_NUM = -3e11;

    public double lat, lng;

    @NotNull
    public static LatLng nullLatLong = new LatLng(0,0);


    public LatLng() { }

    public LatLng(double _lat, double _lng)
    {
        lat = _lat;
        lng = _lng;
    }

    public LatLng(LatLng other)
    {
        lat = other.lat;
        lng = other.lng;
    }


    public static final double EarthRadius = 6371000;
    static public final double radians = 3.14159/180;

    /** This is the Equirectangular approximation. It's a little slower than the Region.distanceInMetres()
     * formula. */
    public double distanceInMetres(//@NotNull
                                   LatLng other)
    {
        double lngDelta = Math.abs(lng - other.lng);
        if (lngDelta > 180)
            lngDelta = 360 - lngDelta;
        double p1 = lngDelta * Math.cos(0.5*radians*(lat + other.lat));
        double p2 = (lat - other.lat);
        return EarthRadius * radians * Math.sqrt( p1*p1 + p2*p2);
    }

}
