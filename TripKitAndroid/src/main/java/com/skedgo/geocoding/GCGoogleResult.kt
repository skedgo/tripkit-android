package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCGoogleResultInterface


class GCGoogleResult : GCResult, GCGoogleResultInterface {
    //  value in address field from google's json
    override var address: String? = null

    //    name is the value in name field in google's json
    //    lat is the value on lat field in on location field in google's json
    //    lng is the value on lat field in on location field in google's json
    constructor(name: String?, lat: Double, lng: Double, address: String?) : super(
        name!!, lat, lng
    ) {
        this.address = address
    }

    constructor(name: String?) : super(name!!, null, null)
}
