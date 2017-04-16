package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.util.StringUtils;

import skedgo.tripkit.routing.LocationExKt;

public class Location implements Parcelable, ILatLon {
  public static final double SYDNEY_LAT = -33.871008;
  public static final double SYDNEY_LON = 151.209555;

  /**
   * No known location type
   */
  public static final int TYPE_UNKNOWN = -1;

  /**
   * The location is a scheduled stop
   */
  public static final int TYPE_SCHEDULED_STOP = 1;

  /**
   * Location is a stop on a user's trip
   */
  public static final int TYPE_SERVICE_STOP = 2;

  /**
   * Location comes from previous search/geocoding history or long-pressed
   */
  public static final int TYPE_HISTORY = 3;

  /**
   * Location comes from users calendar
   */
  public static final int TYPE_CALENDAR = 4;

  /**
   * Location comes from a contact in the users address book
   */
  public static final int TYPE_CONTACT = 5;

  /**
   * Location is info from the users personal contact card (home/work address etc)
   */
  public static final int TYPE_PERSONAL = 6;

  /**
   * Location is info from the users personal contact card (home/work address etc)
   */
  public static final int TYPE_HOME = 7; //so we never delete this location

  /**
   * What3Words type
   */
  public static final int TYPE_W3W = 9;

  public static final int NO_BEARING = Integer.MAX_VALUE;
  public static final double ZERO_LAT = 0.0;
  public static final double ZERO_LON = 0.0;
  /**
   * Source
   */
  public static final String GOOGLE = "google";
  public static final String FOURSQUARE = "foursquare";
  public static final Creator<Location> CREATOR = new Creator<Location>() {
    public Location createFromParcel(Parcel in) {
      Location location = new Location();

      location.mId = in.readLong();
      location.name = in.readString();
      location.address = in.readString();
      location.lat = in.readDouble();
      location.lon = in.readDouble();
      location.exact = in.readInt() == 1;
      location.bearing = in.readInt();
      location.mLocationType = in.readInt();
      location.mIsFavourite = in.readInt() == 1;
      location.phoneNumber = in.readString();
      location.url = in.readString();
      location.mRatingCount = in.readInt();
      location.mAverageRating = in.readFloat();
      location.mRatingImageUrl = in.readString();
      location.mSource = in.readString();
      location.mFavouriteSortOrderIndex = in.readInt();
      location.timeZone = in.readString();
      location.popularity = in.readInt();
      location.locationClass = in.readString();
      location.w3w = in.readString();
      location.w3wInfoURL = in.readString();

      return location;
    }

    public Location[] newArray(int size) {
      return new Location[size];
    }
  };
  private static final int EARTH_RADIUS_IN_METERS = 6371 * 1000;
  /**
   * Locations this close to each other will be considered equal
   * for the sake of comparing 2 locations
   */
  private static final int APPROXIMATE_EQUALITY_METERS = 30;
  /**
   * relax the diameter
   */
  private static final int APPROXIMATE_EQUALITY_METERS_LOOSE = 60;

  protected long mId;
  protected boolean mIsFavourite;
  protected int mLocationType;
  protected int mFavouriteSortOrderIndex;
  protected int mRatingCount;
  protected float mAverageRating;
  protected String mRatingImageUrl;
  protected String mSource;
  private long lastUpdatedTime;

  @SerializedName("name") private String name;
  @SerializedName("address") private String address;
  @SerializedName("lat") private double lat;
  @SerializedName("lng") private double lon;
  @SerializedName("exact") private boolean exact;
  @SerializedName("bearing") private int bearing;
  @SerializedName("phone") private String phoneNumber;
  @SerializedName("URL") private String url;
  @SerializedName("timezone") private String timeZone;
  @SerializedName("popularity") private int popularity;
  @SerializedName("class") private String locationClass;
  @SerializedName("w3w") private String w3w;
  @SerializedName("w3wInfoURL") private String w3wInfoURL;

  public Location() {
    lat = ZERO_LAT;
    lon = ZERO_LON;
    mId = 0;
    mAverageRating = -1;
    mRatingCount = -1;
    mLocationType = TYPE_UNKNOWN;
    bearing = NO_BEARING;
  }

  public Location(Location other) {
    this();
    fillFrom(other);
  }

  public Location(LatLng ll) {
    this(ll == null ? 0.0 : ll.latitude, ll == null ? 0.0 : ll.longitude);
  }

  public Location(double lat, double lon) {
    this();
    this.lat = lat;
    this.lon = lon;
  }

