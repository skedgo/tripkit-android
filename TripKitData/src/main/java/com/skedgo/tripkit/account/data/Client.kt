package com.skedgo.tripkit.account.data

import com.skedgo.tripkit.common.util.TripKitLatLng

data class Client(
    val clientID: String,
    var clientName: String,
    val polygon: Polygon? = null,
    val appColors: AppColors? = null,
    var isBeta: Boolean = false
)

data class AppColors(
    val barBackground: BarBackground,
    val barForeground: BarForeground,
    val tintColor: TintColor
)

data class BarBackground(
    val blue: Int,
    val green: Int,
    val red: Int
)

data class BarForeground(
    val blue: Int,
    val green: Int,
    val red: Int
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

data class TintColor(
    val blue: Int,
    val green: Int,
    val red: Int
)