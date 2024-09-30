package com.skedgo.tripkit.common.model.realtimealert

import android.text.TextUtils

enum class RealTimeStatus {
    CAPABLE, IS_REAL_TIME, INCAPABLE;

    companion object {
        @JvmStatic
        fun from(s: String?): RealTimeStatus? {
            if (s == null || TextUtils.isEmpty(s)) {
                return null
            }

            val lowerCase = s.lowercase()
            for (value in values()) {
                if (TextUtils.equals(lowerCase, value.name.lowercase())) {
                    return value
                }
            }

            return null
        }
    }
}