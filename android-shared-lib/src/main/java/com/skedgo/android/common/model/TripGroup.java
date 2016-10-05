package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.rx.Var;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.ArrayList;
import java.util.Arrays;
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
      tripGroup.queryId = in.readLong();
      tripGroup.displayTripId = in.readLong();
      tripGroup.trips = in.readArrayList(Trip.class.getClassLoader());

      if (tripGroup.trips != null) {
        for (Trip trip : tripGroup.trips) {
          trip.setGroup(tripGroup);
        }
      }

      tripGroup.frequency = in.readInt();
      tripGroup.query = in.readParcelable(Query.class.getClassLoader());
      tripGroup.visibility.put(Visibility.valueOf(in.readString()));
      return tripGroup;
    }

    public TripGroup[] newArray(int size) {
      return new TripGroup[size];
    }
  };
  private long id;
  private long displayTripId;
  private long queryId;
  private Query query;

  @SerializedName("trips") private ArrayList<Trip> trips;
  @SerializedName("frequency") private int frequency;

  private transient Var<Visibility> visibility = Var.create(Visibility.FULL);
  private transient PublishSubject<Pair<ServiceStop, Boolean>> onChangeStop = PublishSubject.create();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getQueryId() {
    return queryId;
  }

  public void setQueryId(long queryId) {
    this.queryId = queryId;
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
    out.writeLong(queryId);
    out.writeLong(displayTripId);
    out.writeList(trips);
    out.writeInt(frequency);
    out.writeParcelable(query, 0);
    out.writeString(visibility.value().name());
  }

  public Var<Visibility> visibility() {
    return visibility;
  }

  public Observable<Pair<ServiceStop, Boolean>> onChangeStop() {
    return onChangeStop;
  }

  public enum Visibility {
    FULL(1), COMPACT(0), GONE(-1);

    /**
     * To be sortable.
     */
    public final int value;

    Visibility(int value) {
      this.value = value;
    }
  }

  public final static class Comparators {
    public static final Transformer<TripGroup, Trip> DISPLAY_TRIP_TRANSFORMER =
        new Transformer<TripGroup, Trip>() {
          @Override
          public Trip transform(TripGroup group) {
            return group.getDisplayTrip();
          }
        };

    /**
     * @see <a href="https://redmine.buzzhives.com/issues/3967">Why deprecated?</a>
     */
    @Deprecated
    public static final Comparator<TripGroup> CARBON_COST_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.CARBON_COST_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> MONEY_COST_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.MONEY_COST_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> WEIGHTED_SCORE_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.WEIGHTED_SCORE_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> DURATION_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.DURATION_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> END_TIME_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.END_TIME_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> START_TIME_COMPARATOR =
        ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
            TripComparators.START_TIME_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        ));

    public static final Comparator<TripGroup> DESC_VISIBILITY_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<TripGroup>() {
          @Override
          public int compare(TripGroup lhs, TripGroup rhs) {
            // By descendant.
            return rhs.visibility().value().value - lhs.visibility().value().value;
          }
        });

    /**
     * To sort routes by arrive-by query.
     * <p/>
     * Q: Why reverse departure time?
     * A: "If it's an arrive-by query and you sort by time,
     * you don't care when the trips arrive as you told them
     * when they should arrive. What matters is when they leave.
     * Trips that leave later (while arriving before the time you selected) are better.
     * Hence: sort descending by arrival time." (Adrian said)
     *
     * @see <a href="https://redmine.buzzhives.com/issues/3967">Discussion</a>
     */
    public static final Comparator<TripGroup> DEPARTURE_COMPARATOR_CHAIN =
        new ComparatorChain<>(Arrays.asList(
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
            END_TIME_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR
        ));

    /**
     * To sort routes by leave-after query.
     * <p/>
     * "In arrive-by query it's better the later they depart.
     * In a leave-after query it's better the earlier that they arrive." (Adrian said)
     *
     * @see <a href="https://redmine.buzzhives.com/issues/3967">Discussion</a>
     */
    public static final Comparator<TripGroup> ARRIVAL_COMPARATOR_CHAIN =
        new ComparatorChain<>(Arrays.asList(
            END_TIME_COMPARATOR,
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
            DESC_VISIBILITY_COMPARATOR
        ));

    private Comparators() {}

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    public static Comparator<TripGroup> createPreferredComparatorChain(boolean willArriveBy) {
      if (willArriveBy) {
        return new ComparatorChain<>(Arrays.asList(
            DESC_VISIBILITY_COMPARATOR,
            WEIGHTED_SCORE_COMPARATOR,
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
        ));
      } else {
        return new ComparatorChain<>(Arrays.asList(
            DESC_VISIBILITY_COMPARATOR,
            WEIGHTED_SCORE_COMPARATOR,
            END_TIME_COMPARATOR
        ));
      }
    }

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    public static Comparator<TripGroup> createDurationComparatorChain(boolean willArriveBy) {
      if (willArriveBy) {
        return new ComparatorChain<>(Arrays.asList(
            DURATION_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR,
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
        ));
      } else {
        return new ComparatorChain<>(Arrays.asList(
            DURATION_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR,
            END_TIME_COMPARATOR
        ));
      }
    }

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    public static Comparator<TripGroup> createPriceComparatorChain(boolean willArriveBy) {
      if (willArriveBy) {
        return new ComparatorChain<>(Arrays.asList(
            MONEY_COST_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR,
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
        ));
      } else {
        return new ComparatorChain<>(Arrays.asList(
            MONEY_COST_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR,
            END_TIME_COMPARATOR
        ));
      }
    }
  }
}