package com.skedgo.android.common.util;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;

import skedgo.tripkit.configuration.Server;
import skedgo.tripkit.routing.ModeInfo;
import com.skedgo.android.common.model.TransportMode;

public final class TransportModeUtils {
  public static final String ICON_URL_TEMPLATE = Server.ApiTripGo.getValue() + "modeicons/android/%s/ic_transport_%s.png";

  private TransportModeUtils() {}

  @Nullable
  public static String getIconUrlForId(
      @NonNull Resources resources,
      @Nullable String iconId) {
    if (iconId == null || iconId.length() == 0) {
      return null;
    }

    final String densityDpiName = getDensityDpiName(resources.getDisplayMetrics().densityDpi);
    return String.format(TransportModeUtils.ICON_URL_TEMPLATE, densityDpiName, iconId);
  }

  @Nullable
  public static String getIconUrlForModeInfo(
      @NonNull Resources resources,
      @Nullable ModeInfo modeInfo) {
    if (modeInfo == null) {
      return null;
    }

    return getIconUrlForId(resources, modeInfo.getRemoteIconName());
  }

  @Nullable
  public static String getDarkIconUrlForModeInfo(
      @NonNull Resources resources,
      @Nullable ModeInfo modeInfo) {
    if (modeInfo == null) {
      return null;
    }

    return getIconUrlForId(resources, modeInfo.getRemoteDarkIconName());
  }

  @Nullable
  public static String getIconUrlForTransportMode(
      @NonNull Resources resources,
      @Nullable TransportMode mode) {
    if (mode == null) {
      return null;
    }

    return getIconUrlForId(resources, mode.getIconId());
  }

  @Nullable
  public static String getDarkIconUrlForTransportMode(
      @NonNull Resources resources,
      @Nullable TransportMode mode) {
    if (mode == null) {
      return null;
    }

    return getIconUrlForId(resources, mode.getDarkIcon());
  }

  @NonNull
  public static String getDensityDpiName(int densityDpi) {
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhdpi";
      case DisplayMetrics.DENSITY_XXHIGH:
      default:
        return "xxhdpi";
    }
  }
}