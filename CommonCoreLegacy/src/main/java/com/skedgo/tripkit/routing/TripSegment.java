package com.skedgo.tripkit.routing;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.TransactionTooLargeException;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.R;
import com.skedgo.tripkit.common.StyleManager;
import com.skedgo.tripkit.common.agenda.IRealTimeElement;
import com.skedgo.tripkit.common.model.Booking;
import com.skedgo.tripkit.common.model.ITimeRange;
import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.MiniInstruction;
import com.skedgo.tripkit.common.model.Query;
import com.skedgo.tripkit.common.model.RealtimeAlert;
import com.skedgo.tripkit.common.model.SharedVehicle;
import com.skedgo.tripkit.common.model.Street;
import com.skedgo.tripkit.common.model.TransportMode;
import com.skedgo.tripkit.common.util.TimeUtils;
import com.skedgo.tripkit.common.util.TripSegmentUtils;

import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.skedgo.tripkit.routing.VehicleDrawables.createLightDrawable;

/**
 * To go from A to B, sometimes we have to travel X, Y, Z locations between A and B.
 * That means, we travel A to X, then X to Y, then Y to Z, then Z to B which is the destination.
 * To show how to travel from A to X, we use {@link TripSegment}. So, in this case,
 * a trip from A to B comprises 6 segments:
 * - A segment whose type is {@link SegmentType#DEPARTURE}.
 * - A segment from A to X.
 * - A segment from X to Y.
 * - A segment from Y to Z.
 * - A segment from Z to B.
 * - A segment whose type is {@link SegmentType#ARRIVAL}.
 * <p>
 * Note that, to avoid {@link TransactionTooLargeException}, it's discouraged to
 * pass any instance of {@link Query} to {@link Intent} or {@link Bundle}.
 * The {@link Parcelable} is subject to deletion at anytime.
 *
 * @see <a href="http://skedgo.github.io/tripgo-api/site/faq/#trips-groups-frequencies-and-templates">Trips, groups, frequencies and templates</a>
 */
public class TripSegment implements IRealTimeElement, ITimeRange {

    private long mId;
    private transient Trip mTrip;
    @SerializedName("booking")
    private Booking booking;
    @SerializedName("bookingHashCode")
    @Nullable
    private Long bookingHashCode;
    @SerializedName("mini")
    @Nullable
    private MiniInstruction mMiniInstruction;
    @SerializedName("sharedVehicle")
    @Nullable
    private SharedVehicle sharedVehicle;
    @SerializedName("modeInfo")
    private ModeInfo modeInfo;
    @SerializedName("type")
    private String type;
    private SegmentType mType;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;
    @SerializedName("visibility")
    private String mVisibility;
    @SerializedName("from")
    private Location mFrom;
    @SerializedName("to")
    private Location mTo;
    @SerializedName("location")
    private Location mSingleLocation;
    @SerializedName("action")
    private String mAction;
    @SerializedName("travelDirection")
    private int mDirection;
    @SerializedName("notes")
    private String mNotes;
    @SerializedName("serviceIsFrequencyBased")
    private boolean mIsFrequencyBased;
    @SerializedName("frequency")
    private int mFrequency;
    @SerializedName("serviceTripID")
    private String mServiceTripId;
    @SerializedName("serviceName")
    private String mServiceName;
    @SerializedName("serviceColor")
    private ServiceColor mServiceColor;
    @SerializedName("serviceNumber")
    private String mServiceNumber;
    @SerializedName("serviceOperator")
    private String mServiceOperator;
    @SerializedName("stopCode")
    private String mStartStopCode;
    @SerializedName("endStopCode")
    private String mEndStopCode;
    @SerializedName("streets")
    private ArrayList<Street> mStreets;
    @SerializedName("shapes")
    private @Nullable
    ArrayList<Shape> shapes;
    @SerializedName("realtimeVehicle")
    private RealTimeVehicle mRealTimeVehicle;
    @SerializedName("wheelchairAccessible")
    private boolean wheelchairAccessible;
    @SerializedName("bicycleAccessible")
    private boolean bicycleAccessible;
    @SerializedName("localCost")
    @Nullable
    private LocalCost localCost;

    @SerializedName("timetableStartTime")
    @Nullable
    private long mTimetableStartTime;
    @SerializedName("timetableEndTime")
    @Nullable
    private long mTimetableEndTime;

    @SerializedName("availability")
    private String mAvailability;

    private long mStartTimeInSecs;
    private long mEndTimeInSecs;

    private boolean hideExactTimes;
    @Nullable
    private List<Geofence> geofences;

