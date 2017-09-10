package skedgo.tripkit.routing

import android.content.res.Resources
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes

fun Resources.createLightDrawable(@DrawableRes iconRes: Int): Drawable {
  var drawable: Drawable = this.getDrawable(iconRes)
  drawable = drawable.mutate()
  drawable.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), 0xffffffff.toInt())
  return drawable
}