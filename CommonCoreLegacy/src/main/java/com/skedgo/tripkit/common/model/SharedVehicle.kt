package com.skedgo.tripkit.common.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.locations.Garage
import com.skedgo.tripkit.locations.Operator
import org.immutables.gson.Gson
import org.immutables.value.Value

enum class SharedVehicleType(@DrawableRes val iconId: Int) {
    BIKE(R.drawable.ic_bicycle_share),
    BICYCLE(R.drawable.ic_bicycle_share),
    CAR(R.drawable.ic_car),
    KICK_SCOOTER(R.drawable.ic_kickscooter_sharing),
    SCOOTER(R.drawable.ic_kickscooter_sharing),
    MOTO_SCOOTER(R.drawable.ic_motorbike),
    PEDELEC(R.drawable.ic_bicycle_share);
}

fun getSharedVehicleType(type: String): SharedVehicleType? {
    var vehicleType: SharedVehicleType? = null
    SharedVehicleType.values().forEach {
        if (it.name.equals(type, true)) {
            vehicleType = it
        }
    }

    return vehicleType
}

class VehicleTypeInfo(
        var formFactor: String = "",
        var maxRangeMeters: Long = 0,
        var propulsionType: String = ""
)

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersSharedVehicle::class)
abstract class SharedVehicle {
    abstract fun identifier(): String?
    abstract fun name(): String?
    abstract fun available(): Boolean?
    abstract fun batteryLevel(): Int?
    abstract fun batteryRange(): Int?
    abstract fun lastUpdate(): Long?
    abstract fun operator(): Operator?
    abstract fun type(): SharedVehicleType?
    abstract fun vehicleType(): SharedVehicleType?
    abstract fun appURLAndroid(): String?
    abstract fun deepLink(): String?
    abstract fun bookingURL(): String?
    abstract fun garage(): Garage?
    abstract fun vehicleTypeInfo(): VehicleTypeInfo?
}
