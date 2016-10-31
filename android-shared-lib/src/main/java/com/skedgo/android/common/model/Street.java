/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.util.LogUtils;
import com.skedgo.android.common.util.PolylineEncoderUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created: 19/02/13 11:52 AM
 */
public class Street implements Parcelable {
  public static final String TAG = LogUtils.makeTag(Street.class);
  public static final Creator<Street> CREATOR = new Creator<Street>() {
    public Street createFromParcel(Parcel in) {
      Street s = new Street();

      s.mId = in.readLong();
      s.mName = in.readString();
      s.mMeters = in.readFloat();
      s.mWayPoints = in.readArrayList(Double.class.getClassLoader());
      s.mWaypointEncoding = in.readString();
      s.safe = in.readByte() == 1;
      return s;
    }

    public Street[] newArray(int size) {
      return new Street[size];
    }
  };
  private transient long mId;
  @SerializedName("name")
  private String mName;
  @SerializedName("metres")
  private float mMeters;
  @SerializedName("waypoints")
  private List<Double> mWayPoints;
  private boolean safe;
  /**
   * Save encoding .. we will lazily decode it..
   * <p/>
   * NOTE: As of v3 of the TripGO API, the server does the work for us!
   */
  @SerializedName("encodedWaypoints")
  private String mWaypointEncoding;

  public long getId() {
    return mId;
  }

  public void setId(final long id) {
    mId = id;
  }

  public String getName() {
    return mName;
  }

  public void setName(final String name) {
    this.mName = name;
  }

  public float getMeters() {
    return mMeters;
  }

  public void setMetres(final float meters) {
    this.mMeters = meters;
  }

  public List<Double> getWayPoints() {
    if ((mWayPoints == null || mWayPoints.isEmpty()) && !TextUtils.isEmpty(mWaypointEncoding)) {
      List<LatLng> coords = PolylineEncoderUtils.decode(mWaypointEncoding);

      if (coords != null) {
        List<Double> wps = new LinkedList<Double>();
        for (LatLng c : coords) {
          wps.add(c.latitude);
          wps.add(c.longitude);
        }

        setWayPoints(wps);
      }
    }

    return mWayPoints;
  }

  public void setWayPoints(List<Double> wps) {
    mWayPoints = wps;
  }

  public String getWaypointEncoding() {
    return mWaypointEncoding;
  }

  public void setWaypointEncoding(String waypointEncoding) {
    mWaypointEncoding = waypointEncoding;
  }

  @Override
  public int describeContents() {
    return hashCode();
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeLong(mId);
    dest.writeString(mName);
    dest.writeFloat(mMeters);
    dest.writeList(mWayPoints);
    dest.writeString(mWaypointEncoding);
    dest.writeByte((byte) (safe ? 1 : 0));
  }

  public boolean isSafe() {
    return safe;
  }

  public void setSafe(boolean safe) {
    this.safe = safe;
  }
}
