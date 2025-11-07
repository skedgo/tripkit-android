package com.skedgo.tripkit.data.database.locations.facility


import androidx.annotation.Keep

@Keep
data class FacilityLocation(
    var id: String = "",
    var cellId: String? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var address: String? = null,
    var timezone: String? = null,
    var city: String? = null,
    var region: String? = null,
    var name: String? = null,
    var facilityType: String? = null,
) {
    fun toEntity(): FacilityLocationEntity =
        FacilityLocationEntity().apply {
            identifier = this@FacilityLocation.id
            cellId = this@FacilityLocation.cellId
            lat = this@FacilityLocation.lat
            lng = this@FacilityLocation.lng
            address = this@FacilityLocation.address
            timezone = this@FacilityLocation.timezone
            city = this@FacilityLocation.city
            region = this@FacilityLocation.region
            name = this@FacilityLocation.name
            facilityType = this@FacilityLocation.facilityType
        }
}