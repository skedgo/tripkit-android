package com.skedgo.tripkit.common.model.stop

import android.text.TextUtils

enum class StopType(private val key: String) {
    BUS("bus"),
    TRAIN("train"),
    FERRY("ferry"),
    MONORAIL("monorail"),
    SUBWAY("subway"),
    TAXI("taxi"),
    PARKING("parking"),
    TRAM("tram"),
    CABLECAR("cablecar");

    override fun toString(): String {
        return key
    }

    companion object {
        @JvmStatic
        fun from(key: String): StopType? {
            if (TextUtils.isEmpty(key)) {
                return null
            }

            val lowerCase = key.lowercase()
            for (value in values()) {
                if (TextUtils.equals(value.key, lowerCase)) {
                    return value
                }
            }

            return null
        }
    }
}