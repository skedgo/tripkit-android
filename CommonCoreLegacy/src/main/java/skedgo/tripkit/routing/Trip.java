package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.model.ITimeRange;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TransportMode;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Main_API_formats#Trips">API format</a>
 */
public class Trip implements ITimeRange {
  public static final float UNKNOWN_COST = -9999.9999F;

  /**
   * This will be transformed into a list of {@link TripSegment}.
   */
  @SerializedName("segments") public ArrayList<JsonObject> rawSegmentList;
  @SerializedName("currencySymbol") private String currencySymbol;
  @SerializedName("saveURL") private String saveURL;
  @SerializedName("depart") private long mStartTimeInSecs;
  @SerializedName("arrive") private long mEndTimeInSecs;
  @SerializedName("caloriesCost") private float caloriesCost;
  @SerializedName("moneyCost") private float mMoneyCost;
  @SerializedName("carbonCost") private float mCarbonCost;
  @SerializedName("hassleCost") private float mHassleCost;
  @SerializedName("weightedScore") private float weightedScore;
  @SerializedName("updateURL") private String updateURL;
  @SerializedName("progressURL") private String progressURL;
  @SerializedName("plannedURL") private String plannedURL;
  @SerializedName("temporaryURL") private String temporaryURL;
  @Nullable @SerializedName("availability") private String availability;
  private boolean queryIsLeaveAfter;
  private String uuid = UUID.randomUUID().toString();
  private long mId;
  private transient TripGroup mGroup;
  private boolean mIsFavourite;
  private ArrayList<TripSegment> mSegments;

  public Trip() {
    mStartTimeInSecs = 0;
    mEndTimeInSecs = 0;
    mMoneyCost = UNKNOWN_COST;
    mCarbonCost = 0;
    mHassleCost = 0;
  }

  public void uuid(String uuid) {
    this.uuid = uuid;
  }

  public String uuid() {
    return uuid;
  }

  public long getId() {
    return mId;
  }

  public void setId(final long id) {
    this.mId = id;
  }

  public TripGroup getGroup() {
    return mGroup;
  }

  public void setGroup(TripGroup group) {
    this.mGroup = group;
  }

  /**
   * Use {@link TripExtensionsKt#getStartDateTime(Trip)} instead.
   */
  @Deprecated
  public long getStartTimeInSecs() {
    return mStartTimeInSecs;
  }

  /**
   * NOTE: You should only use this setter for testing purpose.
   */
  public void setStartTimeInSecs(long startTimeInSecs) {
    mStartTimeInSecs = startTimeInSecs;
  }

  /**
   * Use {@link TripExtensionsKt#getEndDateTime(Trip)} instead.
   */
  @Deprecated
  public long getEndTimeInSecs() {
    return mEndTimeInSecs;
  }

  /**
   * NOTE: You should only use this setter for testing purpose.
   */
  public void setEndTimeInSecs(final long endTimeInSecs) {
    this.mEndTimeInSecs = endTimeInSecs;
  }

  public float getTimeCost() {
    return getEndTimeInSecs() - getStartTimeInSecs();
  }

  public float getMoneyCost() {
    return mMoneyCost;
  }

  public void setMoneyCost(final float moneyCost) {
    this.mMoneyCost = moneyCost;
  }

  public float getCarbonCost() {
    return mCarbonCost;
  }

  public void setCarbonCost(final float carbonCost) {
    this.mCarbonCost = carbonCost;
  }

  public float getHassleCost() {
    return mHassleCost;
  }

  public void setHassleCost(final float hassleCost) {
    this.mHassleCost = hassleCost;
  }

  public Location getTo() {
    if (mSegments == null || mSegments.isEmpty()) {
      return null;
    }

    TripSegment lastSeg = mSegments.get(mSegments.size() - 1);
    if (lastSeg == null) {
      return null;
    } else if (lastSeg.getTo() != null) {
      return lastSeg.getTo();
    } else {
      return lastSeg.getSingleLocation();
    }
  }

  public Location getFrom() {
    if (mSegments == null || mSegments.isEmpty()) {
      return null;
    }

    TripSegment firstSeg = mSegments.get(0);

    if (firstSeg == null) {
      return null;
    } else if (firstSeg.getFrom() != null) {
      return firstSeg.getFrom();
    } else {
      return firstSeg.getSingleLocation();
    }
  }

  public ArrayList<TripSegment> getSegments() {
    return mSegments;
  }

  public void setSegments(final ArrayList<TripSegment> segments) {
    this.mSegments = segments;
    if (mSegments != null) {
      for (TripSegment seg : mSegments) {
        seg.setTrip(this);
      }
    }
  }

  public boolean isFavourite() {
    return mIsFavourite;
  }

  public void isFavourite(final boolean isFavourite) {
    mIsFavourite = isFavourite;
  }

  public String getSaveURL() {
    return saveURL;
  }

