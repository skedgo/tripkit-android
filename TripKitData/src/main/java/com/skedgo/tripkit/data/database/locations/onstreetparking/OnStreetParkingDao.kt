package com.skedgo.tripkit.data.database.locations.onstreetparking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface OnStreetParkingDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveAll(onStreetParkings: List<OnStreetParkingEntity>)

  @Query("SELECT * from onStreetParkings WHERE cellId IN (:ids) AND :southWestLat < lat  AND lat < :northEastLat AND :southWestLng < lng AND lng < :northEastLng ")
  fun getByCellIds(ids: List<String>,
                   southWestLat: Double,
                   southWestLng: Double,
                   northEastLat: Double,
                   northEastLng: Double): Flowable<List<OnStreetParkingEntity>>

}