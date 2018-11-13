package com.skedgo.android.common.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.TransactionTooLargeException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a query to find routes from A to B.
 * <p>
 * Note that, to avoid {@link TransactionTooLargeException}, it's discouraged to
 * pass any instance of {@link Query} to {@link Intent} or {@link Bundle}.
 * The {@link Parcelable} is subject to deletion at anytime.
 */
public class Query implements Parcelable {
  public static final Creator<Query> CREATOR = new Creator<Query>() {
    public Query createFromParcel(Parcel in) {
      Query query = new Query();
      query.uuid = in.readString();
      query.mUnit = in.readString();
      query.mFromLocation = in.readParcelable(Location.class.getClassLoader());
      query.mToLocation = in.readParcelable(Location.class.getClassLoader());
      query.timeTag = in.readParcelable(TimeTag.class.getClassLoader());

      query.mTimeWeight = in.readInt();
      query.mBudgetWeight = in.readInt();
      query.mHassleWeight = in.readInt();
      query.mEnvironmentWeight = in.readInt();
      query.mRegion = in.readParcelable(Region.class.getClassLoader());
      query.mTransferTime = in.readInt();
      query.mCyclingSpeed = in.readInt();
      query.mWalkingSpeed = in.readInt();
      query.transportModeIds = query.readTransportModeIds(in);
      query.maxWalkingTime = in.readInt();

      List<String> stops = new ArrayList<>();
      in.readStringList(stops);
      query.excludedStopCodes = stops;
      return query;
    }

    public Query[] newArray(int size) {
      return new Query[size];
    }
  };

  private String uuid = UUID.randomUUID().toString();
  private List<String> transportModeIds = new ArrayList<>();
  private String mUnit;
  private Location mFromLocation;
  private Location mToLocation;
  private Region mRegion;
  private TimeTag timeTag;
  private int mTimeWeight;
  private int mBudgetWeight;
  private int mHassleWeight;
  private int mEnvironmentWeight;
  private int mCyclingSpeed;
  private int mWalkingSpeed;
  private int mTransferTime;
  private List<String> excludedStopCodes = new ArrayList<>();

  /**
   * This is only used for XUM project. TripGo may not need it.
   * See more: https://www.flowdock.com/app/skedgo/androiddev/threads/nZJbtLU0jgsgziQpuoqhcaB-U9A.
   */
  private int maxWalkingTime;

  public Query() {}

  public Query clone() {
    return clone(false);
  }

  public Query clone(boolean cloneTransportMode) {
    Query query = new Query();
    query.mFromLocation = mFromLocation;
    query.mToLocation = mToLocation;
    query.timeTag = timeTag;
    query.mTimeWeight = mTimeWeight;
    query.mBudgetWeight = mBudgetWeight;
    query.mHassleWeight = mHassleWeight;
    query.mEnvironmentWeight = mEnvironmentWeight;
    query.mUnit = mUnit;
    query.mTransferTime = mTransferTime;
    query.mCyclingSpeed = mCyclingSpeed;
    query.mWalkingSpeed = mWalkingSpeed;
    query.mRegion = mRegion;
    query.maxWalkingTime = maxWalkingTime;

    // Perform deep copy of modes, so that removing member of one list doesn't affect the other.
    if (cloneTransportMode) {
      List<String> cloneTransportModeIds = new ArrayList<>(transportModeIds.size());
      cloneTransportModeIds.addAll(transportModeIds);
      query.setTransportModeIds(cloneTransportModeIds);
    }

    // clone excludedStopCodes
    List<String> cloneExcludedStopCodes = new ArrayList<>(excludedStopCodes.size());
    cloneExcludedStopCodes.addAll(excludedStopCodes);
    query.setExcludedStopCodes(cloneExcludedStopCodes);

    return query;
  }

  /**
   * @return values of {@link Units}.
   */
  public String getUnit() {
    if (TextUtils.isEmpty(mUnit)) {
      mUnit = Units.UNIT_AUTO;
    }
    return mUnit;
  }

  /**
   * @param unit Must be values of {@link Units}.
   */
  public void setUnit(final String unit) {
    this.mUnit = unit;
    if (TextUtils.isEmpty(this.mUnit)) {
      this.mUnit = Units.UNIT_AUTO;
    }
  }

  public boolean originIsCurrentLocation() {
    return mFromLocation == null;
  }

  public boolean destinationIsCurrentLocation() {
    return mToLocation == null;
  }

  @Nullable
  public Location getFromLocation() {
    return mFromLocation;
  }

  public void setFromLocation(final Location location) {
    this.mFromLocation = location;
  }

