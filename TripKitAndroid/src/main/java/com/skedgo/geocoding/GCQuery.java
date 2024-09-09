package com.skedgo.geocoding;

import com.skedgo.geocoding.agregator.GCQueryInterface;

import org.jetbrains.annotations.NotNull;

public class GCQuery implements GCQueryInterface {

    @NotNull
//  user search input
    private String term;
    @NotNull
//  bounding box on user screen
    private GCBoundingBox boundingBox;


    public GCQuery(@NotNull String term, @NotNull GCBoundingBox boundsData) {
        this.term = term;
        this.boundingBox = boundsData;
//        this.boundingBox = new GCBoundingBox(boundsData.getLatN(), boundsData.getLatS(), boundsData.getLngW(), boundsData.getLngE());
    }

    @NotNull
    @Override
    public String getQueryText() {
        return term;
    }

    public void setQueryText(String term) {
        this.term = term;
    }

    @NotNull
    @Override
    public GCBoundingBox getBounds() {
        return boundingBox;
    }

    public void setBounds(GCBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }


}
