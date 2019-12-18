package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.gojuno.koptional.toOptional
import com.skedgo.android.common.util.PolyUtil
import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import skedgo.tripgo.parkingspots.models.*
import skedgo.tripkit.location.GeoPoint
import skedgo.tripkit.parkingspots.OnStreetParkingRepository
import skedgo.tripkit.parkingspots.models.OnStreetParking
import skedgo.tripkit.parkingspots.models.Parking
import skedgo.tripkit.parkingspots.models.ParkingOperator
import skedgo.tripkit.parkingspots.models.PaymentType
import javax.inject.Inject

class OnStreetParkingRepositoryImpl @Inject constructor(
    val onStreetParkingApi: OnStreetParkingApi,
    val tripKitDatabase: TripKitDatabase) : OnStreetParkingRepository {

  override fun getByCellIds(ids: List<String>, southWest: GeoPoint, northEast: GeoPoint): Observable<List<OnStreetParking>> {
    return tripKitDatabase.onStreetParkingDao()
        .getByCellIds(ids = ids,
            southWestLat = southWest.latitude,
            southWestLng = southWest.longitude,
            northEastLat = northEast.latitude,
            northEastLng = northEast.longitude)
        .onBackpressureLatest()
        .observeOn(Schedulers.io())
        .map {
          it.map {
            OnStreetParking(
                id = it.identifier,
                location = GeoPoint(latitude = it.lat, longitude = it.lng),
                address = it.address,
                hasRestrictions = it.availableContent.split(",").contains("restrictions"),
                parkingOperator = ParkingOperator(it.operator.name, it.operator.phone.toOptional(), it.operator.website.toOptional()),
                encodedPolygon = PolyUtil.decode(it.encodedPolyline).map { GeoPoint(it.latitude, it.longitude) },
                info = it.info,
                name = it.name,
                parkingVacancy = it.parkingVacancy ?: Parking.Vacancy.UNKNOWN
            )
          }
        }.toObservable()
  }

  override fun getParkingDetails(onStreetParking: OnStreetParking): Single<OnStreetParkingDetails> {
    return onStreetParkingApi.fetchLocationInfoAsync(onStreetParking.id)
        .map { it.onStreetParking() }
        .map {
          OnStreetParkingDetails(
              actualRate = it.actualRate(),
              availableSpaces = it.availableSpaces(),
              lastUpdate = it.lastUpdate(),
              paymentTypes = it.paymentTypes()
                  ?.map { paymentType ->
                    PaymentType.values().find { it.value == paymentType }!!
                  },
              restrictions = it.restrictions()?.map {
                Restriction(
                    color = it.color(),
                    maximumParkingMinutes = it.maximumParkingMinutes(),
                    parkingSymbol = it.parkingSymbol(),
                    timezone = it.daysAndTimes().timeZone(),
                    type = it.type(),
                    daysAndTimes = it.daysAndTimes().days().map {
                      RestrictionDayAndTime(
                          name = it.name(),
                          times = it.times().map {
                            RestrictionTime(
                                opens = it.opens(),
                                closes = it.closes())
                          }
                      )
                    })
              },
              totalSpaces = it.totalSpaces())
        }
  }
}