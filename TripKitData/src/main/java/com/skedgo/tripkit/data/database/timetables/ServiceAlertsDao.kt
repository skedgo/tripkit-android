package com.skedgo.tripkit.data.database.timetables

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ServiceAlertsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlerts(alerts: List<ServiceAlertsEntity>)

    @Delete
    fun deleteAlertByService(alerts: List<ServiceAlertsEntity>)

    @Query("SELECT * FROM serviceAlerts WHERE serviceTripId == :serviceId")
    fun getAlertForService(serviceId: String): Single<List<ServiceAlertsEntity>>
}