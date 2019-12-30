package com.skedgo.geocoding;

import com.skedgo.geocoding.agregator.GCAppResultInterface;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the the minimum information we need to calculate the score
 * for a result obtained from the information stored in the app by the user.
 */
public class GCAppResult extends GCResult implements GCAppResultInterface {

    @NotNull
//     address value
    private String subtitle;
//    is true if the result was set as favourite by the user false otherwise.
    private boolean isFavourite;
    @NotNull
//    source that provides the result
    private Source source;

    public GCAppResult(String name, double lat, double lng, @NotNull String address, boolean isFavourite, @NotNull Source source){
        super(name, lat, lng);
        this.subtitle = address;
        this.isFavourite = isFavourite;
        this.source = source;
    }

    @NotNull
    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public Source getAppResultSource() {
        return source;
    }

    @Override
    public boolean isFavourite() {
        return isFavourite;
    }

    public void setAppResultSource(Source source) {
        this.source = source;
    }

    public void setSubtitle(@NotNull String subtitle) {
        this.subtitle = subtitle;
    }

    public void setIsFavourite(boolean favourite) {
        this.isFavourite = favourite;
    }
}
