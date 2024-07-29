package com.skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.RealtimeAlert;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class RealTimeVehicle implements Parcelable {
    public static final Creator<RealTimeVehicle> CREATOR = new Creator<RealTimeVehicle>() {
        public RealTimeVehicle createFromParcel(Parcel in) {
            RealTimeVehicle v = new RealTimeVehicle();
            v.mId = in.readLong();
            v.mLocation = in.readParcelable(Location.class.getClassLoader());
            v.mLabel = in.readString();
            v.mLastUpdateTime = in.readLong();
            v.mServiceTripId = in.readString();
            v.mEndStopCode = in.readString();
            v.mStartStopCode = in.readString();
            v.mArriveAtEndStopTime = in.readLong();
            v.mArriveAtStartStopTime = in.readLong();
            v.mAlerts = in.readArrayList(RealtimeAlert.class.getClassLoader());
            v.icon = in.readString();
            v.occupancy = in.readString();
            return v;
        }

        public RealTimeVehicle[] newArray(int size) {
            return new RealTimeVehicle[size];
        }
    };
    private transient long mId;
    private transient String mServiceTripId;
    private transient String mEndStopCode;
    private transient String mStartStopCode;
    private transient long mArriveAtEndStopTime = -1;
    private transient long mArriveAtStartStopTime = -1;
    private transient ArrayList<RealtimeAlert> mAlerts;

    @SerializedName("location")
    private Location mLocation;
    @SerializedName("label")
    private String mLabel;
    @SerializedName("lastUpdate")
    private long mLastUpdateTime;
    @SerializedName("icon")
    @Nullable
    private String icon;
    @SerializedName("occupancy")
    @Nullable
    private String occupancy;
    @SerializedName("components")
    @Nullable
    private List<List<VehicleComponent>> components;

    public long getId() {
        return mId;
    }

    public void setId(final long id) {
        mId = id;
    }

    public String getServiceTripId() {
        return mServiceTripId;
    }

    public void setServiceTripId(final String serviceTripId) {
        mServiceTripId = serviceTripId;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(final Location location) {
        mLocation = location;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(final String label) {
        mLabel = label;
    }

    public long getLastUpdateTime() {
        return mLastUpdateTime;
    }

    public void setLastUpdateTime(final long lastUpdateTime) {
        mLastUpdateTime = lastUpdateTime;
    }

    public String getEndStopCode() {
        return mEndStopCode;
    }

    public void setEndStopCode(final String endStopCode) {
        mEndStopCode = endStopCode;
    }

    public String getStartStopCode() {
        return mStartStopCode;
    }

    public void setStartStopCode(final String startStopCode) {
        mStartStopCode = startStopCode;
    }

    public long getArriveAtEndStopTime() {
        return mArriveAtEndStopTime;
    }

    public void setArriveAtEndStopTime(final long arriveAtEndStopTime) {
        mArriveAtEndStopTime = arriveAtEndStopTime;
    }

    public long getArriveAtStartStopTime() {
        return mArriveAtStartStopTime;
    }

    public void setArriveAtStartStopTime(final long arriveAtStartStopTime) {
        mArriveAtStartStopTime = arriveAtStartStopTime;
    }

    public ArrayList<RealtimeAlert> getAlerts() {
        return mAlerts;
    }

    public void setAlerts(final ArrayList<RealtimeAlert> alerts) {
        mAlerts = alerts;
    }

    public void addAlert(RealtimeAlert alert) {
        if (mAlerts == null) {
            mAlerts = new ArrayList<>();
        }

        mAlerts.add(alert);
    }

    public boolean hasLocationInformation() {
        return mLocation != null;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(mId);
        dest.writeParcelable(mLocation, 0);
        dest.writeString(mLabel);
        dest.writeLong(mLastUpdateTime);
        dest.writeString(mServiceTripId);
        dest.writeString(mEndStopCode);
        dest.writeString(mStartStopCode);
        dest.writeLong(mArriveAtEndStopTime);
        dest.writeLong(mArriveAtStartStopTime);
        dest.writeList(mAlerts);
        dest.writeString(icon);
        dest.writeString(occupancy);
    }

    @Nullable
    public Occupancy getOccupancy() {
        return OccupancyKt.toOccupancy(occupancy);
    }

    public void setOccupancy(@Nullable String occupancy) {
        this.occupancy = occupancy;
    }

    @Nullable
    public List<List<VehicleComponent>> getComponents() {
        return components;
    }

    public void setComponents(@Nullable List<List<VehicleComponent>> components) {
        this.components = components;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    public void setIcon(@Nullable String icon) {
        this.icon = icon;
    }
}