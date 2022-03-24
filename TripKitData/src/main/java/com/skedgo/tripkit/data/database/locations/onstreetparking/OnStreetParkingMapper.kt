package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.skedgo.tripkit.data.database.locations.carparks.ParkingOperatorEntity
import javax.inject.Inject


open class OnStreetParkingMapper @Inject constructor() {

  fun toEntity(cellId: String, onStreetParkingLocations: List<OnStreetParkingLocation>): List<OnStreetParkingEntity> {
    return onStreetParkingLocations.map { toEntity(cellId, it) }
  }

  private fun toEntity(cellId: String, onStreetParkingLocation: OnStreetParkingLocation): OnStreetParkingEntity {
    val entity = OnStreetParkingEntity()
    entity.identifier = onStreetParkingLocation.id()
    entity.cellId = cellId
    entity.name = onStreetParkingLocation.name()
    entity.lat = onStreetParkingLocation.lat()
    entity.lng = onStreetParkingLocation.lng()
    entity.address = onStreetParkingLocation.address()
    entity.modeIdentifier = onStreetParkingLocation.modeInfo().id
    entity.localIconName = onStreetParkingLocation.modeInfo().localIconName
    entity.remoteIconName = onStreetParkingLocation.modeInfo().remoteIconName
    entity.info = onStreetParkingLocation.onStreetParking().description()
    entity.availableContent = onStreetParkingLocation.onStreetParking().availableContent().joinToString(separator = ",")
    entity.parkingVacancy = onStreetParkingLocation.onStreetParking().parkingVacancy()
    entity.encodedPolyline = onStreetParkingLocation.onStreetParking().encodedPolyline()
    entity.operator = ParkingOperatorEntity().apply {
      this.name = onStreetParkingLocation.onStreetParking().source().provider().name()
      this.website = onStreetParkingLocation.onStreetParking().source().provider().website()
      this.phone = onStreetParkingLocation.onStreetParking().source().provider().phone()
    }
    return entity
  }
}