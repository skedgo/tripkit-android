package com.skedgo.geocoding.agregator;

import org.jetbrains.annotations.NotNull;

public interface GCResultInterface {

//    result name
    @NotNull
    String getName();
//    result latitude
    Double getLat();
//    result longitude
    Double getLng();


}
