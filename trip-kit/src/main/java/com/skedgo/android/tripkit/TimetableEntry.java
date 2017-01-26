package com.skedgo.android.tripkit;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.agenda.IRealTimeElement;
import com.skedgo.android.common.model.ITimeRange;
import com.skedgo.android.common.model.ModeInfo;
import com.skedgo.android.common.model.RealTimeStatus;
import com.skedgo.android.common.model.RealTimeVehicle;
import com.skedgo.android.common.model.RealtimeAlert;
import com.skedgo.android.common.model.ScheduledStop;
import com.skedgo.android.common.model.ServiceColor;
import com.skedgo.android.common.model.VehicleMode;
import com.skedgo.android.common.rx.Var;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * (Aka Service)
 */
public class TimetableEntry implements Parcelable, IRealTimeElement, ITimeRange {
  public static final Creator<TimetableEntry> CREATOR = new Creator<TimetableEntry>() {
    public TimetableEntry createFromParcel(Parcel in) {
      TimetableEntry service = new TimetableEntry();

      service.id = in.readLong();
      service.stopCode = in.readString();
      service.serviceTripId = in.readString();
      service.serviceNumber = in.readString();
      service.serviceName = in.readString();
      service.realTimeStatus = RealTimeStatus.from(in.readString());
      service.startSecs().put(in.readLong());
      service.endSecs().put(in.readLong());
      service.serviceColor = in.readParcelable(ServiceColor.class.getClassLoader());
      service.frequency = in.readInt();
      service.isFavourite = in.readInt() == 1;
      service.alerts = in.readArrayList(RealtimeAlert.class.getClassLoader());
      service.searchString = in.readString();
      service.endStopCode = in.readString();
      service.startStop = in.readParcelable(ScheduledStop.class.getClassLoader());
      service.endStop = in.readParcelable(ScheduledStop.class.getClassLoader());
      service.mode = VehicleMode.from(in.readString());
      service.pairIdentifier = in.readString();
      service.operator = in.readString();
      service.realtimeVehicle = in.readParcelable(RealTimeVehicle.class.getClassLoader());
      service.serviceTime = in.readLong();
      service.modeInfo = in.readParcelable(ModeInfo.class.getClassLoader());
      service.serviceDirection = in.readString();
      service.wheelchairAccessible = in.readByte() == 1;
      return service;
    }

    public TimetableEntry[] newArray(int size) {
      return new TimetableEntry[size];
    }
  };
  public final transient Var<List<StopInfo>> stops = Var.create();
  /**
   * For A2B-timetable-related stuff.
   */
  public transient ScheduledStop startStop;
  /**
   * For A2B-timetable-related stuff.
   */
  public transient ScheduledStop endStop;
  /**
   * For A2B-timetable-related stuff.
   */
  public String pairIdentifier;
  private transient Var<Long> startSecs;
  private transient Var<Long> endSecs;
  private transient long id;
  private transient boolean isFavourite;
  @SerializedName("realtimeVehicle") private RealTimeVehicle realtimeVehicle;
  @SerializedName("stopCode") private String stopCode;
  @SerializedName("modeInfo") private ModeInfo modeInfo;
  @SerializedName("operator") private String operator;
  @SerializedName("endStopCode") private String endStopCode;
  @SerializedName("serviceTripID") private String serviceTripId;
  @SerializedName("serviceNumber") private String serviceNumber;
  @SerializedName("serviceName") private String serviceName;
  @SerializedName("serviceDirection") private String serviceDirection;
  @SerializedName("realTimeStatus") private RealTimeStatus realTimeStatus;
  @SerializedName("alerts") private ArrayList<RealtimeAlert> alerts;
  @SerializedName("serviceColor") private ServiceColor serviceColor;
  @SerializedName("frequency") private int frequency;
  @SerializedName("searchString") private String searchString;
  private boolean wheelchairAccessible;
  /**
   * Replacement: {@link #modeInfo}.
   */
  @Deprecated @SerializedName("mode") private VehicleMode mode;
  /**
   * This field is primarily used to interact with Gson or Parcel.
   */
  @SerializedName("startTime") private long serializedStartSecs;
  /**
   * This field is primarily used to interact with Gson or Parcel.
   */
  @SerializedName("endTime") private long serializedEndSecs;
  /**
   * Service time is initially the same as "startTime". If is a realtime service, here we save the
   * service time, while startTime will have the real arriving time.
   */
  private long serviceTime;

