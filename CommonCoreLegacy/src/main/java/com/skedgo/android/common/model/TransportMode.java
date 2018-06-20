package com.skedgo.android.common.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.R;

import java.util.ArrayList;

import skedgo.tripkit.routing.ServiceColor;

@Keep
public class TransportMode {
  public static final String MIDDLE_FIX_CAR = "car-s";
  public static final String MIDDLE_FIX_BIC = "bic-s";

  public static final String ID_WALK = "wa_wal";
  public static final String ID_TAXI = "ps_tax";
  public static final String ID_AIR = "in_air";
  public static final String ID_SHUFFLE = "ps_shu";
  public static final String ID_TNC = "ps_tnc";
  public static final String ID_BICYCLE = "cy_bic";
  public static final String ID_SCHOOL_BUS = "pt_sch";
  public static final String ID_PUBLIC_TRANSPORT = "pt_pub";
  public static final String ID_MOTORBIKE = "me_mot";
  public static final String ID_CAR = "me_car";
  public static final String ID_WHEEL_CHAIR = "wa_whe";

  /**
   * FIXME: It seems we no longer need this id.
   * Replacement seems to be 'pt_ltd_SCHOOLBUS'.
   */
  public static final String ID_SHUTTLE_BUS = "ps_shu";

  @SerializedName("id") private String id;
  @SerializedName("URL") private String url;
  @SerializedName("title") private String title;
  @SerializedName("icon") private String iconId;
  @SerializedName("darkIcon") private String darkIcon;
  @SerializedName("implies") private ArrayList<String> implies;
  @SerializedName("required") private boolean isRequired;
  @SerializedName("color") private ServiceColor color;

  public TransportMode() {}

  public static TransportMode fromId(@NonNull String id) {
    final TransportMode mode = new TransportMode();
    mode.setId(id);
    return mode;
  }

  @DrawableRes
  public static int getLocalIconResId(@Nullable String identifier) {
    if (ID_BICYCLE.equals(identifier)) {
      return R.drawable.ic_bicycle;
    } else if (ID_WALK.equals(identifier)) {
      return R.drawable.ic_walk;
    } else if (ID_PUBLIC_TRANSPORT.equals(identifier)) {
      return R.drawable.ic_public_transport;
    } else if (ID_TAXI.equals(identifier)) {
      return R.drawable.ic_taxi;
    } else if (ID_SHUTTLE_BUS.equals(identifier)) {
      return R.drawable.ic_shuttlebus;
    } else if (ID_MOTORBIKE.equals(identifier)) {
      return R.drawable.ic_motorbike;
    } else if (ID_CAR.equals(identifier)) {
      return R.drawable.ic_car;
    } else if (ID_AIR.equals(identifier)) {
      return R.drawable.ic_aeroplane;
    } else if (identifier != null && identifier.startsWith("cy_bic-s")) {
      return R.drawable.ic_bicycle_share;
    } else if (ID_WHEEL_CHAIR.equals(identifier)) {
      return R.drawable.ic_wheelchair;
    } else {
      return 0;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof TransportMode) {
      final TransportMode that = (TransportMode) o;
      return TextUtils.equals(id, that.id);
    } else {
      return false;
    }
  }

  @Nullable
  public String getURL() {
    return url;
  }

  public void setURL(String URL) {
    url = URL;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Nullable
  public ArrayList<String> getImplies() {
    return implies;
  }

  public void setImplies(ArrayList<String> implies) {
    this.implies = implies;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public void setRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }

  public String getIconId() {
    return iconId;
  }

  public void setIconId(String iconId) {
    this.iconId = iconId;
  }

  @Nullable public ServiceColor getColor() {
    return color;
  }

  void setColor(ServiceColor color) {
    this.color = color;
  }

  @Nullable public String getDarkIcon() {
    return darkIcon;
  }

  public void setDarkIcon(String darkIcon) {
    this.darkIcon = darkIcon;
  }
}