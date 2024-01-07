package com.skedgo.tripkit.data.database.locations.carpods

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(tableName = "carPods")
class CarPodEntity {
    @PrimaryKey
    var id: String = ""
    var address: String? = null
    lateinit var cellId: String
    var lat: Double = 0.0
    var lng: Double = 0.0
    lateinit var name: String
    lateinit var localIcon: String
    var remoteIcon: String? = null
    lateinit var operatorName: String
    var operatorPhone: String? = null
    var operatorWebsite: String? = null
    var garageAddress: String? = null

    var availableChargingSpaces: Int? = null
    var availableVehicles: Int? = null
    var totalSpaces: Int? = null
    var lastUpdate: Long? = null
    var inService: Boolean? = null
    var deepLink: String? = null
    var appURLAndroid: String? = null
    var bookingURL: String? = null

    var red: Int = 0
    var green: Int = 0
    var blue: Int = 0
}

@Entity(foreignKeys = [ForeignKey(entity = CarPodEntity::class,
        parentColumns = ["id"],
        childColumns = ["carPodId"],
        onDelete = CASCADE)], tableName = "carPodVehicles")
class CarPodVehicle {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(index = true)
    lateinit var carPodId: String
    lateinit var name: String
    var fuelType: String? = null
    var licensePlate: String? = null
}