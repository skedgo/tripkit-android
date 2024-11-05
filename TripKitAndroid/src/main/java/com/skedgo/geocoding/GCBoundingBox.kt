package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCBoundingBoxInterface;

import java.util.ArrayList;
import java.util.List;

public class GCBoundingBox implements GCBoundingBoxInterface {

    public static final GCBoundingBox World = new GCBoundingBox(85, -85, -180, 180);
    public double lat1, lat2, lng1, lng2;
    private double latitudeDelta = -1;
    private double longitudeDelta = -1;

    public GCBoundingBox(double lat1, double lat2, double lng1, double lng2) {
        if (lat1 < lat2) {
            this.lat1 = lat1;
            this.lat2 = lat2;
        } else {
            this.lat1 = lat2;
            this.lat2 = lat1;
        }
        if (lng1 < lng2) {
            this.lng1 = lng1;
            this.lng2 = lng2;
        } else {
            this.lng1 = lng2;
            this.lng2 = lng1;
        }
    }


    public GCBoundingBox(GCBoundingBoxInterface bb) {
        if (bb.getLatN() < bb.getLatS()) {
            this.lat1 = bb.getLatN();
            this.lat2 = bb.getLatS();
        } else {
            this.lat1 = bb.getLatS();
            this.lat2 = bb.getLatN();
        }
        if (bb.getLngW() < bb.getLngE()) {
            this.lng1 = bb.getLngW();
            this.lng2 = bb.getLngE();
        } else {
            this.lng1 = bb.getLngE();
            this.lng2 = bb.getLngW();
        }
    }

    public GCBoundingBox(//@NotNull
                         GCBoundingBox other) {
        lat1 = other.lat1;
        lat2 = other.lat2;
        lng1 = other.lng1;
        lng2 = other.lng2;
    }

    public int height() {
        // This constant is valid for all locations on Earth, since lines of latitude are equally spaced.
        return (int) ((lat2 - lat1) * 110852);
    }

    public LatLng center() {
        return new LatLng(((lat2 - lat1) / 2) + lat1, ((lng2 - lng1) / 2) + lng1);
    }

    public GCBoundingBox getBoundingBox() {
        return this;
    }

    public List<LatLng> getLatLngs() {
        List<LatLng> result = new ArrayList<>();
        result.add(new LatLng(lat1, lng1));
        result.add(new LatLng(lat1, lng2));
        result.add(new LatLng(lat2, lng2));
        result.add(new LatLng(lat2, lng1));
        return result;
    }

    public double getLatitudeDelta() {
        if (latitudeDelta == -1) {
            double latitudeSpan = lat2 - lat1;
            latitudeDelta = latitudeSpan * Math.PI / 180;
        }
        return latitudeDelta;
    }

    public double getLongitudeDelta() {
        if (longitudeDelta == -1) {
            longitudeDelta = Math.cos(getLatitudeDelta()) * Math.PI / 180;
        }
        return longitudeDelta;
    }

    @Override
    public double getLatN() {
        return lat1;
    }

    @Override
    public double getLatS() {
        return lat2;
    }

    @Override
    public double getLngW() {
        return lng1;
    }

    @Override
    public double getLngE() {
        return lng2;
    }
}
