package com.skedgo.tripkit.data.database.locations.facility

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class FacilityDao {
  @Query("SELECT * from facilities WHERE cellId IN (:cellIds)")
  abstract fun getAllInCells(cellIds: List<String>): Flowable<List<FacilityLocationEntity>>

  @Query("SELECT * from facilities WHERE cellId IN (:cellIds) AND :southWestLat < lat AND lat < :northEastLat AND :southWestLng < lng AND lng < :northEastLng")
  abstract fun getAllByCellsAndLatLngBounds(
      cellIds: List<String>,
      southWestLat: Double,
      southWestLng: Double,
      northEastLat: Double,
      northEastLng: Double): Flowable<List<FacilityLocationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun saveAll(facilities: List<FacilityLocationEntity>)

  @Query("SELECT * from facilities WHERE identifier == :id")
  abstract fun getById(id: String): FacilityLocationEntity
}