package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class LinkFormField extends FormField {
  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";
  public static final String METHOD_REFRESH = "refresh";
  public static final String METHOD_EXTERNAL = "external";

  public static final Creator<LinkFormField> CREATOR = new Creator<LinkFormField>() {
    @Override
    public LinkFormField createFromParcel(Parcel in) {
      in.readInt();
      return new LinkFormField(in);
    }

    @Override
    public LinkFormField[] newArray(int size) {
      return new LinkFormField[size];
    }
  };
  @SerializedName("value")
  private String link;
  @SerializedName("method")
  private String method;

  public LinkFormField() {
    super();
  }

  public LinkFormField(Parcel in) {
    super(in);
    this.link = in.readString();
    this.method = in.readString();
  }

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

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(LINK);
    super.writeToParcel(dest, flags);
    dest.writeString(link);
    dest.writeString(method);
  }
}