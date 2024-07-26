package com.skedgo.tripkit.data.database.locations.carpods

import javax.inject.Inject

open class CarPodMapper @Inject constructor() {
    fun toEntity(
        key: String,
        carPods: List<CarPodLocation>
    ): List<Pair<CarPodEntity, List<CarPodVehicle>?>> {
        return carPods
            .map {
                val entity = CarPodEntity()
                entity.address = it.address
                entity.id = it.id
                entity.lat = it.lat
                entity.lng = it.lng
                entity.localIcon = it.modeInfo.localIconName
                entity.remoteIcon = it.modeInfo.remoteIconName
                entity.name = it.name
                entity.operatorName = it.carPod.operator.name
                entity.operatorPhone = it.carPod.operator.phone
                entity.operatorWebsite = it.carPod.operator.website
                entity.cellId = key
                entity.availableChargingSpaces = it.carPod.availableChargingSpaces
                entity.availableVehicles = it.carPod.availableVehicles
                entity.inService = it.carPod.inService
                entity.totalSpaces = it.carPod.totalSpaces
                entity.lastUpdate = it.carPod.lastUpdate
                entity.deepLink = it.carPod.deepLink
                entity.appURLAndroid = it.carPod.operator.appInfo?.appURLAndroid
                entity.garageAddress = it.carPod.garage?.address
                entity.red = it.modeInfo.color?.red ?: 0
                entity.blue = it.modeInfo.color?.blue ?: 0
                entity.green = it.modeInfo.color?.green ?: 0
                entity to it.carPod.vehicles?.map {
                    val carPodVehicle = CarPodVehicle()
                    carPodVehicle.carPodId = entity.id
                    carPodVehicle.fuelType = it.fuelType
                    carPodVehicle.licensePlate = it.licensePlate
                    carPodVehicle.name = it.name
                    carPodVehicle
                }
            }
    }
}