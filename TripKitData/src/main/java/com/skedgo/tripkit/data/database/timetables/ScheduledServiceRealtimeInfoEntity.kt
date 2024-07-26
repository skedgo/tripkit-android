package com.skedgo.tripkit.data.database.timetables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduledServiceRealtimeInfo")
class ScheduledServiceRealtimeInfoEntity {

    @PrimaryKey
    var id: Long = 0

    var realTimeDeparture: Int = 0
    var realTimeArrival: Int = 0
}