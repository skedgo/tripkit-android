package com.buzzhives.android.tripplanner.util;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

public final class Snackbars {
  private Snackbars() {}

  public static void showShortly(@NonNull Activity activity, @Nullable String message) {
    final View v = activity.findViewById(android.R.id.content);
    Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
  }
}