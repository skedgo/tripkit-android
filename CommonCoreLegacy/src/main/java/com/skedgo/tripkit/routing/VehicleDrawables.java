package com.skedgo.tripkit.routing;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class VehicleDrawables {
    private VehicleDrawables() {
    }

    public static @Nullable Drawable createLightDrawable(@NonNull Resources resources, int iconRes) {
        try {
            Drawable drawable = resources.getDrawable(iconRes);
            if (drawable != null) {
                drawable = drawable.mutate();
                drawable.setColorFilter(new LightingColorFilter(Color.parseColor("#FF000000"), 0xffffffff));
            }

            return drawable;
        } catch (Exception e) {
            return null;
        }
    }
}
