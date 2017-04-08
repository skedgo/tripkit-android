package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * @see <a href="https://github.com/skedgo/tripkit-ios/wiki/Mode%20Identifiers">Mode Identifiers</a>
 */
public class ModeInfo implements Parcelable {
  public static final Creator<ModeInfo> CREATOR = new Creator<ModeInfo>() {
    @Override
    public ModeInfo createFromParcel(Parcel source) {
      return new ModeInfo(source);
    }

    @Override
    public ModeInfo[] newArray(int size) {
      return new ModeInfo[0];
    }
  };

  public static final float MAP_LIST_SIZE_RATIO = 1f;

  @SerializedName("alt") private String alternativeText;
  @SerializedName("localIcon") private String localIconName;
  @SerializedName("remoteIcon") private String remoteIconName;
  @SerializedName("remoteDarkIcon") private String remoteDarkIconName;
  @SerializedName("description") private String description;
  @SerializedName("identifier") private String id;
  @SerializedName("color") private ServiceColor color;

  public ModeInfo() {}

  private ModeInfo(@NonNull Parcel source) {
    alternativeText = source.readString();
    localIconName = source.readString();
    remoteIconName = source.readString();
    remoteDarkIconName = source.readString();
    description = source.readString();
    id = source.readString();
    color = source.readParcelable(ServiceColor.class.getClassLoader());
  }

  /**
   * Indicates a human-readable name of the transport (e.g, "Train").
   */
  @Nullable
  public String getAlternativeText() {
    return alternativeText;
  }

  public void setAlternativeText(String alternativeText) {
    this.alternativeText = alternativeText;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(alternativeText);
    dest.writeString(localIconName);
    dest.writeString(remoteIconName);
    dest.writeString(remoteDarkIconName);
    dest.writeString(description);
    dest.writeString(id);
    dest.writeParcelable(color, 0);
  }

  public String getLocalIconName() {
    return localIconName;
  }

  public void setLocalIconName(String localIconName) {
    this.localIconName = localIconName;
  }

  public String getRemoteIconName() {
    return remoteIconName;
  }

  public void setRemoteIconName(String remoteIconName) {
    this.remoteIconName = remoteIconName;
  }

  @Nullable
  public String getRemoteDarkIconName() {
    return remoteDarkIconName;
  }

  public void setRemoteDarkIconName(String remoteDarkIconName) {
    this.remoteDarkIconName = remoteDarkIconName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public VehicleMode getModeCompat() {
    return VehicleMode.from(localIconName);
  }

  @Nullable
  public String getId() {
    return id;
  }

  void setId(String id) {
    this.id = id;
  }

  @Nullable
  public ServiceColor getColor() {
    return color;
  }

  void setColor(ServiceColor color) {
    this.color = color;
  }
}