package com.skedgo.android.common.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.util.Gsons;
import com.skedgo.android.common.util.ListUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.skedgo.android.common.util.LogUtils.LOGE;
import static com.skedgo.android.common.util.LogUtils.makeTag;
import static com.skedgo.android.common.util.Preconditions.checkMainThread;
import static com.skedgo.android.common.util.Preconditions.checkWorkerThread;

/**
 * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Main_API_formats#Trips">API format</a>
 */
public class Trip implements Parcelable, ITimeRange {
  public static final float UNKNOWN_COST = -9999.9999F;
  private static final String TAG = makeTag("3931_realtime");

  // FIXME: Release this raw JSON as soon as we no longer use it.
  @SerializedName("segments") public ArrayList<JsonObject> rawSegmentList;
  @SerializedName("currencySymbol") private String currencySymbol;
  @SerializedName("saveURL") private String saveURL;
  @SerializedName("depart") private long mStartTimeInSecs;
  @SerializedName("arrive") private long mEndTimeInSecs;
  @SerializedName("caloriesCost") private float caloriesCost;
  @SerializedName("moneyCost") private float mMoneyCost;
  @SerializedName("carbonCost") private float mCarbonCost;
  @SerializedName("hassleCost") private float mHassleCost;
  @SerializedName("temporaryId") private String mTemporaryId;
  @SerializedName("weightedScore") private float weightedScore;
  @SerializedName("updateURL") private String updateURL;
  @SerializedName("progressURL") private String progressURL;
  @SerializedName("plannedURL") private String plannedURL;
  @SerializedName("temporaryURL") private String temporaryURL;

  private long mId;
  private transient TripGroup mGroup;
  private boolean mIsFavourite;
  private ArrayList<TripSegment> mSegments;

  public static final Creator<Trip> CREATOR = new Creator<Trip>() {
    public Trip createFromParcel(Parcel in) {
      Trip trip = new Trip();

      trip.mId = in.readLong();
      trip.mStartTimeInSecs = in.readLong();
      trip.mEndTimeInSecs = in.readLong();
      trip.mMoneyCost = in.readFloat();
      trip.mCarbonCost = in.readFloat();
      trip.mHassleCost = in.readFloat();
      trip.mSegments = in.readArrayList(TripSegment.class.getClassLoader());

      if (trip.mSegments != null) {
        for (TripSegment segment : trip.mSegments) {
          segment.setTrip(trip);
        }
      }

      trip.mIsFavourite = in.readInt() == 1;
      trip.mTemporaryId = in.readString();
      trip.saveURL = in.readString();
      trip.updateURL = in.readString();
      trip.progressURL = in.readString();
      trip.weightedScore = in.readFloat();
      trip.currencySymbol = in.readString();
      trip.caloriesCost = in.readFloat();
      trip.plannedURL = in.readString();
      trip.temporaryURL = in.readString();
      return trip;
    }

    public Trip[] newArray(int size) {
      return new Trip[size];
    }
  };

