package com.skedgo.tripkit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skedgo.tripkit.data.database.booking.ticket.TicketDao
import com.skedgo.tripkit.data.database.booking.ticket.TicketEntity
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodDao
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodLocationEntity
import com.skedgo.tripkit.data.database.locations.carparks.CarParkDao
import com.skedgo.tripkit.data.database.locations.carparks.CarParkLocationEntity
import com.skedgo.tripkit.data.database.locations.carparks.OpeningDayEntity
import com.skedgo.tripkit.data.database.locations.carparks.OpeningTimeEntity
import com.skedgo.tripkit.data.database.locations.carparks.PricingEntryEntity
import com.skedgo.tripkit.data.database.locations.carparks.PricingTableEntity
import com.skedgo.tripkit.data.database.locations.carpods.CarPodDao
import com.skedgo.tripkit.data.database.locations.carpods.CarPodEntity
import com.skedgo.tripkit.data.database.locations.carpods.CarPodVehicle
import com.skedgo.tripkit.data.database.locations.facility.FacilityDao
import com.skedgo.tripkit.data.database.locations.facility.FacilityLocationEntity
import com.skedgo.tripkit.data.database.locations.freefloating.FreeFloatingLocationDao
import com.skedgo.tripkit.data.database.locations.freefloating.FreeFloatingLocationEntity
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingDao
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingEntity
import com.skedgo.tripkit.data.database.stops.StopLocationDao
import com.skedgo.tripkit.data.database.stops.StopLocationEntity
import com.skedgo.tripkit.data.database.timetables.ScheduledServiceRealtimeInfoDao
import com.skedgo.tripkit.data.database.timetables.ScheduledServiceRealtimeInfoEntity
import com.skedgo.tripkit.data.database.timetables.ServiceAlertsDao
import com.skedgo.tripkit.data.database.timetables.ServiceAlertsEntity
import skedgo.tripgo.data.timetables.ParentStopDao
import skedgo.tripgo.data.timetables.ParentStopEntity

@Database(
    entities = [
        CarParkLocationEntity::class,
        OnStreetParkingEntity::class,
        BikePodLocationEntity::class,
        FreeFloatingLocationEntity::class,
        OpeningDayEntity::class,
        OpeningTimeEntity::class,
        CarPodEntity::class,
        PricingEntryEntity::class,
        CarPodVehicle::class,
        PricingTableEntity::class,
        StopLocationEntity::class,
        ScheduledServiceRealtimeInfoEntity::class,
        ParentStopEntity::class,
        ServiceAlertsEntity::class,
        TicketEntity::class,
        FacilityLocationEntity::class,
    ], version = 7
)
abstract class TripKitDatabase : RoomDatabase() {
    abstract fun carParkDao(): CarParkDao
    abstract fun carPodDao(): CarPodDao
    abstract fun bikePodDao(): BikePodDao
    abstract fun facilityDao(): FacilityDao
    abstract fun freeFloatingLocationDao(): FreeFloatingLocationDao
    abstract fun stopLocationDao(): StopLocationDao
    abstract fun scheduledServiceRealtimeInfoDao(): ScheduledServiceRealtimeInfoDao
    abstract fun parentStopDao(): ParentStopDao
    abstract fun onStreetParkingDao(): OnStreetParkingDao
    abstract fun serviceAlertsDao(): ServiceAlertsDao
    abstract fun ticketDao(): TicketDao

    companion object {
        fun getInstance(context: Context): TripKitDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TripKitDatabase::class.java, "tripkit.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}