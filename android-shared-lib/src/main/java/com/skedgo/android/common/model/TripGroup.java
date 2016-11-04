package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class TripGroup implements Parcelable {
  public static final Creator<TripGroup> CREATOR = new Creator<TripGroup>() {
    public TripGroup createFromParcel(Parcel in) {
      TripGroup tripGroup = new TripGroup();

      tripGroup.id = in.readLong();
      tripGroup.displayTripId = in.readLong();
      tripGroup.trips = in.readArrayList(Trip.class.getClassLoader());

      if (tripGroup.trips != null) {
        for (Trip trip : tripGroup.trips) {
          trip.setGroup(tripGroup);
        }
      }

      tripGroup.frequency = in.readInt();
      tripGroup.query = in.readParcelable(Query.class.getClassLoader());
      return tripGroup;
    }

    public TripGroup[] newArray(int size) {
      return new TripGroup[size];
    }
  };
  private long id;
  private long displayTripId;
  private Query query;

  @SerializedName("trips") private ArrayList<Trip> trips;
  @SerializedName("frequency") private int frequency;
  private transient GroupVisibility visibility = GroupVisibility.FULL;

  private transient PublishSubject<Pair<ServiceStop, Boolean>> onChangeStop = PublishSubject.create();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDisplayTripId() {
    return displayTripId;
  }

  @Nullable
  public Trip getDisplayTrip() {
    if (trips == null || trips.isEmpty()) {
      return null;
    }

    for (Trip trip : trips) {
      if (displayTripId == trip.getId()) {
        return trip;
      }
    }

    return trips.get(0);
  }

  @Nullable
  public ArrayList<Trip> getTrips() {
    return trips;
  }

  public void setTrips(ArrayList<Trip> trips) {
    if (this.trips != null) {
      this.trips.clear();
    }

    if (trips == null) {
      this.trips = null;
    } else {
      for (Trip trip : trips) {
        addTrip(trip);
      }
    }
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }

  public void addTrip(Trip trip) {
    if (trip == null) {
      return;
    }

    trip.setGroup(this);

    if (trips == null) {
      trips = new ArrayList<Trip>();
    }

    trips.add(trip);
  }

  public boolean containThisMode(TransportMode mode) {
    if (trips == null) {
      return false;
    }

    for (Trip trip : trips) {
      for (TripSegment segment : trip.getSegments()) {
        if (mode.getId().equals(segment.getTransportModeId())) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean containAnyOfTheseModes(List<TransportMode> modes) {
    for (TransportMode mode : modes) {
      if (containThisMode(mode)) {
        return true;
      }
    }

    return false;
  }

  /**
   * A sample use case: Add a trip computed by waypoint API into trip list.
   *
   * @param trip This trip must not belong to group's trips.
   *             Otherwise, {@link IllegalStateException} will be thrown.
   * @throws IllegalStateException if the trip already belongs to the group.
   *                               To change display trip, should invoke {@link TripGroup#changeDisplayTrip(Trip)} instead.
   */
  public void addAsDisplayTrip(@NonNull Trip trip) {
    if (trips != null && trips.contains(trip)) {
      throw new IllegalStateException("Trip already belongs to group");
    }

    // To avoid id conflict with the existing trips in trip list.
    final Trip maxIdTrip = Collections.max(trips, new Comparator<Trip>() {
      @Override public int compare(Trip lhs, Trip rhs) {
        return TripComparators.compareLongs(lhs.getId(), rhs.getId());
      }
    });
    trip.setId(maxIdTrip.getId() + 1);

    // Don't call List<Trip>.add() but this.
    // We need to reference the group for each trip added.
    addTrip(trip);
    changeDisplayTrip(trip);
  }

  /**
   * @param trip This trip must belong to group's trips.
   *             Otherwise, {@link IllegalStateException} will be thrown.
   * @return A TripGroup having new display trip.
   * @throws IllegalStateException if the trip doesn't belong to the group.
   */
  public TripGroup changeDisplayTrip(@NonNull Trip trip) {
    if (trips != null && !trips.contains(trip)) {
      throw new IllegalStateException("Trip does not belong to group");
    }

    displayTripId = trip.getId();
    return this;
  }

  /**
   * Change stop for the display trip.
   */
  public void changeStop(Pair<ServiceStop, Boolean> args) {
    onChangeStop.onNext(args);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeLong(id);
    out.writeLong(displayTripId);
    out.writeList(trips);
    out.writeInt(frequency);
    out.writeParcelable(query, 0);
  }

  public Observable<Pair<ServiceStop, Boolean>> onChangeStop() {
    return onChangeStop;
  }

  public GroupVisibility getVisibility() {
    return visibility;
  }

  public void setVisibility(@NonNull GroupVisibility visibility) {
    this.visibility = visibility;
  }
}