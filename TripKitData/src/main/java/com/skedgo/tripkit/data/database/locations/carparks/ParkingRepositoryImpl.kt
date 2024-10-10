package com.skedgo.tripkit.data.database.locations.carparks

import com.skedgo.tripkit.data.database.TripKitDatabase
import com.skedgo.tripkit.data.locations.LocationsApi
import com.skedgo.tripkit.data.locations.StopsFetcher
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.location.GeoPoint
import com.skedgo.tripkit.parkingspots.ParkingRepository
import com.skedgo.tripkit.parkingspots.models.OffStreetParking
import com.skedgo.tripkit.parkingspots.models.OpeningHour
import com.skedgo.tripkit.parkingspots.models.ParkingOperator
import com.skedgo.tripkit.parkingspots.models.PricingEntry
import com.skedgo.tripkit.parkingspots.models.PricingTable
import com.skedgo.tripkit.utils.OptionalCompat
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalTime

internal class ParkingRepositoryImpl(
    val api: LocationsApi,
    val regionService: RegionService,
    val stopsFetcher: StopsFetcher, val tripKitDatabase: TripKitDatabase
) : ParkingRepository {

    override fun fetchCarParks(
        center: GeoPoint,
        cellIds: List<String>,
        zoomLevel: Int
    ): Observable<List<OffStreetParking>> {

        return regionService.getRegionByLocationAsync(center.latitude, center.longitude)
            .flatMap { stopsFetcher.fetchAsync(cellIds, it, zoomLevel) }
            .ignoreElements()
            .toObservable<List<OffStreetParking>>()
            .mergeWith(this.getByCellIds(cellIds))
    }

    override fun getByCellIds(ids: List<String>): Observable<List<OffStreetParking>> {
        return tripKitDatabase.carParkDao().getByCellIds(ids)
            .onBackpressureLatest()
            .observeOn(Schedulers.io())
            .map { it.map { convert(it) } }.toObservable()
    }

    private fun getOpeningTimesByCarParkId(carParkId: String): List<OpeningHour> {
        return tripKitDatabase.carParkDao().getOpeningTimesByCarParkId(carParkId)
            .map {
                OpeningHour(it.name, parse(it.opens), parse(it.closes))
            }
    }

    private fun getPricingTablesByCarParkId(carParkId: String): List<PricingTableEntity> {
        return tripKitDatabase.carParkDao().getPricingTablesByCarParkId(carParkId)
    }


    private fun getPricingEntriesByPricingTableId(pricingTableId: String): List<PricingEntryEntity> {
        return tripKitDatabase.carParkDao().getPricingEntriesByPricingTableId(pricingTableId)
    }

    private fun parse(hourMinute: String): LocalTime {
        val (hour, minutes) = hourMinute.split(":").take(2).map { it.toInt() }
        return LocalTime(hour % 24, minutes)
    }

    private fun convert(it: CarParkLocationEntity): OffStreetParking {
        val openingHours = getOpeningTimesByCarParkId(it.identifier)

        return OffStreetParking(
            it.identifier,
            it.name,
            GeoPoint(it.lat, it.lng),
            it.remoteIconName,
            it.localIconName,
            it.address,
            ParkingOperator(
                it.operator.name,
                OptionalCompat.ofNullable(it.operator.phone),
                OptionalCompat.ofNullable(it.operator.website)
            ),
            it.info.orEmpty(),
            openingHours,
            OptionalCompat.ofNullable(
                getPricingTablesByCarParkId(it.identifier)
                    .map {
                        PricingTable(
                            it.currency,
                            it.currencySymbol,
                            this.getPricingEntriesByPricingTableId(it.id)
                                .map {
                                    PricingEntry(
                                        OptionalCompat.ofNullable(it.maxDurationInMinutes),
                                        it.label.orEmpty(),
                                        it.price
                                    )
                                },
                            it.title,
                            OptionalCompat.ofNullable(it.subtitle)
                        )
                    }
            )
        )
    }
}