    /**
     * This is no longer a part of json returned from server due to Version 6.
     * It's currently being used for json-based persistence on app local.
     *
     * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs#Version-6">Version 6</a>
     */
    @SerializedName("alerts")
    private ArrayList<RealtimeAlert> mAlerts;
    @SerializedName("isContinuation")
    private boolean mIsContinuation;
    @SerializedName("serviceDirection")
    private String mServiceDirection;
    /**
     * NOTE: Values are defined by 'modes' in https://api.tripgo.com/v1/regions.json
     */
    @SerializedName("modeIdentifier")
    private String transportModeId;
    /**
     * Currently used for shuttle buses which explains how often they run
     */
    @SerializedName("terms")
    private String mTerms;
    /**
     * Indicates if the times are real-time or not.
     *
     * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs#Version-9-next">Version 9</a>
     * @see <a href="https://redmine.buzzhives.com/issues/3931">Issue 3931</a>
     */
    @SerializedName("realTime")
    private boolean isRealTime;
    /**
     * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs#Version-9-next">Version 9</a>
     * @see <a href="https://redmine.buzzhives.com/issues/3931">Issue 3931</a>
     */
    @SerializedName("durationWithoutTraffic")
    private long durationWithoutTraffic;
    @SerializedName("segmentTemplateHashCode")
    private long templateHashCode;
    @SerializedName("alertHashCodes")
    private long[] alertHashCodes;
    @SerializedName("platform")
    private String platform;
    @SerializedName("stops")
    private int stopCount;
    @SerializedName("metres")
    private int metres;
    @SerializedName("metresSafe")
    private int metresSafe;
    @SerializedName("metresUnsafe")
    private int metresUnsafe;
    @SerializedName("turn-by-turn")
    private String turnByTurn;

    @SerializedName("hasCarParks")
    private boolean hasCarParks;

    @SerializedName("ticket")
    private Ticket ticket;

    @SerializedName("ticketWebsiteURL")
    private String ticketURL;

    @SerializedName("tickets")
    private List<Ticket> tickets;

    private TripKitMapTiles mapTiles;

    /**
     * FIXME: Should replace this with Quantity Strings.
     * See http://developer.android.com/intl/vi/guide/topics/resources/string-resource.html#Plurals.
     */
    public static String convertStopCountToText(Context context, int stopCount) {
        return String.format(Locale.US, context.getResources().getQuantityString(R.plurals.number_of_stops, stopCount), stopCount);
    }

    public String getTerms() {
        return mTerms;
    }

    public void setTerms(String terms) {
        mTerms = terms;
    }

    public String getServiceDirection() {
        return mServiceDirection;
    }

    public void setServiceDirection(String serviceDirection) {
        mServiceDirection = serviceDirection;
    }

    public Trip getTrip() {
        return mTrip;
    }

    public void setTrip(Trip trip) {
        mTrip = trip;
    }

    public List<Street> getStreets() {
        return mStreets;
    }

