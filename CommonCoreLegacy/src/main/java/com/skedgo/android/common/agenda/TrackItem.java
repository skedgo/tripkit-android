package com.skedgo.android.common.agenda;

import com.google.gson.annotations.SerializedName;

public class TrackItem {
  @SerializedName("class") private String klass;
  @SerializedName("id") private String id;

  public String getId() {
    return id;
  }
}