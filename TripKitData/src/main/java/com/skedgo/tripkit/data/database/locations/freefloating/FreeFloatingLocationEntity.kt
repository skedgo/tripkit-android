package com.skedgo.tripkit.data.database.locations.freefloating

import androidx.annotation.Keep
import androidx.room.*
import com.skedgo.tripkit.data.database.locations.bikepods.ModeInfoEntity
import com.skedgo.tripkit.data.database.locations.onstreetparking.VacancyConverters
import com.skedgo.tripkit.parkingspots.models.Parking

@Entity(tableName = "freeFloatingLocations")
@Keep
class FreeFloatingLocationEntity {
    @PrimaryKey
    var identifier: String = ""
    var cellId: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    var name: String? = null
    var address: String? = null
    @Embedded(prefix="freefloating_vehicle_")
    lateinit var vehicle: FreeFloatingVehicleEntity

    @Embedded(prefix = "freefloating_modeinfo_")
    var modeInfo: ModeInfoEntity? = null
}

@Entity
@Keep
class FreeFloatingVehicleEntity {
    @Ignore
    lateinit var identifier: String
    var available: Boolean = false
    var batteryLevel: Int = 0
    var lastUpdate: Int = 0
    var qrCode: String? = null
    var name: String = ""
    var vehicleType: String = ""
    @Embedded(prefix = "freefloating_vehicletypeinfo_")
    var vehicleTypeInfo: FreeFloatingVehicleTypeInfoEntity? = null

    @Embedded(prefix = "freefloating_operator_")
    lateinit var operator: FreeFloatingOperatorEntity
}

@Entity
@Keep
class FreeFloatingOperatorEntity {
    lateinit var name: String
    var website: String? = null
    var phone: String? = null

    @Embedded(prefix="freefloating_appinfo_")
    var appInfo: FreeFloatingAppInfoEntity? = null
}

@Entity
@Keep
class FreeFloatingAppInfoEntity {
    var name: String? = null
    var appURLAndroid: String? = null
}

@Entity
@Keep
class FreeFloatingVehicleTypeInfoEntity {
    var formFactor: String = ""
    var maxRangeMeters: Long = 0
    var propulsionType: String = ""
}