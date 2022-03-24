package com.skedgo.tripkit.routing;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class ServiceColor implements Parcelable {
  public static final Creator<ServiceColor> CREATOR = new Creator<ServiceColor>() {
    public ServiceColor createFromParcel(Parcel in) {
      return new ServiceColor(in);
    }

    public ServiceColor[] newArray(int size) {
      return new ServiceColor[size];
    }
  };

  @SerializedName("red") private int red;
  @SerializedName("blue") private int blue;
  @SerializedName("green") private int green;

  public ServiceColor() {
    this(0, 0, 0);
  }

  public ServiceColor(int color) {
    this(Color.red(color), Color.green(color), Color.blue(color));
  }

  public ServiceColor(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  private ServiceColor(Parcel parcel) {
    red = parcel.readInt();
    blue = parcel.readInt();
    green = parcel.readInt();
  }

  public int getRed() {
    return red;
  }

  public void setRed(final int red) {
    this.red = red;
  }

  public int getBlue() {
    return blue;
  }

  public void setBlue(final int blue) {
    this.blue = blue;
  }

  public int getGreen() {
    return green;
  }

  public void setGreen(final int green) {
    this.green = green;
  }

  public int getColor() {
    return Color.rgb(red, green, blue);
  }

  @Override
  public int describeContents() {
    return hashCode();
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeInt(red);
    dest.writeInt(blue);
    dest.writeInt(green);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ServiceColor) {
      final ServiceColor that = (ServiceColor) o;
      return red == that.red && blue == that.blue && green == that.green;
    } else {
      return false;
    }
  }
}