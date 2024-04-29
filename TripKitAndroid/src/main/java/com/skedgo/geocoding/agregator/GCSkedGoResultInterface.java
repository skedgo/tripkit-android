package com.skedgo.geocoding.agregator;

import androidx.annotation.Nullable;

import java.util.List;

public interface GCSkedGoResultInterface extends GCResultInterface {

//    skedgo result class (class json field)
    String getResultClass();
//    skedgo result popularity (popularity json field)
    int getPopularity();

    @Nullable
    List<String> getModeIdentifiers();
}