  public TimetableEntry() {
    stops.observe()
        .flatMap(new Func1<List<StopInfo>, Observable<StopInfo>>() {
          @Override
          public Observable<StopInfo> call(List<StopInfo> stops) {
            return Observable.from(stops);
          }
        })
        .subscribe(new Action1<StopInfo>() {
          @Override
          public void call(final StopInfo stop) {
            // Determine the current stop that the user are standing at.
            if (TextUtils.equals(stop.stop.getCode(), stopCode)) {
              // Service time and this stop time are the same. They're correlated.
              // If real-time data change service time, this stop time also gets changed accordingly.
              startSecs().observe().subscribe(new Action1<Long>() {
                @Override
                public void call(Long secs) {
                  stop.stop.departureSecs().put(secs);
                }
              });
            }
          }
        });

    // For debug purpose only.
    if (BuildConfig.DEBUG) {
      stops.observe().subscribe(new Action1<List<StopInfo>>() {
        @Override
        public void call(List<StopInfo> stops) {
          Log.w("LoadStops", "Got " + stops.size() + " stops for: " + serviceNumber + " - " + TimetableEntry.this);
        }
      });
    }
  }

  @Nullable public String getServiceDirection() {
    return serviceDirection;
  }

  public void setServiceDirection(String serviceDirection) {
    this.serviceDirection = serviceDirection;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStopCode() {
    return stopCode;
  }

  public void setStopCode(String stopCode) {
    this.stopCode = stopCode;
  }

  public String getServiceTripId() {
    return serviceTripId;
  }

  public void setServiceTripId(String serviceTripId) {
    this.serviceTripId = serviceTripId;
  }

  public String getServiceNumber() {
    return serviceNumber;
  }

  public void setServiceNumber(String serviceNumber) {
    this.serviceNumber = serviceNumber;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public RealTimeStatus getRealTimeStatus() {
    return realTimeStatus;
  }

  public void setRealTimeStatus(RealTimeStatus realTimeStatus) {
    this.realTimeStatus = realTimeStatus;
  }

  @Override
  public long getStartTimeInSecs() {
    return startSecs().value();
  }

  @Override
  public void setStartTimeInSecs(long startTimeInSecs) {
    startSecs().put(startTimeInSecs);
  }

  @Override
  public long getEndTimeInSecs() {
    return endSecs().value();
  }

  @Override
  public void setEndTimeInSecs(long endTimeInSecs) {
    endSecs().put(endTimeInSecs);
  }

  public ServiceColor getServiceColor() {
    return serviceColor;
  }

  public void setServiceColor(ServiceColor serviceColor) {
    this.serviceColor = serviceColor;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int freq) {
    frequency = freq;
  }

  public boolean isFrequencyBased() {
    return frequency > 0;
  }

  @Override
  public String getStartStopCode() {
    return stopCode;
  }

  @Override
  public void setStartStopCode(String startStopCode) {
    stopCode = startStopCode;
  }

  public boolean isFavourite() {
    return isFavourite;
  }

  public void isFavourite(boolean isFavourite) {
    this.isFavourite = isFavourite;
  }

  public ArrayList<RealtimeAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(ArrayList<RealtimeAlert> alerts) {
    this.alerts = alerts;
  }

  public boolean hasAlerts() {
    return alerts != null && !alerts.isEmpty();
  }

  @Override
  public String getEndStopCode() {
    return endStopCode;
  }

  @Override
  public void setEndStopCode(String endStopCode) {
    this.endStopCode = endStopCode;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * In secs.
   */
  public long getServiceTime() {
    return serviceTime;
  }

  public void setServiceTime(long serviceTime) {
    this.serviceTime = serviceTime;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeLong(id);
    out.writeString(stopCode);
    out.writeString(serviceTripId);
    out.writeString(serviceNumber);
    out.writeString(serviceName);
    out.writeString(realTimeStatus != null ? realTimeStatus.name() : null);
    out.writeLong(startSecs().value());
    out.writeLong(endSecs().value());
    out.writeParcelable(serviceColor, 0);
    out.writeInt(frequency);
    out.writeInt(isFavourite ? 1 : 0);
    out.writeList(alerts);
    out.writeString(searchString);
    out.writeString(endStopCode);
    out.writeParcelable(startStop, 0);
    out.writeParcelable(endStop, 0);
    out.writeString(mode == null ? null : mode.toString());
    out.writeString(pairIdentifier);
    out.writeString(operator);
    out.writeParcelable(realtimeVehicle, 0);
    out.writeLong(serviceTime);
    out.writeParcelable(modeInfo, 0);
    out.writeString(serviceDirection);
    out.writeByte((byte) (wheelchairAccessible ? 1 : 0));
  }

  @Deprecated
  public VehicleMode getMode() {
    return mode;
  }

  @Deprecated
  public void setMode(VehicleMode mode) {
    this.mode = mode;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public Var<Long> startSecs() {
    if (startSecs == null) {
      // We have to lazily initialize this to synchronize
      // the default value with the value updated by Gson or by Parcel beforehand.
      startSecs = Var.create(serializedStartSecs);
      startSecs.observe().subscribe(new Action1<Long>() {
        @Override
        public void call(Long secs) {
          // This makes sure Gson or Parcel will persist the latest value.
          serializedStartSecs = secs;
        }
      });
    }

    return startSecs;
  }

  public Var<Long> endSecs() {
    if (endSecs == null) {
      // We have to lazily initialize this to synchronize
      // the default value with the value updated by Gson or by Parcel beforehand.
      endSecs = Var.create(serializedEndSecs);
      endSecs.observe().subscribe(new Action1<Long>() {
        @Override
        public void call(Long secs) {
          // This makes sure Gson or Parcel will persist the latest value.
          serializedEndSecs = secs;
        }
      });
    }

    return endSecs;
  }

  /**
   * For example, in order to determine a past service trip.
   */
  public boolean isBefore(long pointSecs) {
    if (endSecs().value() > 0) {
      return endSecs().value() < pointSecs;
    } else {
      // Some services don't have arrival time.
      return startSecs().value() < pointSecs;
    }
  }

  /**
   * @param timeUnit Mins, secs or millis.
   * @return Time (in minutes) left to depart.
   */
  public Observable<Long> getTimeLeftToDepart(long period, TimeUnit timeUnit) {
    return Observable.combineLatest(
        Observable.timer(0, period, timeUnit, AndroidSchedulers.mainThread()),
        startSecs().observe(),
        new Func2<Long, Long, Long>() {
          @Override
          public Long call(Long unused, Long secs) {
            long secsToDepart = TimeUnit.SECONDS.toMillis(secs) - System.currentTimeMillis();
            return TimeUnit.MILLISECONDS.toMinutes(secsToDepart);
          }
        });
  }

  /**
   * For debug purpose only.
   */
  @Override
  public String toString() {
    // Trim the package part to print out something less verbal.
    return String.format("%s@%d", TimetableEntry.class.getSimpleName(), hashCode());
  }

  public RealTimeVehicle getRealtimeVehicle() {
    return realtimeVehicle;
  }

  public void setRealtimeVehicle(RealTimeVehicle realtimeVehicle) {
    this.realtimeVehicle = realtimeVehicle;
  }

  public boolean isWheelchairAccessible() {
    return wheelchairAccessible;
  }

  public void setWheelchairAccessible(boolean wheelchairAccessible) {
    this.wheelchairAccessible = wheelchairAccessible;
  }

  @Nullable
  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public void setModeInfo(@Nullable ModeInfo modeInfo) {
    this.modeInfo = modeInfo;
  }
}