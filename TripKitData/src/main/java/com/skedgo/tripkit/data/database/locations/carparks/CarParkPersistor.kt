package com.skedgo.tripkit.data.database.locations.carparks

import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class CarParkPersistor @Inject constructor(val tripKitDatabase: TripKitDatabase) {

  fun saveCarParks(carParkEntity: List<Triple<CarParkLocationEntity, Map<OpeningDayEntity, List<OpeningTimeEntity>>, Map<PricingTableEntity, List<PricingEntryEntity>>>>): Completable {
    return Completable
        .fromAction {
          val carParkEntities = carParkEntity.map { it.first }
          val openingDays = carParkEntity.map { it.second }.flatMap { it.keys }
          val openingTimes: List<OpeningTimeEntity> = carParkEntity.map { it.second }.flatMap { it.values }.flatMap { it }
          val pricingTables = carParkEntity.map { it.third }.flatMap { it.keys }
          val pricingEntries = carParkEntity.map { it.third }.flatMap { it.values }.flatMap { it }
          tripKitDatabase.carParkDao().saveAll(carParkEntities, openingDays, openingTimes, pricingTables, pricingEntries)
        }
        .subscribeOn(Schedulers.io())
  }
}