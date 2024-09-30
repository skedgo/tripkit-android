package com.skedgo.tripkit.common.model.realtimealert;

import android.text.TextUtils;

import java.util.Locale;

import androidx.annotation.Nullable;

public enum RealTimeStatus {
    CAPABLE, IS_REAL_TIME, INCAPABLE;

    @Nullable
    public static RealTimeStatus from(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }

        final String lowerCase = s.toLowerCase(Locale.US);
        for (RealTimeStatus value : values()) {
            if (TextUtils.equals(lowerCase, value.name().toLowerCase(Locale.US))) {
                return value;
            }
        }

        return null;
    }
}