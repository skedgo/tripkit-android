package com.skedgo.android.common.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.rx.Var;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import rx.functions.Action1;

/**
 * Represents a future stop of a service in a trip.
 */
public class ServiceStop extends Location implements WheelchairAccessible {
  public static final Creator<ServiceStop> CREATOR = new Creator<ServiceStop>() {
    public ServiceStop createFromParcel(Parcel in) {
      Location location = Location.CREATOR.createFromParcel(in);
      ServiceStop stop = new ServiceStop(location);

      stop.departureSecs().put(in.readLong());
      stop.arrivalTime = in.readLong();
      stop.relativeArrival = (Long) in.readValue(Long.class.getClassLoader());
      stop.relativeDeparture = (Long) in.readValue(Long.class.getClassLoader());
      stop.code = in.readString();
      stop.shortName = in.readString();
      stop.wheelchairAccessible = (Boolean) in.readValue(Boolean.class.getClassLoader());
      stop.type = StopType.from(in.readString());

      return stop;
    }

    public ServiceStop[] newArray(int size) {
      return new ServiceStop[size];
    }
  };
  /**
   * This gets initialized lazily. So don't access it directly. Use its getter instead.
   */
  private transient Var<Long> departureSecs;
  private StopType type;
  /**
   * This field is primarily used to interact with Gson.
   */
  @SerializedName("departure") private long serializedDepartureSecs;
  @SerializedName("relativeDeparture") private @Nullable Long relativeArrival;
  @SerializedName("relativeArrival") private @Nullable Long relativeDeparture;
  @SerializedName("arrival") private long arrivalTime;
  @SerializedName("code") private String code;
  @SerializedName("shortName") private @Nullable String shortName;
  @SerializedName("wheelchairAccessible") private @Nullable Boolean wheelchairAccessible;

  public ServiceStop() {}

  public ServiceStop(Location location) {
    super(location);
  }

  public long getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(long arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public @Nullable Long getRelativeDeparture() {
    return relativeDeparture;
  }

  public void setRelativeDeparture(@Nullable Long relativeDeparture) {
    this.relativeDeparture = relativeDeparture;
  }

  public @Nullable Long getRelativeArrival() {
    return relativeArrival;
  }

  public void setRelativeArrival(@Nullable Long relativeArrival) {
    this.relativeArrival = relativeArrival;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Nullable public String getShortName() {
    return shortName;
  }

  public void setShortName(@Nullable String shortName) {
    this.shortName = shortName;
  }

  @Nullable public Boolean getWheelchairAccessible() {
    return wheelchairAccessible;
  }

  public void setWheelchairAccessible(@Nullable Boolean wheelchairAccessible) {
    this.wheelchairAccessible = wheelchairAccessible;
  }

  public StopType getType() {
    return type;
  }

  public void setType(StopType type) {
    this.type = type;
  }

  public Long getDisplayTime() {
    if (departureSecs().value() != 0) {
      return TimeUnit.SECONDS.toMillis(departureSecs().value());
    } else if (arrivalTime != 0) {
      return TimeUnit.SECONDS.toMillis(arrivalTime);
    } else {
      return null;
    }
  }

  @Override
  public void fillFrom(Location other) {
    if (other == null) {
      return;
    }

    super.fillFrom(other);

    if (other instanceof ServiceStop) {
      ServiceStop otherStop = (ServiceStop) other;
      type = otherStop.type;
      departureSecs().put(otherStop.departureSecs().value());
      arrivalTime = otherStop.arrivalTime;
      code = otherStop.code;
      shortName = otherStop.shortName;
      wheelchairAccessible = otherStop.wheelchairAccessible;
    }
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeLong(departureSecs().value());
    out.writeLong(arrivalTime);
    out.writeValue(relativeArrival);
    out.writeValue(relativeDeparture);
    out.writeString(code);
    out.writeString(shortName);
    out.writeValue(wheelchairAccessible);
    out.writeString(type == null ? null : type.toString());
  }

  public Var<Long> departureSecs() {
    if (departureSecs == null) {
      // We have to lazily initialize this to synchronize
      // the default value with the value updated by Gson or by Parcel beforehand.
      departureSecs = Var.create(serializedDepartureSecs);
      departureSecs.observe().subscribe(new Action1<Long>() {
        @Override
        public void call(Long secs) {
          // This makes sure Gson or Parcel will persist the latest value.
          serializedDepartureSecs = secs;
        }
      });
    }

    return departureSecs;
  }
}