  public Trip() {
    mStartTimeInSecs = 0;
    mEndTimeInSecs = 0;
    mMoneyCost = UNKNOWN_COST;
    mCarbonCost = 0;
    mHassleCost = 0;
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

  public long getStartTimeInSecs() {
    return mStartTimeInSecs;
  }

  public void setStartTimeInSecs(long startTimeInSecs) {
    mStartTimeInSecs = startTimeInSecs;
  }

  public long getEndTimeInSecs() {
    return mEndTimeInSecs;
  }

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

  public String getTemporaryId() {
    return mTemporaryId;
  }

  public void setTemporaryId(final String temporaryId) {
    mTemporaryId = temporaryId;
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

  @Nullable public String getTemporaryURL() {
    return temporaryURL;
  }

  void setCaloriesCost(float caloriesCost) {
    this.caloriesCost = caloriesCost;
  }

  @Nullable public String getPlannedURL() {
    return plannedURL;
  }

  public void setPlannedURL(String plannedURL) {
    this.plannedURL = plannedURL;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getProgressURL() {
    return progressURL;
  }

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeLong(mId);
    dest.writeLong(mStartTimeInSecs);
    dest.writeLong(mEndTimeInSecs);
    dest.writeFloat(mMoneyCost);
    dest.writeFloat(mCarbonCost);
    dest.writeFloat(mHassleCost);
    dest.writeList(mSegments);
    dest.writeInt(mIsFavourite ? 1 : 0);
    dest.writeString(mTemporaryId);
    dest.writeString(saveURL);
    dest.writeString(updateURL);
    dest.writeString(progressURL);
    dest.writeFloat(weightedScore);
    dest.writeString(currencySymbol);
    dest.writeFloat(caloriesCost);
    dest.writeString(plannedURL);
    dest.writeString(temporaryURL);
  }

  public ArrayList<String> getTripModes() {
    if (ListUtils.isEmpty(mSegments)) {
      return null;
    }

    ArrayList<String> modes = new ArrayList<String>();
    for (TripSegment segment : mSegments) {
      String mode = segment.getTransportModeId();
      if (!TextUtils.isEmpty(mode)) {
        modes.add(mode);
      }
    }

    return modes;
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

  /**
   * TODO: Need a test.
   */
  public List<TripSegment> getVisibleSegmentsInSummary() {
    List<TripSegment> visibleSegments = new ArrayList<>();
    if (ListUtils.isEmpty(mSegments)) {
      return visibleSegments;
    }

    for (TripSegment segment : mSegments) {
      if (segment.isVisibleInContext(TripSegment.VISIBILITY_IN_SUMMARY)) {
        visibleSegments.add(segment);
      }
    }

    return visibleSegments;
  }

  /**
   * @param timeUnit Mins, secs or millis.
   */
  public Observable<Trip> updateTripPeriodically(@NonNull final Resources resources, @NonNull final OkHttpClient httpClient,
                                                 long period,
                                                 TimeUnit timeUnit) {
    return Observable.timer(period, period, timeUnit)
        .filter(new Func1<Long, Boolean>() {
          @Override
          public Boolean call(Long unused) {
            return !TextUtils.isEmpty(updateURL);
          }
        })
        .map(new Func1<Long, RoutingResponse>() {
          @Override
          public RoutingResponse call(Long unused) {
            try {
              return fetchUpdate(resources, httpClient);
            } catch (Exception e) {
              LOGE(TAG, e.getMessage(), e);
              return null;
            }
          }
        })
                /* To avoid unexpected bad data. */
        .filter(new Func1<RoutingResponse, Boolean>() {
          @Override
          public Boolean call(RoutingResponse response) {
            return response != null
                && !response.hasError()
                && !ListUtils.isEmpty(response.getTripGroupList())
                && !ListUtils.isEmpty(response.getTripGroupList().get(0).getTrips())
                && response.getTripGroupList().get(0).getTrips().get(0) != null;
          }
        })
                /* Extract update. */
        .map(new Func1<RoutingResponse, Trip>() {
          @Override
          public Trip call(RoutingResponse response) {
            return response.getTripGroupList().get(0).getTrips().get(0);
          }
        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Action1<Trip>() {
          @Override
          public void call(Trip update) {
            checkMainThread();
            updateTrip(update);
          }
        });
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

  /**
   * @see <a href="https://github.com/skedgo/tripgo-ios/blob/develop/TripPlanner/Classes/BHBuzzRoutingParser.m#L429">iOS impl</a>
   */
  void updateTrip(@NonNull Trip tripUpdate) {
    setStartTimeInSecs(tripUpdate.getStartTimeInSecs());
    setEndTimeInSecs(tripUpdate.getEndTimeInSecs());
    setSaveURL(tripUpdate.getSaveURL());
    setUpdateURL(tripUpdate.getUpdateURL());
    setProgressURL(tripUpdate.getProgressURL());
    setCarbonCost(tripUpdate.getCarbonCost());
    setMoneyCost(tripUpdate.getMoneyCost());
    setHassleCost(tripUpdate.getHassleCost());

    CollectionUtils.forAllDo(tripUpdate.getSegments(), new Closure<TripSegment>() {
      @Override
      public void execute(final TripSegment segmentUpdate) {
        TripSegment segmentToUpdate = CollectionUtils.find(mSegments, new Predicate<TripSegment>() {
          @Override
          public boolean evaluate(TripSegment object) {
            // Times in both departure and arrival segments are bound
            // tightly to the first segment and the final segment respectively.
            // If we update times for first segment and the final segment,
            // the times in arrival and departure segments will be updated accordingly.
            // Hence we filter them out.
            // See: https://redmine.buzzhives.com/issues/4397.
            return object.getType() != SegmentType.ARRIVAL
                && object.getType() != SegmentType.DEPARTURE
                && object.getTemplateHashCode() == segmentUpdate.getTemplateHashCode();
          }
        });
        if (segmentToUpdate != null) {
          segmentToUpdate.setStartTimeInSecs(segmentUpdate.getStartTimeInSecs());
          segmentToUpdate.setEndTimeInSecs(segmentUpdate.getEndTimeInSecs());
          segmentToUpdate.setRealTime(segmentUpdate.isRealTime());
          segmentToUpdate.setAlerts(segmentUpdate.getAlerts());
          segmentToUpdate.setRealTimeVehicle(segmentUpdate.getRealTimeVehicle());

          // FIXME: Just ditch the old trip when receiving realtime data.
          // See more https://www.flowdock.com/app/skedgo/tripgo-v4/threads/WhEA69BC4SQNO2f7qcPHUw2kQNq.
        }
      }
    });
  }

  RoutingResponse fetchUpdate(@NonNull Resources resources, @NonNull OkHttpClient httpClient) throws IOException {
    checkWorkerThread();

    // FIXME: It's a hack to receive correct 'realTime' from server.
    String url = updateURL + "&v=10";
    Request request = new Request.Builder()
        .url(url)
        .build();

    Response response = httpClient.newCall(request).execute();
    if (!response.isSuccessful()) {
      throw new IOException("Unexpected code " + response);
    }

    ResponseBody responseBody = null;
    try {
      responseBody = response.body();

      Gson gson = Gsons.createForLowercaseEnum();
      RoutingResponse routingResponse = gson.fromJson(responseBody.charStream(), RoutingResponse.class);
      if (routingResponse != null) {
        routingResponse.processRawData(resources, gson);
      }

      return routingResponse;
    } catch (IOException e) {
      throw new IOException("Unexpected error " + response);
    } finally {
      Util.closeQuietly(responseBody);
    }
  }

  public static class Comparators {
    public static final Comparator<Trip> START_TIME_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return compareLongs(
                lhs.getStartTimeInSecs(),
                rhs.getStartTimeInSecs()
            );
          }
        });

    public static final Comparator<Trip> END_TIME_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return compareLongs(
                lhs.getEndTimeInSecs(),
                rhs.getEndTimeInSecs()
            );
          }
        });

    public static final Comparator<Trip> TIME_COMPARATOR_CHAIN =
        new ComparatorChain<>(Arrays.asList(
            START_TIME_COMPARATOR,
            END_TIME_COMPARATOR
        ));

    public static final Comparator<Trip> WEIGHTED_SCORE_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return Float.compare(lhs.getWeightedScore(), rhs.getWeightedScore());
          }
        });

    public static final Comparator<Trip> DURATION_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return Float.compare(lhs.getTimeCost(), rhs.getTimeCost());
          }
        });

    public static final Comparator<Trip> MONEY_COST_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return Float.compare(lhs.getMoneyCost(), rhs.getMoneyCost());
          }
        });

    public static final Comparator<Trip> CARBON_COST_COMPARATOR =
        ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
          @Override
          public int compare(Trip lhs, Trip rhs) {
            return Float.compare(lhs.getCarbonCost(), rhs.getCarbonCost());
          }
        });

    /**
     * FIXME: Possibly a duplicate.
     * This was copied from Long.compare() which only available for API 19+.
     */
    public static int compareLongs(long lhs, long rhs) {
      return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }
  }
}