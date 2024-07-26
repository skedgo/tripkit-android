package com.skedgo.tripkit.booking.ui;

import android.view.View;

import androidx.databinding.BindingConversion;

public final class Converters {
    private Converters() {
    }

    @BindingConversion
    public static int convertBooleanToViewVisibility(boolean value) {
        return value ? View.VISIBLE : View.GONE;
    }
}
