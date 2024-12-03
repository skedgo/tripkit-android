package com.skedgo.geocoding.agregator

interface GCQueryInterface {

    // User query
    val queryText: String

    // User bounding box
    val bounds: GCBoundingBoxInterface
}