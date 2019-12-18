package com.skedgo.tripkit.routing;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.skedgo.tripkit.common.R;

import java.util.Locale;

/**
 * As of v11, this denotes local transport icons.
 *
 * @see {@link ModeInfo}
 */
public enum VehicleMode {
  AEROPLANE("aeroplane", R.drawable.ic_aeroplane, R.drawable.ic_aeroplane),
  BICYCLE_SHARE("bicycle-share", R.drawable.ic_bicycle_share, R.drawable.ic_bicycle_share),
  BICYCLE("bicycle", R.drawable.ic_bicycle, R.drawable.ic_bicycle),
  BUS("bus", R.drawable.ic_bus, R.drawable.ic_bus_realtime),
  CABLECAR("cablecar", R.drawable.ic_cablecar, R.drawable.ic_cablecar_realtime),
  CAR_POOL("car-pool", R.drawable.ic_car_pool, R.drawable.ic_car_pool),
  CAR_RIDE_SHARE("car-ride-share", R.drawable.ic_car_ride_share, R.drawable.ic_car_ride_share),
  CAR_SHARE("car-share", R.drawable.ic_car_share, R.drawable.ic_car_share),
  CAR("car", R.drawable.ic_car, R.drawable.ic_car),
  COACH("coach", R.drawable.ic_coach, R.drawable.ic_coach),
  FERRY("ferry", R.drawable.ic_ferry, R.drawable.ic_ferry_realtime),
  MONORAIL("monorail", R.drawable.ic_monorail, R.drawable.ic_monorail_realtime),
  MOTORBIKE("motorbike", R.drawable.ic_motorbike, R.drawable.ic_motorbike),
  PARKING("parking", R.drawable.ic_parking, R.drawable.ic_parking),
  PUBLIC_TRANSPORT(
      "public-transport",
      R.drawable.ic_public_transport,
      R.drawable.ic_public_transport
  ),
  SHUTTLE_BUS("shuttlebus", R.drawable.ic_shuttlebus, R.drawable.ic_shuttlebus),
  SUBWAY("subway", R.drawable.ic_subway, R.drawable.ic_subway_realtime),
  TAXI("taxi", R.drawable.ic_taxi, R.drawable.ic_taxi),
  TRAIN_INTERCITY(
      "train-intercity",
      R.drawable.ic_train_intercity,
      R.drawable.ic_train_intercity
  ),
  TRAIN("train", R.drawable.ic_train, R.drawable.ic_train_realtime),
  TRAM("tram", R.drawable.ic_tram, R.drawable.ic_tram_realtime),
  WALK("walk", R.drawable.ic_walk, R.drawable.ic_walk),
  WHEEL_CHAIR("wheelchair", R.drawable.ic_wheelchair, R.drawable.ic_wheelchair),
  FUNICULAR("funicular", R.drawable.ic_icon_mode_funicular, R.drawable.ic_icon_mode_funicular),

  // FIXME: Is this still being used?
  TOLL("toll", R.drawable.ic_toll, R.drawable.ic_toll);

  private String key;
  private int iconRes;
  private int realtimeIconRes = -1;

  VehicleMode(String key, @DrawableRes int iconRes, @DrawableRes int realtimeIconRes) {
    this.key = key;
    this.iconRes = iconRes;
    this.realtimeIconRes = realtimeIconRes;
  }

  @Nullable
  public static VehicleMode from(String key) {
    if (TextUtils.isEmpty(key)) {
      return null;
    }

    final String lowerCase = key.toLowerCase(Locale.US);
    for (VehicleMode value : values()) {
      if (TextUtils.equals(value.key.toLowerCase(Locale.US), lowerCase)) {
        return value;
      }
    }

    return null;
  }

  /**
   * Instead of typing: (obj.getEnumVar() != null) ? obj.getEnumVar().toString() : null,
   * now we can simply use: EnumVar.toString(obj.getEnumVar())
   */
  @Deprecated
  public static String toString(VehicleMode vehicleMode) {
    return (vehicleMode != null) ? vehicleMode.toString() : null;
  }

  public static VehicleMode[] getPublicTransportModes() {
    return new VehicleMode[] {FERRY, TRAIN, MONORAIL, SUBWAY, BUS, TRAM, CABLECAR, FUNICULAR};
  }

  public boolean isPublicTransport() {
    VehicleMode[] publicTransportModes = getPublicTransportModes();
    for (VehicleMode vehicleMode : publicTransportModes) {
      if (this == vehicleMode) {
        return true;
      }
    }

    return false;
  }

  /**
   * Replacement: {@link ModeInfo#getAlternativeText()}
   */
  @Deprecated
  @Override
  public String toString() {
    return key;
  }

  @DrawableRes
  public int getIconRes() {
    return iconRes;
  }

  @DrawableRes
  public int getRealTimeIconRes() {
    return realtimeIconRes;
  }

  public Drawable getMapIconRes(@NonNull Resources resources) {
    return resources.getDrawable(iconRes);
  }

  public Drawable getRealtimeMapIconRes(@NonNull Resources resources) {
    return resources.getDrawable(realtimeIconRes);
  }
}