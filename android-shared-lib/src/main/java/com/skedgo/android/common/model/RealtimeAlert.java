package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * @see <a href="https://github.com/skedgo/skedgo-java/blob/production/RealTime/src/main/java/com/buzzhives/Realtime/RealtimeAlert.java">RealtimeAlert</a>
 */
@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRealtimeAlert.class)
public abstract class RealtimeAlert implements Parcelable {
  public static final String SEVERITY_ALERT = "alert";
  public static final String SEVERITY_WARNING = "warning";
  public static final Creator<RealtimeAlert> CREATOR = new Creator<RealtimeAlert>() {
    public RealtimeAlert createFromParcel(Parcel in) {
      return ImmutableRealtimeAlert.builder()
          .title(in.readString())
          .text(in.readString())
          .serviceTripID(in.readString())
          .stopCode(in.readString())
          .remoteHashCode(in.readLong())
          .severity(in.readString())
          .location((Location) in.readParcelable(Location.class.getClassLoader()))
          .url(in.readString())
          .build();
    }

    public RealtimeAlert[] newArray(int size) {
      return new RealtimeAlert[size];
    }
  };

  @Nullable public abstract String title();
  @Nullable public abstract String text();
  @Nullable public abstract String serviceTripID();
  @Nullable public abstract String stopCode();
  @SerializedName("hashCode") public abstract long remoteHashCode();
  @Nullable public abstract Location location();
  @AlertSeverity @Nullable public abstract String severity();
  @Nullable public abstract String url();

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    out.writeString(title());
    out.writeString(text());
    out.writeString(serviceTripID());
    out.writeString(stopCode());
    out.writeLong(remoteHashCode());
    out.writeString(severity());
    out.writeParcelable(location(), 0);
    out.writeString(url());
  }
}