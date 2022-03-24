package com.skedgo.tripkit.routing;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

public final class VehicleDrawables {
  private VehicleDrawables() {}

  public static Drawable createLightDrawable(@NonNull Resources resources, int iconRes) {
    Drawable drawable = resources.getDrawable(iconRes);
    if (drawable != null) {
      drawable = drawable.mutate();
      drawable.setColorFilter(new LightingColorFilter(Color.parseColor("#FF000000"), 0xffffffff));
    }

    return drawable;
  }
}
