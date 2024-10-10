package com.skedgo.tripkit.routing

import android.content.Context
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable

object VehicleDrawables {
    fun createLightDrawable(context: Context, iconRes: Int): Drawable? {
        try {
            var drawable = context.getDrawable(iconRes)
            if (drawable != null) {
                drawable = drawable.mutate()
                drawable.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), -0x1)
            }

            return drawable
        } catch (e: Exception) {
            return null
        }
    }
}
