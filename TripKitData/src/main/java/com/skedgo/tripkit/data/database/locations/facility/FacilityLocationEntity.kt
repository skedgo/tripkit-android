package com.skedgo.tripkit.data.database.locations.facility


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facilities")
@Keep
class FacilityLocationEntity {
    @PrimaryKey
    var identifier: String = ""
    var cellId: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    var address: String? = null
    var timezone: String? = null
    var city: String? = null
    var region: String? = null
    var name: String? = null
    var facilityType: String? = null
}