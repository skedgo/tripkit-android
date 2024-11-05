package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCFoursquareResultInterface

/**
 * Represents the minimum information we need
 * to calculate the score for a foursquare result.
 */
class GCFoursquareResult
    (
    name: String, //    name is the value in name field in foursquare's json
    lat: Double, //    lat is the value in latitude field in on location field in foursquare's json
    lng: Double, //    lng is the value in longitude field in on location field in foursquare's json
    override var isVerified: Boolean,  //  value in verified field in foursquare's json
    override var categories: List<String> // each element for category is the value of each
) : GCResult(name, lat, lng), GCFoursquareResultInterface
