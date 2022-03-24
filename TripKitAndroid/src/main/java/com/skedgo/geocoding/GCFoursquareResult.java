package com.skedgo.geocoding;

import com.skedgo.geocoding.agregator.GCFoursquareResultInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents the minimum information we need
 * to calculate the score for a foursquare result.
 */
public class GCFoursquareResult extends GCResult implements GCFoursquareResultInterface {

//  value in verified field in foursquare's json
    private boolean verified;
    @NotNull
// each element for category is the value of each
// name field from each categories in foursquare's json
    private List<String> categories;

//    name is the value in name field in foursquare's json
//    lat is the value in latitude field in on location field in foursquare's json
//    lng is the value in longitude field in on location field in foursquare's json
    public GCFoursquareResult(String name, double lat, double lng,  boolean verified, @NotNull List<String> categories){
        super(name, lat, lng);
        this.verified = verified;
        this.categories = categories;
    }

    @Override
    public boolean isVerified() {
        return verified;
    }

    @NotNull
    @Override
    public List<String> getCategories() {
        return categories;
    }

    public void setIsVerified(boolean verified) {
        this.verified = verified;
    }

    public void setCategories(@NotNull List<String> categories) {
        this.categories = categories;
    }

}
