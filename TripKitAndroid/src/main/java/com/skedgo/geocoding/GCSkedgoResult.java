package com.skedgo.geocoding;


import com.skedgo.geocoding.agregator.GCSkedGoResultInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

public class GCSkedgoResult extends GCResult implements GCSkedGoResultInterface {

    @NotNull
//  class json field from skedgo's json
    private String resultClass;
    //  popularity json field from skedgo's json
    private int popularity;

    @Nullable
    private List<String> modeIdentifiers;

    public GCSkedgoResult(
        String name,
        double lat,
        double lng,
        @NotNull String resultClass,
        Integer popularity,
        @Nullable List<String> modeIdentifiers
    ) {
        super(name, lat, lng);
        this.popularity = popularity;
        this.resultClass = resultClass;
        this.modeIdentifiers = modeIdentifiers;
    }

    @NotNull
    @Override
    public String getResultClass() {
        return resultClass;
    }

    public void setResultClass(@NotNull String resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isStopLocation() {
        return this.resultClass.equalsIgnoreCase("StopLocation");
    }

    @Nullable
    @Override
    public List<String> getModeIdentifiers() {
        return modeIdentifiers;
    }
}
