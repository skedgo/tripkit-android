package com.skedgo.geocoding.agregator

interface GCFoursquareResultInterface : GCResultInterface {

    // Foursquare JSON verified field
    val isVerified: Boolean

    // Foursquare JSON categories names
    val categories: List<String>
}