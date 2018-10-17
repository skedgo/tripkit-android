package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.model.ServiceStop;

import java.util.List;

public class Shape implements Parcelable {
  public static final Creator<Shape> CREATOR = new Creator<Shape>() {
    public Shape createFromParcel(Parcel in) {
      Shape shape = new Shape();

      shape.id = in.readLong();
      shape.isTravelled = in.readInt() == 1;
      shape.stops = in.readArrayList(ServiceStop.class.getClassLoader());
      shape.encodedWaypoints = in.readString();
      shape.serviceColor = in.readParcelable(ServiceColor.class.getClassLoader());

      return shape;
    }

    public Shape[] newArray(int size) {
      return new Shape[size];
    }
  };
  private long id;
  @SerializedName("travelled") private boolean isTravelled;
  @SerializedName("stops") private @Nullable List<ServiceStop> stops;
  @SerializedName("serviceColor") private ServiceColor serviceColor;
  @SerializedName("encodedWaypoints") private String encodedWaypoints;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isTravelled() {
    return isTravelled;
  }

  public void isTravelled(boolean isTravelled) {
    this.isTravelled = isTravelled;
  }

  public @Nullable List<ServiceStop> getStops() {
    return stops;
  }

  public void setStops(@Nullable List<ServiceStop> stops) {
    this.stops = stops;
  }

  public String getEncodedWaypoints() {
    return encodedWaypoints;
  }

  public void setEncodedWaypoints(String encodedWaypoints) {
    this.encodedWaypoints = encodedWaypoints;
  }

  public ServiceColor getServiceColor() {
    return serviceColor;
  }

  public void setServiceColor(ServiceColor serviceColor) {
    this.serviceColor = serviceColor;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeLong(id);
    out.writeInt(isTravelled ? 1 : 0);
    out.writeList(stops);
    out.writeString(encodedWaypoints);
    out.writeParcelable(serviceColor, 0);
  }
}