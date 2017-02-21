package com.skedgo.android.tripkit.booking.ui.view;

import android.databinding.BindingConversion;
import android.view.View;

public final class Converters {
  private Converters() {}

  @BindingConversion
  public static int convertBooleanToViewVisibility(boolean value) {
    return value ? View.VISIBLE : View.GONE;
  }
}