  public void setSaveURL(String saveURL) {
    this.saveURL = saveURL;
  }

  @Nullable public String getUpdateURL() {
    return updateURL;
  }

  public void setUpdateURL(String updateURL) {
    this.updateURL = updateURL;
  }

  public float getWeightedScore() {
    return weightedScore;
  }

  public void setWeightedScore(float weightedScore) {
    this.weightedScore = weightedScore;
  }

  public float getCaloriesCost() {
    return caloriesCost;
  }

  public void setCaloriesCost(float caloriesCost) {
    this.caloriesCost = caloriesCost;
  }

  @Nullable public String getTemporaryURL() {
    return temporaryURL;
  }

  public void setTemporaryURL(String temporaryURL) {
    this.temporaryURL = temporaryURL;
  }

  /**
   * Indicates availability of the trip, e.g., if it's too late to book a trip for the requested
   * departure time, or if a scheduled service has been cancelled.
   */
  @Nullable public Availability getAvailability() {
    return skedgo.tripkit.routing.AvailabilityKt.toAvailability(availability);
  }

  /**
   * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
   */
  @Deprecated
  public void setAvailability(@NonNull Availability availability) {
    this.availability = availability.getValue();
  }

  public boolean queryIsLeaveAfter() {
    return queryIsLeaveAfter;
  }

  /**
   * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
   */
  @Deprecated
  public void setQueryIsLeaveAfter(boolean queryIsLeaveAfter) {
    this.queryIsLeaveAfter = queryIsLeaveAfter;
  }

  @Nullable public String getPlannedURL() {
    return plannedURL;
  }

  /**
   * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
   */
  @Deprecated
  public void setPlannedURL(String plannedURL) {
    this.plannedURL = plannedURL;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  /**
   * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
   */
  @Deprecated
  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getProgressURL() {
    return progressURL;
  }

  /**
   * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
   */
  @Deprecated
  public void setProgressURL(String progressURL) {
    this.progressURL = progressURL;
  }

  public boolean hasTransportMode(VehicleMode... modes) {
    if (mSegments == null || mSegments.isEmpty() || modes == null) {
      return false;
    }

    for (TripSegment seg : mSegments) {
      for (VehicleMode mode : modes) {
        if (seg.getMode() == mode) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Adrian: "duration (arrive)" should be used for transport where
   * the departure time isn't fixed, such as driving trips not involving public transport,
   * or public transport trips that use only frequency-based trips.
   */
  public boolean isDepartureTimeFixed() {
    boolean isDepartureTimeFixed = false;
    if (mSegments != null) {
      boolean hasPublicTransportSegment = false;
      boolean hasNonFrequencyBasedSegment = false;

      for (TripSegment segment : mSegments) {
        // Check if there is any Public Transport segment.
        if (!TextUtils.isEmpty(segment.getServiceTripId())) {
          hasPublicTransportSegment = true;

          if (segment.getFrequency() == 0) {
            hasNonFrequencyBasedSegment = true;
          }
        }
      }

      isDepartureTimeFixed = hasPublicTransportSegment && hasNonFrequencyBasedSegment;
    }

    return isDepartureTimeFixed;
  }

  public boolean hasAnyPublicTransport() {
    if (mSegments != null) {
      for (TripSegment segment : mSegments) {
        // Check if there is any Public Transport segment.
        if (!TextUtils.isEmpty(segment.getServiceTripId())) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Check if this trip has shuffle, taxi or shared vehicle.
   */
  public boolean hasAnyExpensiveTransport() {
    if (mSegments == null) {
      return false;
    }

    for (TripSegment segment : mSegments) {
      if (segment.getTransportModeId() != null) {
        switch (segment.getTransportModeId()) {
          case TransportMode.ID_AIR:
          case TransportMode.ID_SHUFFLE:
          case TransportMode.ID_TAXI:
          case TransportMode.ID_TNC:
            return true;
        }

        // Check shared car or shared bicycle.
        if (segment.getTransportModeId().contains(TransportMode.MIDDLE_FIX_CAR)
            || segment.getTransportModeId().contains(TransportMode.MIDDLE_FIX_BIC)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean hasQuickBooking() {
    if (mSegments == null) {
      return false;
    }

    for (TripSegment segment : mSegments) {
      if (segment.getBooking() != null && segment.getBooking().getQuickBookingsUrl() != null) {
        return true;
      }
    }
    return false;
  }

  public String getDisplayCost(String localizedFreeText) {
    if (mMoneyCost == 0) {
      return localizedFreeText;
    } else if (mMoneyCost == Trip.UNKNOWN_COST) {
      return null;
    } else {
      // Use locale.
      NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
      numberFormat.setRoundingMode(RoundingMode.CEILING);
      numberFormat.setMaximumFractionDigits(0);
      String value = numberFormat.format(mMoneyCost);
      return (currencySymbol != null ? currencySymbol : "$") + value;
    }
  }
}