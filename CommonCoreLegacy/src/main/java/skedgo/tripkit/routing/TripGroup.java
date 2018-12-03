package skedgo.tripkit.routing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Represents a list of {@link Trip}s. A list of {@link Trip}s comprises
 * of a display trip (aka representative trip) and alternative trips.
 * A display trip can be accessed via {@link #getDisplayTrip()} while
 * alternative trips can be retrieved via {@link #getTrips()} minus
 * {@link #getDisplayTrip()}. That's because {@link #getTrips()} returns
 * a list of {@link Trip}s including alternative trips and display trip.
 * <p>
 * Besides, a {@link TripGroup} also hold info related to {@link Source}
 * via {@link #getSources()}.
 */
public class TripGroup {

  private String uuid = UUID.randomUUID().toString();
  private long displayTripId;

  @SerializedName("sources") @Nullable private List<Source> sources;
  @SerializedName("trips") private ArrayList<Trip> trips;
  @SerializedName("frequency") private int frequency;
  private transient GroupVisibility visibility = GroupVisibility.FULL;

  public long getDisplayTripId() {
    return displayTripId;
  }

  public void setDisplayTripId(long displayTripId) {
    this.displayTripId = displayTripId;
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

  public GroupVisibility getVisibility() {
    return visibility;
  }

  public void setVisibility(@NonNull GroupVisibility visibility) {
    this.visibility = visibility;
  }

  public void uuid(String uuid) {
    this.uuid = uuid;
  }

  public String uuid() {
    return uuid;
  }

  @Nullable public List<Source> getSources() {
    return sources;
  }

  public void setSources(@Nullable List<Source> sources) {
    this.sources = sources;
  }
}
