package com.skedgo.tripkit.data.database.locations.carpods

import com.skedgo.tripkit.data.database.TripKitDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.skedgo.tripkit.location.GeoPoint
import com.skedgo.tripkit.locations.*

import javax.inject.Inject

open class CarPodRepository @Inject constructor(private val database: TripKitDatabase) {
    fun saveCarPods(carPodEntities: List<Pair<CarPodEntity, List<CarPodVehicle>?>>): Completable {
        return Completable
                .fromAction {
                    database.carPodDao()
                            .saveAll(
                                    carPodEntities.map { it.first },
                                    carPodEntities.flatMap { it.second.orEmpty() }
                            )
                }
                .subscribeOn(Schedulers.io())
    }

    fun clearCarPods(): Completable {
        return Completable.fromAction {
            database.carPodDao().clearCarPods()
        }.subscribeOn(Schedulers.io())
    }

    fun getCarPodsByCellIds(cellIds: List<String>): Observable<List<CarPod>> {
        return database.carPodDao().getCarPodsByCellIds(cellIds)
                .onBackpressureLatest()
                .subscribeOn(Schedulers.io())
                .map { it.map { toModel(it, database.carPodDao().getVehiclesByCarPodId(it.id)) } }.toObservable()
    }

    fun getCarPod(id: String): Single<CarPod> {
        return Single
                .fromCallable {
                    database.carPodDao().getCarPodById(id)
                }
                .subscribeOn(Schedulers.io())
                .map {
                    toModel(it, database
                            .carPodDao().getVehiclesByCarPodId(it.id))
                }

    }

    private fun toModel(entity: CarPodEntity, vehicles: List<CarPodVehicle>?): CarPod {
        return entity.let {
            CarPod(
                    id = it.id,
                    address = it.address,
                    lat = it.lat,
                    lng = it.lng,
                    localIcon = it.localIcon,
                    name = it.name,
                    bookingURL = it.bookingURL,
                    deepLink = it.deepLink,
                    appLinkAndroid = it.appURLAndroid,
                    operator = Operator(it.operatorName, it.operatorPhone, it.operatorWebsite, AppInfo(it.appURLAndroid, it.deepLink)),
                    realTimeInfo = getRealTimeInfo(it),
                    remoteIcon = it.remoteIcon,
                    vehicles = vehicles?.map { Vehicle(name = it.name, fuelType = it.fuelType, licensePlate = it.licensePlate) },
                    garage = Garage(it.garageAddress),
                    modeInfo = ModeInfo(ModeInfoColor(it.red, it.blue, it.green))
            )
        }
    }

    private fun getRealTimeInfo(entity: CarPodEntity): RealTimeInfo? {
        return entity
                .let {
                    val inService = it.inService
                    val totalSpaces = it.totalSpaces
                    val availableChargingSpaces = it.availableChargingSpaces
                    val availableVehicles = it.availableVehicles
                    val lastUpdate = it.lastUpdate
                    if (inService != null
                            && totalSpaces != null
                            && availableChargingSpaces != null
                            && availableVehicles != null
                            && lastUpdate != null) {
                        RealTimeInfo(
                                availableChargingSpaces = availableChargingSpaces,
                                availableVehicles = availableVehicles,
                                inService = inService,
                                lastUpdate = lastUpdate,
                                totalSpaces = totalSpaces
                        )
                    } else {
                        null
                    }
                }
    }

    fun getCarPodsByCellIdsWithinBounds(cellIds: List<String>, southwest: GeoPoint, northEast: GeoPoint): Observable<List<CarPod>> {
        return database.carPodDao().getCarPodsByCellIds(cellIds)
                .onBackpressureLatest()
                .map { it.map { toModel(it, database.carPodDao().getVehiclesByCarPodId(it.id)) } }
                .subscribeOn(Schedulers.io()).toObservable()
    }
}
