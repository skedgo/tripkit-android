package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class OnStreetParkingPersistor @Inject constructor(val tripKitDatabase: TripKitDatabase) {

  fun saveOnStreetParkings(onStreetParkings: List<OnStreetParkingEntity>): Completable {
    return Completable
        .fromAction {
            tripKitDatabase.onStreetParkingDao().saveAll(onStreetParkings)
        }
        .subscribeOn(Schedulers.io())
  }
}