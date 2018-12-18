package com.skedgo.android.tripkit.booking;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public abstract class FormField {

  public static final int ADDRESS = 5;
  public static final int BOOKINGFORM = 7;
  public static final int DATETIME = 3;
  public static final int LINK = 6;
  public static final int OPTION = 2;
  public static final int STEPPER = 4;
  public static final int STRING = 1;
  public static final int SWITCH = 8;
  public static final int PASSWORD = 9;
  public static final int EXTERNAL = 10;

  public static final String ACCESS_TOKEN = "access_token";
  public static final String EXPIRES_IN = "expires_in";
  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String REDIRECT_URI = "redirectUri";
  public static final String CLIENT_ID = "clientID";
  public static final String CLIENT_SECRET = "clientSecret";
  public static final String AUTH_URL = "authURL";
  public static final String TOKEN_URL = "tokenURL";
  public static final String SCOPE = "scope";

  @SerializedName("id")
  private String id;
  @SerializedName("title")
  private String title;
  @SerializedName("subtitle")
  private String subtitle;
  @SerializedName("sidetitle")
  private String sidetitle;
  @SerializedName("required")
  private boolean required;
  @SerializedName("readOnly")
  private boolean readOnly;
  @SerializedName("hidden")
  private boolean hidden;
  @SerializedName("type")
  private String type;

  @Nullable
  public abstract Object getValue();

  @Nullable
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Nullable
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Nullable
  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  @Nullable
  public String getSidetitle() {
    return sidetitle;
  }

  public void setSidetitle(String sidetitle) {
    this.sidetitle = sidetitle;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
}
