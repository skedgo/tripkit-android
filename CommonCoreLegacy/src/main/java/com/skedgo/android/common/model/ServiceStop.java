package com.skedgo.android.common.model;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.rx.Var;
import com.skedgo.android.common.util.DateTimeFormats;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import skedgo.tripkit.routing.TripSegment;

/**
 * Represents a future stop of a service in a trip.
 */
public class ServiceStop extends Location {
  public static final Creator<ServiceStop> CREATOR = new Creator<ServiceStop>() {
    public ServiceStop createFromParcel(Parcel in) {
      Location location = Location.CREATOR.createFromParcel(in);
      ServiceStop stop = new ServiceStop(location);

      stop.departureSecs().put(in.readLong());
      stop.arrivalTime = in.readLong();
      stop.relativeArrival = in.readLong();
      stop.relativeDeparture = in.readLong();
      stop.code = in.readString();
      stop.type = StopType.from(in.readString());

      return stop;
    }

    public ServiceStop[] newArray(int size) {
      return new ServiceStop[size];
    }
  };
  /**
   * This should be a bus/train segment.
   */
  private final transient Var<TripSegment> segment = Var.create();
  /**
   * This gets initialized lazily. So don't access it directly. Use its getter instead.
   */
  private transient Var<Long> departureSecs;
  private StopType type;
  /**
   * This field is primarily used to interact with Gson.
   */
  @SerializedName("departure")
  private long serializedDepartureSecs;
  @SerializedName("relativeDeparture")
  private long relativeArrival;
  @SerializedName("relativeArrival")
  private long relativeDeparture;
  @SerializedName("arrival")
  private long arrivalTime;
  @SerializedName("code")
  private String code;

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

  public long getRelativeDeparture() {
    return relativeDeparture;
  }

  public void setRelativeDeparture(long relativeDeparture) {
    this.relativeDeparture = relativeDeparture;
  }

  public long getRelativeArrival() {
    return relativeArrival;
  }

  public void setRelativeArrival(long relativeArrival) {
    this.relativeArrival = relativeArrival;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public StopType getType() {
    return type;
  }

  public void setType(StopType type) {
    this.type = type;
  }

  public String getDisplayTime(Context context) {
    if (departureSecs().value() != 0) {
      final long millis = TimeUnit.SECONDS.toMillis(departureSecs().value());
      return DateTimeFormats.printTime(context, millis, getTimeZone());
    } else if (arrivalTime != 0) {
      final long millis = TimeUnit.SECONDS.toMillis(arrivalTime);
      return DateTimeFormats.printTime(context, millis, getTimeZone());
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
    }
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeLong(departureSecs().value());
    out.writeLong(arrivalTime);
    out.writeLong(relativeArrival);
    out.writeLong(relativeDeparture);
    out.writeString(code);
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

  public Var<TripSegment> segment() {
    return segment;
  }
}