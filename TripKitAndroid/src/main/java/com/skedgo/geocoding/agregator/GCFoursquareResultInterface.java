package com.skedgo.geocoding.agregator;

import java.util.List;

public interface GCFoursquareResultInterface extends GCResultInterface {

//    foursquare json verified field
    boolean isVerified();
//    foursquare json categories names
    List<String> getCategories();

}
