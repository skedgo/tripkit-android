package com.skedgo.tripkit.locations

class CarPod constructor(
        val id: String,
        val address: String?,
        val lat: Double,
        val lng: Double,
        val name: String,
        val localIcon: String,
        val remoteIcon: String?,
        val deepLink: String?,
        val appLinkAndroid: String?,
        val bookingURL: String?,

        val realTimeInfo: RealTimeInfo?,
        val operator: Operator,
        val vehicles: List<Vehicle>?,
        val garage: Garage
)

class RealTimeInfo(
        val availableChargingSpaces: Int,
        val availableVehicles: Int,
        val totalSpaces: Int,
        val lastUpdate: Long,
        val inService: Boolean
)

class Operator(
        val name: String,
        var phone: String?,
        var website: String?,
        var appInfo: AppInfo?,
)

class AppInfo(
        val appURLAndroid: String?,
        val deepLink: String?
)

class Vehicle(
        val name: String,
        val fuelType: String?,
        val licensePlate: String?
)

class Garage(
        val address: String?
)