package com.skedgo.tripkit.data.database.locations.carparks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface CarParkDao {
    @Query("SELECT * from carParks WHERE cellId IN (:ids)")
    fun getByCellIds(ids: List<String>): Flowable<List<CarParkLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(
        carParks: List<CarParkLocationEntity>,
        openingDays: List<OpeningDayEntity>,
        openingTimes: List<OpeningTimeEntity>,
        pricingTables: List<PricingTableEntity>,
        pricingEntries: List<PricingEntryEntity>
    )

    @Query("SELECT name, opens, closes from openingdayentity INNER JOIN openingtimeentity ON openingdayentity.id = openingDayId WHERE carParkId = :carParkId")
    fun getOpeningTimesByCarParkId(carParkId: String): List<OpeningTimeResult>

    @Query("SELECT * from pricingentryentity WHERE pricingTableId = :pricingTableId")
    fun getPricingEntriesByPricingTableId(pricingTableId: String): List<PricingEntryEntity>

    @Query("SELECT * from pricingtableentity WHERE carParkId = :carParkId")
    fun getPricingTablesByCarParkId(carParkId: String): List<PricingTableEntity>
}

class OpeningTimeResult {
    lateinit var name: String
    lateinit var opens: String
    lateinit var closes: String
}