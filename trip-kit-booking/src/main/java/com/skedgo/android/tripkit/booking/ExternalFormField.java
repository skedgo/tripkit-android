package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExternalFormField extends FormField {
  @SerializedName("disregardURL")
  private String disregardURL;
  @SerializedName("nextHudText")
  private String nextHudText;
  @SerializedName("nextURL")
  private String nextURL;
  @SerializedName("value")
  private String value;

  public ExternalFormField() {
    super();
  }

  @Override
  public String getValue() {
    return value;
  }

  public String getDisregardURL() {
    return disregardURL;
  }

  public String getNextHudText() {
    return nextHudText;
  }

  public String getNextURL() {
    return nextURL;
  }

}
