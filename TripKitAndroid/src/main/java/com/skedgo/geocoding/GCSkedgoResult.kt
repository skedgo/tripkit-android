package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCSkedGoResultInterface


class GCSkedgoResult(
    name: String?,
    lat: Double,
    lng: Double,
    //  class json field from skedgo's json
    override var resultClass: String,
    //  popularity json field from skedgo's json
    override var popularity: Int,
    override val modeIdentifiers: List<String>?
) : GCResult(name!!, lat, lng), GCSkedGoResultInterface {
    val isStopLocation: Boolean
        get() = resultClass.equals("StopLocation", ignoreCase = true)
}
