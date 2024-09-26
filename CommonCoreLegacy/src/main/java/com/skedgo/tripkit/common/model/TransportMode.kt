package com.skedgo.tripkit.common.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.routing.ServiceColor

data class TransportMode(
    @SerializedName("id") var id: String = "",
    @SerializedName("URL") var url: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("icon") var iconId: String? = null,
    @SerializedName("darkIcon") var darkIcon: String? = null,
    @SerializedName("implies") var implies: ArrayList<String>? = null,
    @SerializedName("required") var isRequired: Boolean = false,
    @SerializedName("color") var color: ServiceColor? = null
) {

    override fun equals(other: Any?): Boolean {
        return if (other is TransportMode) {
            this.id == other.id
        } else {
            false
        }
    }

    override fun toString(): String {
        return id
    }

    companion object {
        const val MIDDLE_FIX_CAR: String = "car-s"
        const val MIDDLE_FIX_BIC: String = "bic-s"

        const val ID_WALK: String = "wa_wal"
        const val ID_TAXI: String = "ps_tax"
        const val ID_AIR: String = "in_air"
        const val ID_SHUFFLE: String = "ps_shu"
        const val ID_TNC: String = "ps_tnc"
        const val ID_BICYCLE: String = "cy_bic"
        const val ID_SCHOOL_BUS: String = "pt_ltd_SCHOOLBUS"
        const val ID_PUBLIC_TRANSPORT: String = "pt_pub"
        const val ID_MOTORBIKE: String = "me_mot"
        const val ID_CAR: String = "me_car"
        const val ID_WHEEL_CHAIR: String = "wa_whe"
        const val ID_PS_DRT: String = "ps_drt"

        /**
         * FIXME: It seems we no longer need this id.
         * Replacement seems to be 'pt_ltd_SCHOOLBUS'.
         */
        const val ID_SHUTTLE_BUS: String = "ps_shu"

        @JvmStatic
        fun fromId(id: String): TransportMode {
            return TransportMode(id = id)
        }

        @JvmStatic
        @DrawableRes
        fun getLocalIconResId(identifier: String?): Int {
            return when {
                ID_BICYCLE == identifier -> R.drawable.ic_bicycle
                ID_WALK == identifier -> R.drawable.ic_walk
                ID_PUBLIC_TRANSPORT == identifier -> R.drawable.ic_public_transport
                ID_TAXI == identifier -> R.drawable.ic_taxi
                ID_SHUTTLE_BUS == identifier || ID_SCHOOL_BUS == identifier -> R.drawable.ic_shuttlebus
                ID_MOTORBIKE == identifier -> R.drawable.ic_motorbike
                ID_CAR == identifier -> R.drawable.ic_car
                ID_AIR == identifier -> R.drawable.ic_aeroplane
                identifier != null && identifier.startsWith("cy_bic-s") -> R.drawable.ic_bicycle_share
                ID_WHEEL_CHAIR == identifier -> R.drawable.ic_wheelchair
                else -> 0

            }
        }
    }
}