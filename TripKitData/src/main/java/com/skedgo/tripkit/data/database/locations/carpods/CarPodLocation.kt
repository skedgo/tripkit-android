package com.skedgo.tripkit.data.database.locations.carpods

import androidx.annotation.Keep
import com.skedgo.tripkit.routing.ModeInfo

@Keep
class CarPodLocation {
    lateinit var id: String
    var address: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    lateinit var name: String
    lateinit var modeInfo: ModeInfo
    lateinit var carPod: CarPod

    @Keep
    class CarPod {
        var availableChargingSpaces: Int? = null
        var availableVehicles: Int? = null
        var totalSpaces: Int? = null
        var inService: Boolean? = null
        var lastUpdate: Long? = null
        var deepLink: String? = null
        lateinit var operator: Operator
        var garage: Garage? = null

        var vehicles: List<Vehicle>? = null
    }

    @Keep
    class Operator {
        lateinit var name: String
        var website: String? = null
        var phone: String? = null
        var appInfo: AppInfoEntity? = null
    }

    @Keep
    class AppInfoEntity {
        var appURLAndroid: String? = null
    }

    @Keep
    class Vehicle {
        lateinit var name: String
        var fuelType: String? = null
        var licensePlate: String? = null
    }

    @Keep
    class Garage(
            val address: String?
    )
}

