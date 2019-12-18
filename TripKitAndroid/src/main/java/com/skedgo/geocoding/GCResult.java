package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCResultInterface;
import org.jetbrains.annotations.NotNull;

public class GCResult implements GCResultInterface {

    @NotNull
    private String name;
    private Double lat;
    private Double lng;

    public GCResult(@NotNull String name, Double lat, Double lng){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public GCResult(){

    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
