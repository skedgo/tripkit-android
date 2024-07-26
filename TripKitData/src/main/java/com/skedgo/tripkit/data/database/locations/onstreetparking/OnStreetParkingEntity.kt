package com.skedgo.tripkit.data.database.locations.onstreetparking

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.skedgo.tripkit.data.database.locations.carparks.ParkingOperatorEntity
import com.skedgo.tripkit.parkingspots.models.Parking

@Entity(tableName = "onStreetParkings")
@TypeConverters(VacancyConverters::class)
class OnStreetParkingEntity {
    @PrimaryKey
    var identifier: String = ""
    lateinit var cellId: String
    lateinit var name: String
    lateinit var encodedPolyline: String

    var lat: Double = 0.0
    var lng: Double = 0.0
    var address: String? = null
    lateinit var info: String
    var parkingVacancy: Parking.Vacancy? = Parking.Vacancy.UNKNOWN
    lateinit var availableContent: String
    var modeIdentifier: String? = null
    lateinit var localIconName: String
    var remoteIconName: String? = null

    @Embedded
    lateinit var operator: ParkingOperatorEntity
}

object VacancyConverters {
    @TypeConverter
    @JvmStatic
    fun vacancyToString(vacancy: Parking.Vacancy?) = vacancy?.name

    @TypeConverter
    @JvmStatic
    fun stringToVacancy(s: String?) = s?.let(Parking.Vacancy::valueOf)
}
