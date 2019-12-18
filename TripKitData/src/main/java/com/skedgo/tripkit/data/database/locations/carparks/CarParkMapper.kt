package com.skedgo.tripkit.data.database.locations.carparks

import java.util.*
import javax.inject.Inject

open class CarParkMapper @Inject constructor() {
  fun toEntity(cellId: String, carParks: List<CarParkLocation>):
      List<Triple<CarParkLocationEntity, Map<OpeningDayEntity, List<OpeningTimeEntity>>, Map<PricingTableEntity, List<PricingEntryEntity>>>> {
    return carParks.map { toEntity(cellId, it) }
  }

  private fun toEntity(cellId: String, carParkLocation: CarParkLocation):
      Triple<CarParkLocationEntity, Map<OpeningDayEntity, List<OpeningTimeEntity>>, Map<PricingTableEntity, List<PricingEntryEntity>>> {
    val entity = CarParkLocationEntity()
    entity.identifier = carParkLocation.id()
    entity.cellId = cellId
    entity.name = carParkLocation.name()
    entity.lat = carParkLocation.lat()
    entity.lng = carParkLocation.lng()
    entity.address = carParkLocation.address()
    entity.modeIdentifier = carParkLocation.modeInfo().id
    entity.localIconName = carParkLocation.modeInfo().localIconName
    entity.remoteIconName = carParkLocation.modeInfo().remoteIconName
    entity.operator = ParkingOperatorEntity().apply {
      this.name = carParkLocation.carPark().operator().name()
      this.phone = carParkLocation.carPark().operator().phone()
      this.website = carParkLocation.carPark().operator().website()
    }
    entity.info = carParkLocation.carPark().info()
    val openingDays = createOpeningDaysEntity(carParkLocation)
    val pricingTables = createPricingTablesEntity(carParkLocation)

    return Triple(entity, openingDays, pricingTables)
  }

  private fun createOpeningDaysEntity(carParkLocation: CarParkLocation): Map<OpeningDayEntity, List<OpeningTimeEntity>> {
    return carParkLocation
        .carPark()
        .openingHours()
        ?.days()
        .let { it ?: emptyList() }
        .map {
          val openingDayEntity = OpeningDayEntity()
              .apply {
                this.carParkId = carParkLocation.id()
                this.id = UUID.randomUUID().toString()
                this.name = it.name()
              }
          val openingTimes = it.times()
              .map {
                OpeningTimeEntity()
                    .apply {
                      this.openingDayId = openingDayEntity.id
                      this.opens = it.opens()
                      this.closes = it.closes()
                    }
              }
          openingDayEntity to openingTimes
        }
        .toMap()
  }

  private fun createPricingTablesEntity(carParkLocation: CarParkLocation): Map<PricingTableEntity, List<PricingEntryEntity>> {
    return carParkLocation
        .carPark()
        .pricingTables()
        .orEmpty()
        .map {
          val pricingTableEntity = PricingTableEntity()
              .apply {
                this.carParkId = carParkLocation.id()
                this.id = UUID.randomUUID().toString()
                this.title = it.title()
                this.subtitle = it.subtitle()
                this.currency = it.currency()
                this.currencySymbol = it.currencySymbol()
              }
          val pricingEntryEntities = it.entries()
              .map {
                PricingEntryEntity()
                    .apply {
                      this.pricingTableId = pricingTableEntity.id
                      this.label = it.label()
                      this.price = it.price()
                      this.maxDurationInMinutes = it.maxDurationInMinutes()
                    }
              }
          pricingTableEntity to pricingEntryEntities
        }
        .toMap()
  }
}