  public static boolean isValidLocation(Location loc) {
    return (loc != null) && loc.isNonZeroLocation();
  }

  private static boolean equals(Object left, Object right) {
    return left == right || (left != null && left.equals(right));
  }

  @Override public boolean equals(Object o) {
    return this == o || o instanceof Location && equalTo((Location) o);
  }

  public void fillFrom(Location other) {
    if (other == null) {
      return;
    }

    mId = other.mId;
    name = other.name;
    address = other.address;
    lat = other.lat;
    lon = other.lon;
    exact = other.exact;
    bearing = other.bearing;
    mLocationType = other.mLocationType;
    mIsFavourite = other.mIsFavourite;
    phoneNumber = other.phoneNumber;
    url = other.url;
    mRatingCount = other.mRatingCount;
    mAverageRating = other.mAverageRating;
    mRatingImageUrl = other.mRatingImageUrl;
    mSource = other.mSource;
    mFavouriteSortOrderIndex = other.mFavouriteSortOrderIndex;
    popularity = other.popularity;
    locationClass = other.locationClass;
    w3w = other.w3w;
    w3wInfoURL = other.w3wInfoURL;
  }

  public long getId() {
    return mId;
  }

  public void setId(long id) {
    mId = id;
  }

  public boolean isFavourite() {
    return mIsFavourite;
  }

