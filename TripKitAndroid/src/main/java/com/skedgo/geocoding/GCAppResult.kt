package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCAppResultInterface
import com.skedgo.geocoding.agregator.GCAppResultInterface.Source

/**
 * Represents the the minimum information we need to calculate the score
 * for a result obtained from the information stored in the app by the user.
 */
class GCAppResult(
    name: String,
    lat: Double,
    lng: Double, //     address value
    override var subtitle: String, //    is true if the result was set as favourite by the user false otherwise.
    override var isFavourite: Boolean, //    source that provides the result
    override var appResultSource: Source
) : GCResult(
    name, lat, lng
), GCAppResultInterface