  @Nullable
  public Location getToLocation() {
    return mToLocation;
  }

  public void setToLocation(final Location location) {
    this.mToLocation = location;
  }

  @Nullable
  public Region getRegion() {
    return mRegion;
  }

  public void setRegion(Region region) {
    mRegion = region;
  }

  @Nullable
  public TimeTag getTimeTag() {
    return timeTag;
  }

  public void setTimeTag(@NonNull TimeTag timeTag) {
    this.timeTag = timeTag;
  }

  /**
   * @return Time in secs.
   */
  public long getDepartAfter() {
    if (timeTag != null && timeTag.getType() == TimeTag.TIME_TYPE_LEAVE_AFTER) {
      return TimeUnit.SECONDS.toMillis(timeTag.getTimeInSecs());
    } else {
      return -1;
    }
  }

  /**
   * @return Time in secs.
   */
  public long getArriveBy() {
    if (timeTag != null && timeTag.getType() == TimeTag.TIME_TYPE_ARRIVE_BY) {
      return TimeUnit.SECONDS.toMillis(timeTag.getTimeInSecs());
    } else {
      return -1;
    }
  }

  public List<String> getTransportModeIds() {
    return transportModeIds;
  }

  public void setTransportModeIds(List<String> transportModeIds) {
    this.transportModeIds = transportModeIds;
  }

  public List<String> getExcludedStopCodes() {
    return excludedStopCodes;
  }

  public void setExcludedStopCodes(List<String> excludedStopCodes) {
    this.excludedStopCodes = excludedStopCodes;
  }

  public int getTimeWeight() {
    return mTimeWeight;
  }

  public void setTimeWeight(int weight) {
    if (weight < 0) {
      weight = 0;
    } else if (weight > 100) {
      weight = 100;
    }

    this.mTimeWeight = weight;
  }

  public int getBudgetWeight() {
    return mBudgetWeight;
  }

  public void setBudgetWeight(int weight) {
    if (weight < 0) {
      weight = 0;
    } else if (weight > 100) {
      weight = 100;
    }

    this.mBudgetWeight = weight;
  }

  public int getHassleWeight() {
    return mHassleWeight;
  }

  public void setHassleWeight(int weight) {
    if (weight < 0) {
      weight = 0;
    } else if (weight > 100) {
      weight = 100;
    }

    this.mHassleWeight = weight;
  }

  public int getEnvironmentWeight() {
    return mEnvironmentWeight;
  }

  public void setEnvironmentWeight(int weight) {
    if (weight < 0) {
      weight = 0;
    } else if (weight > 100) {
      weight = 100;
    }

    this.mEnvironmentWeight = weight;
  }

  public int getTransferTime() {
    return mTransferTime;
  }

  public void setTransferTime(int transferTime) {
    mTransferTime = transferTime;
  }

  public int getCyclingSpeed() {
    return mCyclingSpeed;
  }

  public void setCyclingSpeed(int cyclingSpeed) {
    mCyclingSpeed = cyclingSpeed;
  }

  public int getWalkingSpeed() {
    return mWalkingSpeed;
  }

  public void setWalkingSpeed(int walkingSpeed) {
    mWalkingSpeed = walkingSpeed;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Query query = (Query) o;
    return TextUtils.equals(uuid, query.uuid);
  }

  @Override public int hashCode() {
    return uuid.hashCode();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(uuid);
    dest.writeString(mUnit);
    dest.writeParcelable(mFromLocation, 0);
    dest.writeParcelable(mToLocation, 0);
    dest.writeParcelable(timeTag, 0);
    dest.writeInt(mTimeWeight);
    dest.writeInt(mBudgetWeight);
    dest.writeInt(mHassleWeight);
    dest.writeInt(mEnvironmentWeight);
    dest.writeParcelable(mRegion, 0);
    dest.writeInt(mTransferTime);
    dest.writeInt(mCyclingSpeed);
    dest.writeInt(mWalkingSpeed);
    dest.writeStringList(transportModeIds);
    dest.writeInt(maxWalkingTime);
    dest.writeStringList(excludedStopCodes);
  }

  /**
   * In minutes.
   * Note that this is only used for XUM project.
   */
  public int getMaxWalkingTime() {
    return maxWalkingTime;
  }

  /**
   * In minutes.
   * Note that this is only used for XUM project.
   */
  public void setMaxWalkingTime(int minutes) {
    this.maxWalkingTime = minutes;
  }

  public String uuid() {
    return uuid;
  }

  private List<String> readTransportModeIds(Parcel in) {
    List<String> ids = new ArrayList<>();
    in.readStringList(ids);
    return ids;
  }
}