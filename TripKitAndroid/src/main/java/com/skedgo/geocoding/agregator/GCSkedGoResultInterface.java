package com.skedgo.geocoding.agregator;

public interface GCSkedGoResultInterface extends GCResultInterface {

//    skedgo result class (class json field)
    String getResultClass();
//    skedgo result popularity (popularity json field)
    int getPopularity();

}