  public void isFavourite(boolean favourite) {
    mIsFavourite = favourite;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public double getLat() {
    return lat;
  }

  @Override
  public void setLat(double lat) {
    this.lat = lat;
  }

  @Override
  public double getLon() {
    return lon;
  }

  @Override
  public void setLon(double lon) {
    this.lon = lon;
  }

  public boolean isExact() {
    return exact;
  }

  public void setExact(boolean exact) {
    this.exact = exact;
  }

  public int getBearing() {
    return bearing;
  }

  public void setBearing(int bearing) {
    this.bearing = bearing;
  }

  public boolean hasValidCoordinates() {
    return !(lat == 0 && lon == 0);
  }

  public int getLocationType() {
    return mLocationType;
  }

  public void setLocationType(int type) {
    mLocationType = type;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getRatingCount() {
    return mRatingCount;
  }

  public void setRatingCount(int ratingCount) {
    mRatingCount = ratingCount;
  }

  public float getAverageRating() {
    return mAverageRating;
  }

  public void setAverageRating(float averageRating) {
    mAverageRating = averageRating;
  }

  public String getRatingImageUrl() {
    return mRatingImageUrl;
  }

  public void setRatingImageUrl(String ratingImageUrl) {
    mRatingImageUrl = ratingImageUrl;
  }

  public String getSource() {
    return mSource;
  }

  public void setSource(String source) {
    mSource = source;
  }

  public int getFavouriteSortOrderIndex() {
    return mFavouriteSortOrderIndex;
  }

  public void setFavouriteSortOrderIndex(int favouriteSortOrderIndex) {
    mFavouriteSortOrderIndex = favouriteSortOrderIndex;
  }

  public String getCoordinateString() {
    return "(" + round(lat) + ", " + round(lon) + ")";
  }

  public int distanceTo(Location location) {
    if (location == null || !location.hasValidCoordinates()) {
      return -1;
    } else {
      return distanceTo(location.lat, location.lon);
    }
  }

  public boolean isApproximatelyAt(Location other) {
    return distanceTo(other) < APPROXIMATE_EQUALITY_METERS;
  }

  public boolean isLooselyApproximatelyAt(Location other) {
    return distanceTo(other) < APPROXIMATE_EQUALITY_METERS_LOOSE;
  }

  public boolean isApproximatelyAt(double lat, double lon) {
    return distanceTo(lat, lon) < APPROXIMATE_EQUALITY_METERS;
  }

  public boolean equalsByLatLon(Location _loc) {
    return _loc != null && _loc.lat == lat && _loc.lon == lon;
  }

  public boolean isNonZeroLocation() {
    return !(lat == ZERO_LAT && lon == ZERO_LON);
  }

  /**
   * Use {@link LocationExKt#getDateTimeZone(Location)} instead.
   */
  @Deprecated
  @Nullable public String getTimeZone() {
    return timeZone;
  }

  /**
   * NOTE: You should only use this setter for testing purpose.
   */
  public void setTimeZone(@Nullable String timeZone) {
    this.timeZone = timeZone;
  }

  @Nullable public String getLocationClass() {
    return locationClass;
  }

  public void setLocationClass(String locationClass) {
    this.locationClass = locationClass;
  }

  public int getPopularity() {
    return popularity;
  }

  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }

  public String getW3w() {
    return w3w;
  }

  public void setW3w(String w3w) {
    this.w3w = w3w;
  }

  public String getW3wInfoURL() {
    return w3wInfoURL;
  }

  public void setW3wInfoURL(String w3wInfoURL) {
    this.w3wInfoURL = w3wInfoURL;
  }

  /**
   * Get the distance between this and another point
   * <p/>
   * This implementation was pulled from:
   * <a href="http://www.codecodex.com/wiki/Calculate_Distance_Between_Two_Points_on_a_Globe#Java">CodeCodex</a>
   *
   * @param lat
   * @param lon
   * @return The distance in meters between <code>this</code> and <code>that</code>
   * @see <a href="http://en.wikipedia.org/wiki/Haversine_formula">Haversine Formula</a>
   */
  public int distanceTo(double lat, double lon) {
    double dLat = Math.toRadians(lat - this.lat);
    double dLon = Math.toRadians(lon - this.lon);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(lat)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.asin(Math.sqrt(a));
    return (int) (EARTH_RADIUS_IN_METERS * c);
  }

  /**
   * @param other
   * @return The bearing from this location to another other
   */
  public double getBearingTo(Location other) {
    return getBearingTo(other.getLat(), other.getLon());
  }

  /**
   * @param lat
   * @param lon
   * @return The bearing from this location to another other
   * <p/>
   * Kudos: http://stackoverflow.com/a/9462757/755332
   */
  public double getBearingTo(double lat, double lon) {
    double longitude1 = this.lon;
    double longitude2 = lon;

    double latitude1 = Math.toRadians(this.lat);
    double latitude2 = Math.toRadians(lat);

    double longDiff = Math.toRadians(longitude2 - longitude1);
    double y = Math.sin(longDiff) * Math.cos(latitude2);
    double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

    return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeLong(mId);
    out.writeString(name);
    out.writeString(address);
    out.writeDouble(lat);
    out.writeDouble(lon);
    out.writeInt(exact ? 1 : 0);
    out.writeInt(bearing);
    out.writeInt(mLocationType);
    out.writeInt(mIsFavourite ? 1 : 0);
    out.writeString(phoneNumber);
    out.writeString(url);
    out.writeInt(mRatingCount);
    out.writeFloat(mAverageRating);
    out.writeString(mRatingImageUrl);
    out.writeString(mSource);
    out.writeInt(mFavouriteSortOrderIndex);
    out.writeString(timeZone);
    out.writeInt(popularity);
    out.writeString(locationClass);
    out.writeString(w3w);
    out.writeString(w3wInfoURL);
  }

  public long getLastUpdatedTime() {
    return lastUpdatedTime;
  }

  public void setLastUpdatedTime(long lastUpdatedTime) {
    this.lastUpdatedTime = lastUpdatedTime;
  }

  /**
   * To present a human-readable name of a location.
   * Invoke this if we want to present a location to users
   * (e.g, a pin on a map, location of an event).
   */
  public String getDisplayName() {
    return StringUtils.firstNonEmpty(
        name != null ? name.trim() : null,
        address != null ? address.trim() : null,
        getCoordinateString()
    );
  }

  public String getDisplayAddress() {
    return StringUtils.firstNonEmpty(
        address != null ? address.trim() : null,
        name != null ? name.trim() : null,
        getCoordinateString()
    );
  }

  /**
   * @return name (if a place got a name: station, uni, .etc) or suburb's name (if that's an address)
   */
  public String getNameOrApproximateAddress() {
    if (!TextUtils.isEmpty(name)) {
      return name.trim();
    }

    if (!TextUtils.isEmpty(address)) {
      String[] parts = address.split(",");
      if (parts.length > 1) {
        return parts[1].trim();
      } else {
        return address.trim();
      }
    }
    return null;
  }

  protected double round(double d) {
    return Math.round(d * 10000) / 10000.0;
  }

  private boolean equalTo(Location another) {
    return equals(name, another.name)
        && equals(address, another.address)
        && lat == another.lat
        && lon == another.lon
        && exact == another.exact
        && bearing == another.bearing
        && equals(phoneNumber, another.phoneNumber)
        && equals(url, another.url)
        && equals(timeZone, another.timeZone)
        && equals(popularity, another.popularity)
        && equals(locationClass, another.locationClass)
        && equals(w3w, another.w3w)
        && equals(w3wInfoURL, another.w3wInfoURL);
  }
}