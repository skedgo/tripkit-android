package com.skedgo.tripkit.data.locations;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.ScheduledStop;

import java.util.ArrayList;
import java.util.List;

import com.skedgo.tripkit.data.database.locations.bikepods.BikePodEntity;
import com.skedgo.tripkit.data.database.locations.bikepods.BikePodLocationEntity;
import com.skedgo.tripkit.data.database.locations.carparks.CarPark;
import com.skedgo.tripkit.data.database.locations.carparks.CarParkLocation;
import com.skedgo.tripkit.data.database.locations.carpods.CarPodLocation;
import com.skedgo.tripkit.data.database.locations.carrentals.CarRentalCompany;
import com.skedgo.tripkit.data.database.locations.carrentals.CarRentalLocation;
import com.skedgo.tripkit.data.database.locations.freefloating.FreeFloatingLocation;
import com.skedgo.tripkit.data.database.locations.onstreetparking.OnStreetParkingLocation;

public class LocationsResponse {
  @SerializedName("groups")
  private ArrayList<Group> groups;

  public ArrayList<Group> getGroups() {
    return groups;
  }

  @VisibleForTesting void setGroups(ArrayList<Group> groups) {
    this.groups = groups;
  }

  public static class Group {
    @SerializedName("stops")
    private ArrayList<ScheduledStop> stops;

    @SerializedName("bikePods")
    List<BikePodLocationEntity> bikePods;

    @SerializedName("freeFloating")
    List<FreeFloatingLocation> freeFloating;

    @SerializedName("carParks")
    List<CarParkLocation> carParks;

    @SerializedName("onStreetParking")
    List<OnStreetParkingLocation> onStreetParkings;

    @SerializedName("carPods")
    List<CarPodLocation> carPods;

    @SerializedName("carRentals")
    List<CarRentalLocation> carRentals;

    @SerializedName("hashCode")
    private long hashCode;

    @SerializedName("key")
    private String key;

    /**
     * For {@link Gson}.
     */
    public Group() {}

    public Group(long hashCode, String key) {
      this.hashCode = hashCode;
      this.key = key;
    }

    public ArrayList<ScheduledStop> getStops() {
      return stops;
    }

    @VisibleForTesting void setStops(ArrayList<ScheduledStop> stops) {
      this.stops = stops;
    }

    public long getHashCode() {
      return hashCode;
    }

    @VisibleForTesting void setHashCode(long hashCode) {
      this.hashCode = hashCode;
    }

    public String getKey() {
      return key;
    }

    @VisibleForTesting void setKey(String key) {
      this.key = key;
    }

    public List<FreeFloatingLocation> getFreeFloating() { return freeFloating; }
    public List<BikePodLocationEntity> getBikePods() { return bikePods; }
    public List<CarRentalLocation> getCarRentals() { return carRentals; }
    public List<CarParkLocation> getCarParks() { return carParks; }
    public List<CarPodLocation> getCarPods() { return carPods; }
  }
}