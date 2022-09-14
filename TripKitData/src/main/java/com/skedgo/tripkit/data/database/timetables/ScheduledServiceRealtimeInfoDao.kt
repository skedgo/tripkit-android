package com.skedgo.tripkit.data.database.timetables

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledServiceRealtimeInfoDao {

  @Insert
  fun insert(entities: List<ScheduledServiceRealtimeInfoEntity>)

  @Query("SELECT * FROM scheduledServiceRealtimeInfo WHERE id IN (:serviceIds)")
  fun getRealtimeInfoByService(serviceIds: List<Long>): Cursor
}