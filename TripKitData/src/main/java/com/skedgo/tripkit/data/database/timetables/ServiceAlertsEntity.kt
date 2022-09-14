package com.skedgo.tripkit.data.database.timetables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings

@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
@Entity(tableName = "serviceAlerts")
data class ServiceAlertsEntity(
        @PrimaryKey val id: String,
        val serviceTripId: String,
        val title: String,
        val remoteHashCode: Long,
        val severity: String,
        val text: String? = null,
        val url: String? = null,
        val remoteIcon: String? = null,
        @Embedded(prefix = "location_")
    val location: AlertLocationEntity? = null,
        val lastUpdated: Long = -1,
        val fromDate: Long = -1,
        @Embedded(prefix = "action_")
    val action: AlertActionEntity? = null
)

data class AlertLocationEntity(
    val lat: Double,
    val lng: Double,
    val timezone: String,
    val address: String? = null)


data class AlertActionEntity(
    val text: String,
    val type: String,
    val excludedStopCodes: String)