package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCSkedGoResultInterface;
import org.jetbrains.annotations.NotNull;

public class GCSkedgoResult extends GCResult implements GCSkedGoResultInterface {

    @NotNull
//  class json field from skedgo's json
    private String resultClass;
//  popularity json field from skedgo's json
    private int popularity;

    public GCSkedgoResult(String name, double lat, double lng, @NotNull String resultClass, Integer popularity ){
        super(name, lat, lng);
        this.popularity = popularity;
        this.resultClass = resultClass;
    }

    @NotNull
    @Override
    public String getResultClass() {
        return resultClass;
    }

    @Override
    public int getPopularity() {
        return popularity;
    }

    public void setResultClass(@NotNull String resultClass) {
        this.resultClass = resultClass;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isStopLocation(){
        return this.resultClass.equalsIgnoreCase("StopLocation");
    }
}
