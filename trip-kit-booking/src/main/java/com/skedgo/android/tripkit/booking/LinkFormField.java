package com.skedgo.android.tripkit.booking;

import com.google.gson.annotations.SerializedName;

public class LinkFormField extends FormField {
  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";
  public static final String METHOD_REFRESH = "refresh";
  public static final String METHOD_EXTERNAL = "external";
  @SerializedName("value")
  private String link;
  @SerializedName("method")
  private String method;

  @Override
  public String getValue() {
    return link;
  }

  public void setValue(String link) {
    this.link = link;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}