package com.skedgo.tripkit.common.model;

import android.os.Parcel;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.R;

import java.util.ArrayList;
import java.util.List;

import com.skedgo.tripkit.routing.ModeInfo;
import com.skedgo.tripkit.routing.VehicleMode;

public class ScheduledStop extends Location {

  public static final Creator<ScheduledStop> CREATOR = new Creator<ScheduledStop>() {

    public ScheduledStop createFromParcel(Parcel in) {
      Location location = Location.CREATOR.createFromParcel(in);

      ScheduledStop stop = new ScheduledStop(location);
      stop.mStopId = in.readLong();
      stop.mCode = in.readString();
      stop.mChildren = in.readArrayList(ScheduledStop.class.getClassLoader());
      stop.mParentId = in.readLong();
      stop.mShortName = in.readString();
      stop.mServices = in.readString();
      stop.mCurrentFilter = in.readString();
      stop.mType = StopType.from(in.readString());
      stop.modeInfo = in.readParcelable(ModeInfo.class.getClassLoader());
      stop.wheelchairAccessible = (Boolean) in.readValue(Boolean.class.getClassLoader());
      stop.alertHashCodes = in.readArrayList(Long.class.getClassLoader());
      return stop;
    }

    public ScheduledStop[] newArray(int size) {
      return new ScheduledStop[size];
    }
  };
  private long mStopId;
  @SerializedName("code")
  private String mCode;
  @SerializedName("services")
  private String mServices;
  @SerializedName("children")
  private ArrayList<ScheduledStop> mChildren;
  @SerializedName("shortName")
  private String mShortName;
  @SerializedName("stopType")
  private StopType mType;
  @SerializedName("modeInfo")
  private ModeInfo modeInfo;
  private transient long mParentId;
  private transient String mCurrentFilter;
  @SerializedName("wheelchairAccessible") private @Nullable Boolean wheelchairAccessible;
  @SerializedName("alertHashCodes") private @Nullable ArrayList<Long> alertHashCodes;

  public ScheduledStop() {
    super();
  }

  public ScheduledStop(Location location) {
    super(location);
  }

  public static VehicleMode convertStopTypeToVehicleMode(StopType type) {
    if (type == null) {
      return null;
    }

    switch (type) {
      case TRAIN:
        return VehicleMode.TRAIN;
      case BUS:
        return VehicleMode.BUS;
      case FERRY:
        return VehicleMode.FERRY;
      case MONORAIL:
        return VehicleMode.MONORAIL;
      case TRAM:
        return VehicleMode.TRAM;
      case SUBWAY:
        return VehicleMode.SUBWAY;
      case CABLECAR:
        return VehicleMode.CABLECAR;
    }

    return null;
  }

  @DrawableRes
  public static int convertStopTypeToTransportModeIcon(StopType type) {
    if (type == null) {
      return 0;
    }

    switch (type) {
      case TRAIN:
        return R.drawable.ic_train;
      case BUS:
        return R.drawable.ic_bus;
      case FERRY:
        return R.drawable.ic_ferry;
      case MONORAIL:
        return R.drawable.ic_monorail;
      case TRAM:
        return R.drawable.ic_tram;
      case SUBWAY:
        return R.drawable.ic_subway;
      case CABLECAR:
        return R.drawable.ic_cablecar;
      default:
        return 0;
    }
  }

  @Override
  public void fillFrom(Location location) {
    if (location == null) {
      return;
    }

    super.fillFrom(location);
    if (location instanceof ScheduledStop) {
      ScheduledStop other = (ScheduledStop) location;
      mStopId = other.mStopId;
      mCode = other.mCode;
      mServices = other.mServices;
      mChildren = other.mChildren;
      mShortName = other.mShortName;
      mType = other.mType;
      modeInfo = other.modeInfo;
      mParentId = other.mParentId;
      mCurrentFilter = other.mCurrentFilter;
      wheelchairAccessible = other.wheelchairAccessible;
      alertHashCodes = other.alertHashCodes;
    }
  }

  public long getStopId() {
    return mStopId;
  }

  public void setStopId(long stopId) {
    mStopId = stopId;
  }

  public String getCode() {
    return mCode;
  }

  public void setCode(String code) {
    mCode = code;
  }

  public ArrayList<ScheduledStop> getChildren() {
    return mChildren;
  }

  public void setChildren(ArrayList<ScheduledStop> children) {
    mChildren = children;
  }

  public String getServices() {
    return mServices;
  }

  public void setServices(String services) {
    mServices = services;
  }

  public long getParentId() {
    return mParentId;
  }

  public String getShortName() {
    return mShortName;
  }

  public void setShortName(String shortName) {
    mShortName = shortName;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public void setModeInfo(ModeInfo modeInfo) {
    this.modeInfo = modeInfo;
  }

  public StopType getType() {
    return mType;
  }

  public void setType(StopType type) {
    mType = type;
  }

  public boolean hasChildren() {
    return mChildren != null && !mChildren.isEmpty();
  }

  /**
   * Alias of {@link #hasChildren()}. If a stop has children, it's a parent stop.
   */
  public boolean isParent() {
    return hasChildren();
  }

  public void setParent(long parent) {
    mParentId = parent;
  }

  @Nullable public ArrayList<Long> getAlertHashCodes() {
    return alertHashCodes;
  }

  public void setAlertHashCodes(@Nullable ArrayList<Long> alertHashCodes) {
    this.alertHashCodes = alertHashCodes;
  }

  /**
   * This was deprecated. {@link #isParent()} is now a deriving property of {@link #getChildren()}.
   * If a stop has children, it's a parent stop.
   * <p/>
   * TODO: Remove it.
   */
  @Deprecated
  public void setIsParent(boolean isParent) {}

  public String getCurrentFilter() {
    return mCurrentFilter;
  }

  public void setCurrentFilter(String filter) {
    mCurrentFilter = filter;
  }

  @Override
  public int getLocationType() {
    return Location.TYPE_SCHEDULED_STOP;
  }

  @Nullable public Boolean getWheelchairAccessible() {
    return wheelchairAccessible;
  }

  public void setWheelchairAccessible(@Nullable Boolean wheelchairAccessible) {
    this.wheelchairAccessible = wheelchairAccessible;
  }
  public List<String> getEmbarkationStopCode() {
     ArrayList<String> list = new ArrayList<>();
     list.add(getCode());
     return list;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj != null && (obj instanceof ScheduledStop)) {
      ScheduledStop stop = (ScheduledStop) obj;
      return TextUtils.equals(mCode, stop.mCode);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return mCode != null ? mCode.hashCode() : 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);

    out.writeLong(mStopId);
    out.writeString(mCode);
    out.writeList(mChildren);
    out.writeLong(mParentId);
    out.writeString(mShortName);
    out.writeString(mServices);
    out.writeString(mCurrentFilter);
    out.writeString(mType == null ? null : mType.toString());
    out.writeParcelable(modeInfo, 0);
    out.writeValue(wheelchairAccessible);
    out.writeList(alertHashCodes);
  }
}
