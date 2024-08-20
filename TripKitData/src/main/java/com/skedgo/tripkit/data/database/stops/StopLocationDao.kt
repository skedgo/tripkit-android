package com.skedgo.tripkit.data.database.stops

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class StopLocationDao {
    @Query("SELECT * FROM stopLocations WHERE code IN (:stopCodes)")
    abstract fun getStopsByStopCodes(stopCodes: List<String>): List<StopLocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveAll(stopLocations: List<StopLocationEntity>)
}