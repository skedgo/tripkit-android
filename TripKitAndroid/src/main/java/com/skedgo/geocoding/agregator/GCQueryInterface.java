package com.skedgo.geocoding.agregator;

import org.jetbrains.annotations.NotNull;

public interface GCQueryInterface {

//    user query
    @NotNull
    String getQueryText();
//    user bounding box
    @NotNull
    GCBoundingBoxInterface getBounds();

}
