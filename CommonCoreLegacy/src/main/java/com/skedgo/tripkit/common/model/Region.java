package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.Nullable;

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

    @SerializedName("cities")
    private ArrayList<City> cities;
    @SerializedName("polygon")
    private String encodedPolyline;
    @SerializedName("name")
    private String name;
    @SerializedName("urls")
    private ArrayList<String> urls;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("modes")
    private ArrayList<String> transportModeIds;

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

    public static class City extends Location {
    }
}