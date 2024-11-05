package com.skedgo.geocoding.agregator

interface GCResultInterface {

    // Result name
    val name: String

    // Result latitude
    val lat: Double?

    // Result longitude
    val lng: Double?
}