package com.skedgo.geocoding.agregator

interface GCSkedGoResultInterface : GCResultInterface {

    // Skedgo result class (class JSON field)
    val resultClass: String

    // Skedgo result popularity (popularity JSON field)
    val popularity: Int

    // Nullable list of mode identifiers
    val modeIdentifiers: List<String>?
}