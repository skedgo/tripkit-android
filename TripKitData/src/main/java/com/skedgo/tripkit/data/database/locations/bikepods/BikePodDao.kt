package com.skedgo.tripkit.data.database.locations.bikepods

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class BikePodDao {
  @Query("SELECT * from bikepods WHERE cellId IN (:cellIds)")
  abstract fun getAllInCells(cellIds: List<String>): Flowable<List<BikePodLocationEntity>>

  @Query("SELECT * from bikepods WHERE cellId IN (:cellIds) AND :southWestLat < lat AND lat < :northEastLat AND :southWestLng < lng AND lng < :northEastLng")
  abstract fun getAllByCellsAndLatLngBounds(
      cellIds: List<String>,
      southWestLat: Double,
      southWestLng: Double,
      northEastLat: Double,
      northEastLng: Double): Flowable<List<BikePodLocationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun saveAll(bikePods: List<BikePodLocationEntity>)

  @Query("SELECT * from bikepods WHERE identifier == :id")
  abstract fun getById(id: String): BikePodLocationEntity
}