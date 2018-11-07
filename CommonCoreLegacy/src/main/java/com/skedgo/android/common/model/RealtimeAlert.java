package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

/**
 * @see <a href="https://github.com/skedgo/skedgo-java/blob/production/RealTime/src/main/java/com/buzzhives/Realtime/RealtimeAlert.java">RealtimeAlert</a>
 */
@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
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
          .remoteIcon(in.readString())
          .alertAction((AlertAction) in.readParcelable(AlertAction.class.getClassLoader()))
          .lastUpdated(in.readLong())
          .fromDate(in.readLong())
          .build();
    }

    public RealtimeAlert[] newArray(int size) {
      return new RealtimeAlert[size];
    }
  };

  @Nullable public abstract String title();
  @SerializedName("hashCode") public abstract long remoteHashCode();
  @AlertSeverity @Nullable public abstract String severity();
  @Nullable public abstract String text();
  @Nullable public abstract String url();
  @Nullable public abstract String remoteIcon();
  @Nullable public abstract Location location();

  @Value.Default public long lastUpdated() {
    return -1L;
  }

  @Value.Default public long fromDate() {
    return -1L;
  }

  @Deprecated @Nullable public abstract String serviceTripID();

  @Deprecated @Nullable public abstract String stopCode();

  @SerializedName("action") @Nullable public abstract AlertAction alertAction();

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
    out.writeString(remoteIcon());
    out.writeParcelable(alertAction(), 0);
    out.writeLong(lastUpdated());
    out.writeLong(fromDate());
  }
}