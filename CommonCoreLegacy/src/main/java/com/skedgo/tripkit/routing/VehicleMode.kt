package com.skedgo.tripkit.routing

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.skedgo.tripkit.common.R

/**
 * As of v11, this denotes local transport icons.
 *
 * @see {@link ModeInfo}
 */
enum class VehicleMode(
    private val key: String,
    @JvmField @get:DrawableRes @param:DrawableRes val iconRes: Int,
    @DrawableRes realtimeIconRes: Int
) {
    AEROPLANE("aeroplane", R.drawable.ic_aeroplane, R.drawable.ic_aeroplane),
    BICYCLE_SHARE("bicycle-share", R.drawable.ic_bicycle_share, R.drawable.ic_bicycle_share),
    BICYCLE_ELECTRIC_SHARE(
        "bicycle-electric-share",
        R.drawable.ic_bicycle_share,
        R.drawable.ic_bicycle_share
    ),
    BICYCLE("bicycle", R.drawable.ic_bicycle, R.drawable.ic_bicycle),
    BUS("bus", R.drawable.ic_bus, R.drawable.ic_bus_realtime),
    CABLECAR("cablecar", R.drawable.ic_cablecar, R.drawable.ic_cablecar_realtime),
    CAR_POOL("car-pool", R.drawable.ic_car_pool, R.drawable.ic_car_pool),
    CAR_RIDE_SHARE("car-ride-share", R.drawable.ic_car_ride_share, R.drawable.ic_car_ride_share),
    CAR_SHARE("car-share", R.drawable.ic_car_share, R.drawable.ic_car_share),
    CAR("car", R.drawable.ic_car, R.drawable.ic_car),
    COACH("coach", R.drawable.ic_coach, R.drawable.ic_coach),
    FERRY("ferry", R.drawable.ic_ferry, R.drawable.ic_ferry_realtime),
    KICK_SCOOTER(
        "kickscooter-share",
        R.drawable.ic_kickscooter_sharing,
        R.drawable.ic_kickscooter_sharing
    ),
    MONORAIL("monorail", R.drawable.ic_monorail, R.drawable.ic_monorail_realtime),
    MOTORBIKE("motorbike", R.drawable.ic_motorbike, R.drawable.ic_motorbike),
    MOTO_SCOOTER("moto_scooter", R.drawable.ic_motorbike, R.drawable.ic_motorbike),

    PARKING("parking", R.drawable.ic_parking, R.drawable.ic_parking),
    PEDELEC("pedelec", R.drawable.ic_bicycle, R.drawable.ic_bicycle),

    PUBLIC_TRANSPORT(
        "public-transport",
        R.drawable.ic_public_transport,
        R.drawable.ic_public_transport
    ),
    SHUTTLE_BUS("shuttlebus", R.drawable.ic_shuttlebus, R.drawable.ic_shuttlebus),
    SUBWAY("subway", R.drawable.ic_subway, R.drawable.ic_subway_realtime),
    TAXI("taxi", R.drawable.ic_taxi, R.drawable.ic_taxi),
    TRAIN_INTERCITY(
        "train-intercity",
        R.drawable.ic_train_intercity,
        R.drawable.ic_train_intercity
    ),
    TRAIN("train", R.drawable.ic_train, R.drawable.ic_train_realtime),
    TRAM("tram", R.drawable.ic_tram, R.drawable.ic_tram_realtime),
    WALK("walk", R.drawable.ic_walk, R.drawable.ic_walk),
    WHEEL_CHAIR("wheelchair", R.drawable.ic_wheelchair, R.drawable.ic_wheelchair),
    FUNICULAR("funicular", R.drawable.ic_funicular, R.drawable.ic_funicular),

    // FIXME: Is this still being used?
    TOLL("toll", R.drawable.ic_toll, R.drawable.ic_toll);

    @get:DrawableRes
    var realTimeIconRes: Int = -1

    init {
        this.realTimeIconRes = realtimeIconRes
    }

    val isPublicTransport: Boolean
        get() {
            val publicTransportModes = publicTransportModes
            for (vehicleMode in publicTransportModes) {
                if (this == vehicleMode) {
                    return true
                }
            }

            return false
        }

    /**
     * Replacement: [ModeInfo.getAlternativeText]
     */
    @Deprecated("")
    override fun toString(): String {
        return key
    }

    fun getMapIconRes(context: Context): Drawable? {
        return context.getDrawable(iconRes)
    }

    fun getRealtimeMapIconRes(context: Context): Drawable? {
        return context.getDrawable(realTimeIconRes)
    }

    companion object {
        @JvmStatic
        fun from(key: String?): VehicleMode? {
            if (key.isNullOrBlank()) {
                return null
            }

            val lowerCase = key.lowercase()
            for (value in values()) {
                if (TextUtils.equals(value.key.lowercase(), lowerCase)) {
                    return value
                }
            }

            return null
        }

        /**
         * Instead of typing: (obj.getEnumVar() != null) ? obj.getEnumVar().toString() : null,
         * now we can simply use: EnumVar.toString(obj.getEnumVar())
         */
        @Deprecated("")
        fun toString(vehicleMode: VehicleMode?): String? {
            return if ((vehicleMode != null)) vehicleMode.toString() else null
        }

        val publicTransportModes: Array<VehicleMode>
            get() = arrayOf(FERRY, TRAIN, MONORAIL, SUBWAY, BUS, TRAM, CABLECAR, FUNICULAR)
    }
}