    public void setStreets(ArrayList<Street> streets) {
        mStreets = streets;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @Nullable
    public SegmentType getType() {
        if (type == null) {
            return mType;
        }
        return SegmentTypeKt.from(type);
    }

    public void setType(SegmentType type) {
        mType = type;
    }

    /**
     * Use {@link TripSegmentExtensionsKt#getStartDateTime(TripSegment)} instead.
     */
    @Deprecated
    @Override
    public long getStartTimeInSecs() {
        if (mStartTimeInSecs > 0) {
            return mStartTimeInSecs;
        }

        long millis = -1L;
        try {
            millis = Long.parseLong(startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (millis < 0 && startTime != null) {
            millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(startTime).getMillis();
        }

        if (startTime == null) {
            millis = 0;
        }
        return millis;
    }

    /**
     * NOTE: You should only use this setter for testing purpose.
     */
    public void setStartTimeInSecs(long newStartTimeInSecs) {
        mStartTimeInSecs = newStartTimeInSecs;
    }

    /**
     * Use {@link TripSegmentExtensionsKt#getEndDateTime(TripSegment)} instead.
     */
    @Deprecated
    @Override
    public long getEndTimeInSecs() {
        if (mEndTimeInSecs > 0) {
            return mEndTimeInSecs;
        }

        long millis = -1L;
        try {
            millis = Long.parseLong(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (millis < 0 && endTime != null) {
            millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(endTime).getMillis();
        }

        if (endTime == null) {
            millis = 0;
        }
        return millis;
    }

    /**
     * NOTE: You should only use this setter for testing purpose.
     */
    public void setEndTimeInSecs(long newEndTimeInSecs) {
        mEndTimeInSecs = newEndTimeInSecs;
    }

    public MiniInstruction getMiniInstruction() {
        return mMiniInstruction;
    }

    @Deprecated
    public VehicleMode getMode() {
        return modeInfo != null ? modeInfo.getModeCompat() : null;
    }

    public String getVisibility() {
        return mVisibility;
    }

    public void setVisibility(String visibility) {
        mVisibility = visibility;
    }

    public Location getFrom() {
        return mFrom;
    }

    public void setFrom(Location from) {
        mFrom = from;
    }

    public Location getTo() {
        return mTo;
    }

    public void setTo(Location to) {
        mTo = to;
    }

    public Location getSingleLocation() {
        return mSingleLocation;
    }

    public void setSingleLocation(Location singleLocation) {
        mSingleLocation = singleLocation;
    }

    @Nullable
    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    /**
     * NOTE: For unscheduled segments, if we want to show notes on views, don't call this.
     * Call {@code getDisplayNotes} instead.
     */
    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public boolean isFrequencyBased() {
        return mIsFrequencyBased;
    }

    public void isFrequencyBased(boolean isFrequencyBased) {
        mIsFrequencyBased = isFrequencyBased;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency(int frequency) {
        mFrequency = frequency;
    }

    @Override
    public String getServiceTripId() {
        return mServiceTripId;
    }

    public void setServiceTripId(String serviceTripId) {
        mServiceTripId = serviceTripId;
    }

    @Override
    public String getOperator() {
        return mServiceOperator;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }

    @Nullable
    public ServiceColor getServiceColor() {
        return mServiceColor;
    }

    public void setServiceColor(ServiceColor serviceColor) {
        mServiceColor = serviceColor;
    }

    public String getServiceNumber() {
        return mServiceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        mServiceNumber = serviceNumber;
    }

    public String getServiceOperator() {
        return mServiceOperator;
    }

    public void setServiceOperator(String serviceOperator) {
        mServiceOperator = serviceOperator;
    }

    @Override
    public String getStartStopCode() {
        return mStartStopCode;
    }

    @Override
    public void setStartStopCode(String startStopCode) {
        mStartStopCode = startStopCode;
    }

    @Override
    public String getEndStopCode() {
        return mEndStopCode;
    }

    @Override
    public void setEndStopCode(String endStopCode) {
        mEndStopCode = endStopCode;
    }

    public @Nullable
    List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(@Nullable ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public RealTimeVehicle getRealTimeVehicle() {
        return mRealTimeVehicle;
    }

    public void setRealTimeVehicle(RealTimeVehicle realTimeVehicle) {
        mRealTimeVehicle = realTimeVehicle;
    }

    @Nullable
    public ArrayList<RealtimeAlert> getAlerts() {
        return mAlerts;
    }

    public void setAlerts(ArrayList<RealtimeAlert> alerts) {
        mAlerts = alerts;
    }

    public boolean getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(boolean wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public boolean getBicycleAccessible() {
        return bicycleAccessible;
    }

    public void setBicycleAccessible(boolean bicycleAccessible) {
        this.bicycleAccessible = bicycleAccessible;
    }

    public int getWheelchairFriendliness() {
        return Math.round(metresSafe / (float) metres * 100);
    }

    public int getCycleFriendliness() {
        return Math.round(metresSafe / (float) metres * 100);
    }

    public int getMetres() {
        return metres;
    }

    public void setMetres(int metres) {
        this.metres = metres;
    }

    public int getMetresSafe() {
        return metresSafe;
    }

    public void setMetresSafe(int metresSafe) {
        this.metresSafe = metresSafe;
    }

    public int getMetresUnsafe() {
        return metresUnsafe;
    }

    public void setMetresUnsafe(int metresUnsafe) {
        this.metresUnsafe = metresUnsafe;
    }

    @Nullable
    public TurnByTurn getTurnByTurn() {
        if (turnByTurn != null) {
            return TurnByTurn.valueOf(turnByTurn);
        } else {
            return null;
        }
    }

    @Nullable
    public LocalCost getLocalCost() {
        return localCost;
    }

    public boolean isContinuation() {
        return mIsContinuation;
    }

    public void setContinuation(boolean isContinuation) {
        mIsContinuation = isContinuation;
    }

    public String getPairIdentifier() {
        return String.format(
            StyleManager.FORMAT_PAIR_IDENTIFIER,
            mStartStopCode,
            mEndStopCode
        );
    }

    public boolean isStationary() {
        return getType() == null || getType() == SegmentType.STATIONARY;
    }

    /**
     * Segments having type as {@link SegmentType#DEPARTURE}, {@link SegmentType#ARRIVAL},
     * and {@link SegmentType#STATIONARY} will have this property as null.
     * <p/>
     * For more information about the transport. Please check out {@link TripSegment#getModeInfo()}.
     *
     * @see <a href="http://skedgo.github.io/tripgo-api/site/faq/#mode-identifiers">Mode Identifiers</a>
     */
    @Nullable
    public String getTransportModeId() {
        return transportModeId;
    }

    public void setTransportModeId(String transportModeId) {
        this.transportModeId = transportModeId;
    }

    public long getTemplateHashCode() {
        return templateHashCode;
    }

    public void setTemplateHashCode(long templateHashCode) {
        this.templateHashCode = templateHashCode;
    }

    /**
     * @see <a href="http://skedgo.github.io/tripgo-api/site/faq/#mode-identifiers">Mode Identifiers</a>
     */
    @Nullable
    public ModeInfo getModeInfo() {
        return modeInfo;
    }

    public void setModeInfo(ModeInfo modeInfo) {
        this.modeInfo = modeInfo;
    }

    @Nullable
    public String getTimeZone() {
        final Location location = TripSegmentUtils.getFirstNonNullLocation(
            this.getFrom(),
            this.getSingleLocation()
        );
        if (location != null) {
            return location.getTimeZone();
        }
        return null;
    }

    public Long getBookingHashCode() {
        return bookingHashCode;
    }

    public void setBookingHashCode(Long bookingHashCode) {
        this.bookingHashCode = bookingHashCode;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public SharedVehicle getSharedVehicle() {
        return sharedVehicle;
    }

    public void setSharedVehicle(SharedVehicle vehicle) {
        this.sharedVehicle = sharedVehicle;
    }

    public long[] getAlertHashCodes() {
        return alertHashCodes;
    }

    public void setAlertHashCodes(long[] alertHashCodes) {
        this.alertHashCodes = alertHashCodes;
    }

    public boolean isRealTime() {
        return isRealTime;
    }

    public void setRealTime(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    public String getAvailability() {
        return mAvailability;
    }

    public void setAvailability(String mAvailability) {
        this.mAvailability = mAvailability;
    }

    public boolean isVisibleInContext(String contextVisibility) {
        if (mVisibility == null || mVisibility.isEmpty() ||
            contextVisibility == null || contextVisibility.isEmpty() ||
            mVisibility.equals(Visibilities.VISIBILITY_HIDDEN) ||
            contextVisibility.equals(Visibilities.VISIBILITY_HIDDEN)) {
            return false;
        }

        if (contextVisibility.equals(Visibilities.VISIBILITY_IN_SUMMARY)) {
            return mVisibility.equals(Visibilities.VISIBILITY_ON_MAP) || mVisibility.equals(Visibilities.VISIBILITY_IN_SUMMARY);
        } else if (contextVisibility.equals(Visibilities.VISIBILITY_ON_MAP)) {
            return !mVisibility.equals(Visibilities.VISIBILITY_IN_DETAILS);
        } else if (contextVisibility.equals(Visibilities.VISIBILITY_IN_DETAILS)) {
            // Show segment in details, no matter what the type
            return true;
        } else {
            return true;
        }
    }

    /**
     * As of now, used only for unscheduled segments.
     * If we want to show notes on views, use it instead of {@code getNotes}.
     * Some essential templates will be resolved before being presented by the views.
     */
    @Nullable
    public String getDisplayNotes(Context context, boolean withPlatform) {
        String notes = TripSegmentUtils.processDurationTemplate(
            context,
            mNotes,
            null,
            mStartTimeInSecs == 0 && Long.parseLong(startTime) > 0 ? Long.parseLong(startTime) : mStartTimeInSecs,
            mEndTimeInSecs == 0 && Long.parseLong(endTime) > 0 ? Long.parseLong(endTime) : mEndTimeInSecs
        );

        if (notes == null) {
            return null;
        }


        if (platform != null && withPlatform) {
            notes = notes.replace(Templates.TEMPLATE_PLATFORM, String.format(Templates.FORMAT_PLATFORM, platform));
        } else {
            notes = notes.replace(Templates.TEMPLATE_PLATFORM + "\n", "");
        }

        notes = notes.replace(Templates.TEMPLATE_STOPS, convertStopCountToText(context, stopCount));

        if (durationWithoutTraffic == 0) {
            return notes;
        }

        long durationWithTraffic = mEndTimeInSecs - mStartTimeInSecs;
        if (durationWithoutTraffic < durationWithTraffic + 60 /* secs */) {
            // Plus 60 secs since we show both duration types in minutes.
            // For instance, if durationWithTraffic is 65 secs, and durationWithoutTraffic is 60 secs,
            // they will be both shown as '1min'. Thus, no need to show this difference.
            return notes.replace(Templates.TEMPLATE_TRAFFIC, getDurationWithoutTrafficText(context));
        } else {
            // TODO: Probably we also need to remove a redundant dot char next to the template.
            return notes.replace(Templates.TEMPLATE_TRAFFIC, "");
        }
    }

    public String getRealTimeStatusText(Resources resources) {
        if (isRealTime) {
            if (getMode() != null && getMode().isPublicTransport()) {
                return resources.getString(R.string.real_minustime);
            } else {
                return resources.getString(R.string.live_traffic);
            }
        } else {
            return null;
        }
    }

    @DrawableRes
    public int getDarkVehicleIcon() {
        if (modeInfo != null && modeInfo.getModeCompat() != null) {
            return isRealTime
                ? modeInfo.getModeCompat().getRealTimeIconRes()
                : modeInfo.getModeCompat().getIconRes();
        } else {
            return 0;
        }
    }

    @DrawableRes
    public int getDarkVehicleIconWithNoRealtimeChecking() {
        if (modeInfo != null && modeInfo.getModeCompat() != null) {
            return modeInfo.getModeCompat().getIconRes();
        } else {
            return 0;
        }
    }

    public Drawable getLightTransportIcon(@NonNull Context context) {
        if (modeInfo != null && modeInfo.getModeCompat() != null) {
            return isRealTime
                ? modeInfo.getModeCompat().getRealtimeMapIconRes(context)
                : modeInfo.getModeCompat().getMapIconRes(context);
        } else {
            return createLightDrawable(context, R.drawable.v4_ic_map_location);
        }
    }

    public boolean isPlane() {
        return getType() == SegmentType.SCHEDULED
            && transportModeId != null
            && transportModeId.startsWith(TransportMode.ID_AIR);
    }

    public boolean hasTimeTable() {
        return getType() == SegmentType.SCHEDULED
            && mServiceTripId != null
            && !(mIsContinuation || isPlane());
    }


    public boolean isWalking() {
        return (transportModeId != null && transportModeId.startsWith("wa_"));
    }

    public boolean isCycling() {
        return (transportModeId != null && (transportModeId.startsWith("bic") || transportModeId.startsWith("cy_")));
    }

    public boolean isWheelchair() {
        return (transportModeId != null && transportModeId.equals(TransportMode.ID_WHEEL_CHAIR));
    }

    public long getDurationWithoutTraffic() {
        return durationWithoutTraffic;
    }

    public void setDurationWithoutTraffic(long durationWithoutTraffic) {
        this.durationWithoutTraffic = durationWithoutTraffic;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    @Nullable
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    private String getDurationWithoutTrafficText(Context context) {
        String durationText = TimeUtils.getDurationInDaysHoursMins(context, (int) durationWithoutTraffic);
        return context.getResources().getString(R.string._pattern_w_slasho_traffic, durationText);
    }

    public int lineColor() {
        if (isWalking()) return Color.TRANSPARENT;
        if (mServiceColor == null) {
            if (modeInfo != null && modeInfo.getColor() != null) {
                return modeInfo.getColor().getColor();
            }
            return Color.TRANSPARENT;
        }
        return mServiceColor.getColor();
    }

    public long getTimetableStartTime() {
        return mTimetableStartTime;
    }

    public void setTimetableStartTime(long timetableStartTime) {
        mTimetableStartTime = timetableStartTime;
    }

    public long getTimetableEndTime() {
        return mTimetableEndTime;
    }

    public void setTimetableEndTime(long timetableEndTime) {
        mTimetableEndTime = timetableEndTime;
    }

    public boolean hasCarParks() {
        return hasCarParks;
    }

    public void setHasCarParks(boolean hasCarParks) {
        this.hasCarParks = hasCarParks;
    }

    public boolean isHideExactTimes() {
        return hideExactTimes;
    }

    public void setHideExactTimes(boolean hideExactTimes) {
        this.hideExactTimes = hideExactTimes;
    }

    @Nullable
    public List<Geofence> getGeofences() {
        return geofences;
    }

    public void setGeofences(List<Geofence> geofences) {
        this.geofences = geofences;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getTicketURL() {
        return ticketURL;
    }

    public TripKitMapTiles getMapTiles() {
        return mapTiles;
    }

    public void setMapTiles(TripKitMapTiles mapTiles) {
        this.mapTiles = mapTiles;
    }

    public boolean isQuickBooking() {
        return getBooking() != null && getBooking().getQuickBookingsUrl() != null;
    }
}