package com.skedgo.tripkit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodDao
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodLocationEntity
import com.skedgo.tripkit.data.database.locations.carparks.*
import com.skedgo.tripkit.data.database.locations.carpods.CarPodDao
import com.skedgo.tripkit.data.database.locations.carpods.CarPodEntity
import com.skedgo.tripkit.data.database.locations.carpods.CarPodVehicle
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingDao
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingEntity
import com.skedgo.tripkit.data.database.stops.StopLocationDao
import com.skedgo.tripkit.data.database.stops.StopLocationEntity
import com.skedgo.tripkit.data.database.timetables.ScheduledServiceRealtimeInfoDao
import com.skedgo.tripkit.data.database.timetables.ScheduledServiceRealtimeInfoEntity
import skedgo.tripgo.data.timetables.ParentStopDao
import skedgo.tripgo.data.timetables.ParentStopEntity
import com.skedgo.tripkit.data.database.timetables.ServiceAlertsDao
import com.skedgo.tripkit.data.database.timetables.ServiceAlertsEntity

@Database(entities = [CarParkLocationEntity::class,
        OnStreetParkingEntity::class,
        BikePodLocationEntity::class,
        OpeningDayEntity::class,
        OpeningTimeEntity::class,
        CarPodEntity::class,
        PricingEntryEntity::class,
        CarPodVehicle::class,
        PricingTableEntity::class,
        StopLocationEntity::class,
        ScheduledServiceRealtimeInfoEntity::class,
        ParentStopEntity::class,
        ServiceAlertsEntity::class], version = 1)
abstract class TripKitDatabase : RoomDatabase() {
        abstract fun carParkDao(): CarParkDao
        abstract fun carPodDao(): CarPodDao
        abstract fun bikePodDao(): BikePodDao
        abstract fun stopLocationDao(): StopLocationDao
        abstract fun scheduledServiceRealtimeInfoDao(): ScheduledServiceRealtimeInfoDao
        abstract fun parentStopDao(): ParentStopDao
        abstract fun onStreetParkingDao(): OnStreetParkingDao
        abstract fun serviceAlertsDao(): ServiceAlertsDao
        companion object {
                fun getInstance(context: Context): TripKitDatabase {
                        return Room.databaseBuilder(context.applicationContext,
                                TripKitDatabase::class.java, "tripkit.db")
                                .fallbackToDestructiveMigrationFrom(1)
                                .build()
                }
        }

}