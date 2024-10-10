package com.skedgo.tripkit.parkingspots.models

import com.skedgo.tripkit.location.GeoPoint
import com.skedgo.tripkit.utils.OptionalCompat

sealed class Parking(
    val id: String,
    val name: String,
    val location: GeoPoint,
    val address: String?,
    val info: String,
    val parkingOperator: ParkingOperator,
    val parkingVacancy: Vacancy = Vacancy.UNKNOWN
) {
    enum class Vacancy {
        UNKNOWN,
        NO_VACANCY,
        LIMITED_VACANCY,
        PLENTY_VACANCY
    }

    override fun equals(other: Any?) =
        other != null && other is OffStreetParking && other.id == this.id

    override fun hashCode(): Int = this.id.hashCode()
}

open class OffStreetParking constructor(
    id: String,
    name: String,
    location: GeoPoint,
    val remoteIcon: String?,
    val localIconRes: String?,
    address: String?,
    parkingOperator: ParkingOperator,
    info: String,
    val openingHours: List<OpeningHour>,
    val pricingTables: OptionalCompat<List<PricingTable>>
) : Parking(id, name, location, address, info, parkingOperator)


class OnStreetParking(
    id: String,
    name: String,
    location: GeoPoint,
    address: String?,
    parkingOperator: ParkingOperator,
    info: String,
    val encodedPolygon: List<GeoPoint>,
    val hasRestrictions: Boolean,
    parkingVacancy: Vacancy
) : Parking(id, name, location, address, info, parkingOperator, parkingVacancy)