package com.skedgo.tripkit.data.database.locations.freefloating

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class FreeFloatingLocationDao {
  @Query("SELECT * from freeFloatingLocations WHERE cellId IN (:cellIds)")
  abstract fun getAllInCells(cellIds: List<String>): Flowable<List<FreeFloatingLocationEntity>>

  @Query("SELECT * from freeFloatingLocations WHERE cellId IN (:cellIds) AND :southWestLat < lat AND lat < :northEastLat AND :southWestLng < lng AND lng < :northEastLng")
  abstract fun getAllByCellsAndLatLngBounds(
      cellIds: List<String>,
      southWestLat: Double,
      southWestLng: Double,
      northEastLat: Double,
      northEastLng: Double): Flowable<List<FreeFloatingLocationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun saveAll(freeFloatingLocations: List<FreeFloatingLocationEntity>)

  @Query("SELECT * from freeFloatingLocations WHERE identifier == :id")
  abstract fun getById(id: String): FreeFloatingLocationEntity
}