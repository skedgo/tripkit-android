package com.skedgo.tripkit.account.data

import com.skedgo.tripkit.common.util.TripKitLatLng

data class Client(
    val clientID: String,
    var clientName: String,
    val polygon: Polygon? = null,
    val appColors: AppColors? = null,
    var isBeta: Boolean = false,
    val features: List<String>? = emptyList()
) {
    fun hasWalletFeature(): Boolean = features?.any { it == ClientFeature.WALLET.feature } ?: false
}

data class AppColors(
    val barBackground: AppColor,
    val barForeground: AppColor,
    val tintColor: AppColor
)

data class Polygon(
    val coordinates: List<List<List<List<Double>>>>,
    val type: String
) {
    fun coordinatesToTripKitLatLng(): List<TripKitLatLng> {
        val result = mutableListOf<TripKitLatLng>()
        coordinates.flatten().flatten().forEach {
            result.add(TripKitLatLng(it.last(), it.first()))
        }
        return result
    }
}

data class AppColor(
    val blue: Int,
    val green: Int,
    val red: Int,
    val alpha: Int = 1
)

enum class ClientFeature(val feature: String) {
    WALLET("WALLET")
}