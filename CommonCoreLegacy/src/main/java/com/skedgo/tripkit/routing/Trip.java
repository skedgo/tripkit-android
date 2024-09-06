package com.skedgo.tripkit.routing;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.ITimeRange;
import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.TransportMode;

import org.joda.time.format.ISODateTimeFormat;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A {@link Trip} will mainly hold a list of {@link TripSegment}s which denotes
 * how to go from {@link Trip#getFrom()} to {@link Trip#getTo()}.
 * <p>
 * Main use-cases:
 * - Trip's segments: {@link Trip#getSegments()}.
 * - Trip's start time: {@link TripExtensionsKt#getStartDateTime(Trip)}.
 * - Trip's end time: {@link TripExtensionsKt#getEndDateTime(Trip)}}.
 * - Trip's costs: {@link #getCaloriesCost()}, {@link #getMoneyCost()}, {@link #getCarbonCost()}.
 */
public class Trip implements ITimeRange {
    public static final float UNKNOWN_COST = -9999.9999F;

    /**
     * This will be transformed into a list of {@link TripSegment}.
     */
    @SerializedName("segments")
    public ArrayList<JsonObject> rawSegmentList;
    @SerializedName("currencySymbol")
    private String currencySymbol;
    @SerializedName("saveURL")
    private String saveURL;
    @SerializedName("depart")
    private String depart;
    @SerializedName("arrive")
    private String arrive;
    @SerializedName("caloriesCost")
    private float caloriesCost;
    @SerializedName("moneyCost")
    private float mMoneyCost;
    @SerializedName("moneyUSDCost")
    private float moneyUsdCost;
    @SerializedName("carbonCost")
    private float mCarbonCost;
    @SerializedName("hassleCost")
    private float mHassleCost;
    @SerializedName("weightedScore")
    private float weightedScore;
    @SerializedName("updateURL")
    private String updateURL;
    @SerializedName("progressURL")
    private String progressURL;
    @SerializedName("plannedURL")
    private String plannedURL;
    @SerializedName("temporaryURL")
    private String temporaryURL;
    @SerializedName("logURL")
    private String logURL;
    @SerializedName("shareURL")
    private String shareURL;

    @SerializedName("subscribeURL")
    private String subscribeURL;

    @SerializedName("unsubscribeURL")
    private String unsubscribeURL;

    private long mStartTimeInSecs;
    private long mEndTimeInSecs;

    @Nullable
    @SerializedName("availability")
    private String availability;
    @Nullable
    private String availabilityInfo;
    @SerializedName("mainSegmentHashCode")
    private long mainSegmentHashCode;
    @SerializedName("hideExactTimes")
    private boolean hideExactTimes;
    @SerializedName("queryIsLeaveAfter")
    private boolean queryIsLeaveAfter;
    @SerializedName("queryTime")
    private long queryTime;

    private String uuid = UUID.randomUUID().toString();
    private long mId;
    private transient TripGroup mGroup;
    private boolean mIsFavourite;
    private ArrayList<TripSegment> mSegments;

    public Trip() {
        mStartTimeInSecs = 0;
        mEndTimeInSecs = 0;
        mMoneyCost = UNKNOWN_COST;
        moneyUsdCost = UNKNOWN_COST;
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
    public long getStartTimeInSecs() {
        if (mStartTimeInSecs > 0) {
            return mStartTimeInSecs;
        }

        long millis = -1L;
        try {
            millis = Long.parseLong(depart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (millis < 0 && depart != null) {
            millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(depart).getMillis();
        }

        if (depart == null) {
            millis = 0;
        }
        return millis;
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
    public long getEndTimeInSecs() {
        if (mEndTimeInSecs > 0) {
            return mEndTimeInSecs;
        }

        long millis = -1L;
        try {
            millis = Long.parseLong(arrive);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (millis < 0 && arrive != null) {
            millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(arrive).getMillis();
        }

        if (arrive == null) {
            millis = 0;
        }
        return millis;
    }

    /**
     * NOTE: You should only use this setter for testing purpose.
     */
    public void setEndTimeInSecs(final long endTimeInSecs) {
        this.mEndTimeInSecs = endTimeInSecs;
    }

    public long durationInSeconds() {
        return mEndTimeInSecs - mStartTimeInSecs;
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

    public float getMoneyUsdCost() {
        return moneyUsdCost;
    }

    public void setMoneyUsdCost(float moneyUsdCost) {
        this.moneyUsdCost = moneyUsdCost;
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

    @Nullable
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

    @Nullable
    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

    @Nullable
    public String getLogURL() {
        return logURL;
    }

    public void setLogURL(String logURL) {
        this.logURL = logURL;
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

    @Nullable
    public String getTemporaryURL() {
        return temporaryURL;
    }

    public void setTemporaryURL(String temporaryURL) {
        this.temporaryURL = temporaryURL;
    }

    /**
     * Indicates availability of the trip, e.g., if it's too late to book a trip for the requested
     * departure time, or if a scheduled service has been cancelled.
     */
    @Nullable
    public Availability getAvailability() {
        return com.skedgo.tripkit.routing.AvailabilityKt.toAvailability(availability);
    }

    /**
     * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
     */
    @Deprecated
    public void setAvailability(@NonNull Availability availability) {
        this.availability = availability.getValue();
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Nullable
    public String getAvailabilityString() {
        return availability;
    }

    public boolean queryIsLeaveAfter() {
        return queryIsLeaveAfter;
    }

    /**
     * Mutability is subject to deletion after we finish migrating to an immutable {@link Trip}.
     */
    public void setQueryIsLeaveAfter(boolean queryIsLeaveAfter) {
        this.queryIsLeaveAfter = queryIsLeaveAfter;
    }

    @Nullable
    public String getPlannedURL() {
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

    @Nullable
    public TripSegment getQuickBookingSegment() {
        if (mSegments == null) {
            return null;
        }

        for (TripSegment segment : mSegments) {
            if (segment.isQuickBooking()) {
                return segment;
            }
        }
        return null;
    }

    @Nullable
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

    @Nullable
    public String getDisplayCostUsd() {
        if (moneyUsdCost == 0) {
            return null;
        } else if (moneyUsdCost == Trip.UNKNOWN_COST) {
            return null;
        } else {
            // Use locale.
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            numberFormat.setRoundingMode(RoundingMode.CEILING);
            numberFormat.setMaximumFractionDigits(0);
            String value = numberFormat.format(moneyUsdCost);
            return (currencySymbol != null ? currencySymbol : "$") + value;
        }
    }

    @Nullable
    public String getDisplayCarbonCost() {
        if (mCarbonCost > 0) {
            return mCarbonCost + "kg";
        } else {
            return null;
        }
    }

    public String getDisplayCalories() {
        return caloriesCost + " kcal";
    }

    public boolean isMixedModal(boolean ignoreWalking) {
        String previousMode = "";
        for (TripSegment segment : mSegments) {
            if (segment.getModeInfo() == null || segment.getModeInfo().getId() == null) {
                continue;
            }

            String currentMode = segment.getModeInfo().getId();
            if (segment.getType() == SegmentType.STATIONARY || currentMode.isEmpty()) {
                continue;
            }

            if ((segment.isWalking() && ignoreWalking) || !segment.getVisibility().equals(Visibilities.VISIBILITY_IN_SUMMARY)) {
                continue;
            }
            if (!previousMode.isEmpty() && !currentMode.equals(previousMode)) {
                return true;
            }
            previousMode = currentMode;
        }

        return false;
    }

    public Long getMainSegmentHashCode() {
        return mainSegmentHashCode;
    }

    public void setMainSegmentHashCode(long mainSegmentHashCode) {
        this.mainSegmentHashCode = mainSegmentHashCode;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    @Nullable
    public String getSubscribeURL() {
        return subscribeURL;
    }

    public void setSubscribeURL(String subscribeURL) {
        this.subscribeURL = subscribeURL;
    }

    @Nullable
    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }

    public boolean isHideExactTimes() {
        return hideExactTimes;
    }

    public void setHideExactTimes(boolean hideExactTimes) {
        this.hideExactTimes = hideExactTimes;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    @Nullable
    public String getAvailabilityInfo() {
        return availabilityInfo;
    }

    public void setAvailabilityInfo(@Nullable String availabilityInfo) {
        this.availabilityInfo = availabilityInfo;
    }

    public String getTripUuid() {
        if (saveURL != null) {
            Uri uri = Uri.parse(saveURL);
            String uUid = uri.getLastPathSegment();
            return uUid == null ? saveURL : uUid;
        } else {
            return uuid();
        }
    }
}