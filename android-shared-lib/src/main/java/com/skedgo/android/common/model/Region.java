package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.PolyUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Region implements Parcelable {
  public static final Creator<Region> CREATOR = new Creator<Region>() {
    @SuppressWarnings("unchecked")
    @Override
    public Region createFromParcel(Parcel source) {
      Region region = new Region();
      region.name = source.readString();

      region.urls = new ArrayList<>();
      source.readStringList(region.urls);

      region.timezone = source.readString();
      region.transportModeIds = new ArrayList<>();
      source.readStringList(region.transportModeIds);
      region.encodedPolyline = source.readString();
      region.cities = source.readArrayList(City.class.getClassLoader());
      return region;
    }

    @Override
    public Region[] newArray(int size) {
      return new Region[size];
    }
  };
  private transient volatile List<LatLng> polygon; // Lazily initialized.

  @SerializedName("cities") private ArrayList<City> cities;
  @SerializedName("polygon") private String encodedPolyline;
  @SerializedName("name") private String name;
  @SerializedName("urls") private ArrayList<String> urls;
  @SerializedName("timezone") private String timezone;
  @SerializedName("modes") private ArrayList<String> transportModeIds;

  @Nullable
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Nullable
  public ArrayList<String> getURLs() {
    return urls;
  }

  public void setURLs(final ArrayList<String> urls) {
    this.urls = urls;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(final String timezone) {
    this.timezone = timezone;
  }

  @Nullable
  public ArrayList<String> getTransportModeIds() {
    return transportModeIds;
  }

  public void setTransportModeIds(ArrayList<String> transportModeIds) {
    this.transportModeIds = transportModeIds;
  }

  public boolean contains(@Nullable Location location) {
    return location != null && contains(new LatLng(location.getLat(), location.getLon()));
  }

  public boolean contains(LatLng latLng) {
    if (encodedPolyline == null) {
      return false;
    }

    final List<LatLng> polygon = getPolygon();
    return CollectionUtils.isNotEmpty(polygon)
        // FIXME: This isn't an optimal solution if it gets called inside a loop, causing a lot of allocations.
        // See: https://medium.com/google-developers/developing-for-android-ii-bb9a51f8c8b9.
        && PolyUtil.containsLocation(latLng, polygon, true);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Region)) {
      return false;
    }

    Region region = (Region) o;
    return TextUtils.equals(region.name, name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeStringList(urls);
    dest.writeString(timezone);
    dest.writeStringList(transportModeIds);
    dest.writeString(encodedPolyline);
    dest.writeList(cities);
  }

  /**
   * @return Region name
   */
  @Override
  public String toString() {
    return name;
  }

  @Nullable
  public String getEncodedPolyline() {
    return encodedPolyline;
  }

  public void setEncodedPolyline(String encodedPolyline) {
    this.encodedPolyline = encodedPolyline;
  }

  @Nullable
  public ArrayList<City> getCities() {
    return cities;
  }

  public void setCities(ArrayList<City> cities) {
    this.cities = cities;
  }

  /**
   * This was copied from the LazyInitializer class of Apache Commons Lang.
   * It's aimed to lazily initialized polygon thread-safely.
   *
   * @see <a href="http://antrix.net/posts/2012/java-lazy-initialization/">Lazy Initialization in Java</a>
   */
  List<LatLng> getPolygon() {
    List<LatLng> var = polygon;
    if (var == null) {
      synchronized (this) {
        var = polygon;
        if (var == null) {
          polygon = var = PolyUtil.decode(encodedPolyline);
        }
      }
    }

    return var;
  }

  public static class City extends Location {}
}