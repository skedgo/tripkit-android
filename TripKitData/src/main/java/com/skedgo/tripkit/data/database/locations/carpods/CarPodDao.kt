package com.skedgo.tripkit.data.database.locations.carpods

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface CarPodDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveAll(entities: List<CarPodEntity>, vehicles: List<CarPodVehicle>)

  @Query("SELECT * from carPods WHERE cellId IN (:cellIds) AND :southWestLat < lat AND lat < :northEastLat AND :southWestLng < lng AND lng < :northEastLng")
  fun getCarPodsByCellIdsWithinBounds(cellIds: List<String>,
                                      southWestLat: Double,
                                      southWestLng: Double,
                                      northEastLat: Double,
                                      northEastLng: Double): Flowable<List<CarPodEntity>>

  @Query("SELECT * from carPods WHERE cellId IN (:cellIds)")
  fun getCarPodsByCellIds(cellIds: List<String>): Flowable<List<CarPodEntity>>

  @Query("SELECT * from carPods WHERE id == :id")
  fun getCarPodById(id: String): CarPodEntity

  @Query("SELECT * FROM carPodVehicles WHERE carPodId == :carPodId")
  fun getVehiclesByCarPodId(carPodId: String): List<CarPodVehicle>?

  @Query("DELETE FROM carPods")
  fun clearCarPods()
}