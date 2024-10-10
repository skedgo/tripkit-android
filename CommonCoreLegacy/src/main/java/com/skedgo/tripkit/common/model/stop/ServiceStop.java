package com.skedgo.tripkit.common.model.stop;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.BicycleAccessible;
import com.skedgo.tripkit.common.model.WheelchairAccessible;
import com.skedgo.tripkit.common.model.location.Location;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

/**
 * Represents a future stop of a service in a trip.
 */
public class ServiceStop extends Location implements WheelchairAccessible, BicycleAccessible {
    public static final Creator<ServiceStop> CREATOR = new Creator<ServiceStop>() {
        public ServiceStop createFromParcel(Parcel in) {
            Location location = Location.CREATOR.createFromParcel(in);
            ServiceStop stop = new ServiceStop(location);

            stop.departure = in.readLong();
            stop.arrivalTime = in.readLong();
            stop.relativeArrival = (Long) in.readValue(Long.class.getClassLoader());
            stop.relativeDeparture = (Long) in.readValue(Long.class.getClassLoader());
            stop.code = in.readString();
            stop.shortName = in.readString();
            stop.wheelchairAccessible = (Boolean) in.readValue(Boolean.class.getClassLoader());
            stop.bicycleAccessible = (Boolean) in.readValue(Boolean.class.getClassLoader());
            stop.type = StopType.from(in.readString());

            return stop;
        }

        public ServiceStop[] newArray(int size) {
            return new ServiceStop[size];
        }
    };
    /**
     * This gets initialized lazily. So don't access it directly. Use its getter instead.
     */
    private StopType type;
    /**
     * This field is primarily used to interact with Gson.
     */
    @SerializedName("departure")
    private long departure;
    @SerializedName("relativeDeparture")
    private @Nullable Long relativeArrival;
    @SerializedName("relativeArrival")
    private @Nullable Long relativeDeparture;
    @SerializedName("arrival")
    private long arrivalTime;
    @SerializedName("code")
    private String code;
    @SerializedName("shortName")
    private @Nullable String shortName;
    @SerializedName("wheelchairAccessible")
    private @Nullable Boolean wheelchairAccessible;
    @SerializedName("bicycleAccessible")
    private @Nullable Boolean bicycleAccessible;

    public ServiceStop() {
    }

    public ServiceStop(Location location) {
        super(location);
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public @Nullable Long getRelativeDeparture() {
        return relativeDeparture;
    }

    public void setRelativeDeparture(@Nullable Long relativeDeparture) {
        this.relativeDeparture = relativeDeparture;
    }

    public @Nullable Long getRelativeArrival() {
        return relativeArrival;
    }

    public void setRelativeArrival(@Nullable Long relativeArrival) {
        this.relativeArrival = relativeArrival;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Nullable
    public String getShortName() {
        return shortName;
    }

    public void setShortName(@Nullable String shortName) {
        this.shortName = shortName;
    }

    @Nullable
    public Boolean getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(@Nullable Boolean wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    @Nullable
    public Boolean getBicycleAccessible() {
        return bicycleAccessible;
    }

    public void setBicycleAccessibleAccessible(@Nullable Boolean bicycleAccessible) {
        this.bicycleAccessible = bicycleAccessible;
    }

    public StopType getType() {
        return type;
    }

    public void setType(StopType type) {
        this.type = type;
    }

    public Long getDisplayTime() {
        if (departure != 0) {
            return TimeUnit.SECONDS.toMillis(departure);
        } else if (arrivalTime != 0) {
            return TimeUnit.SECONDS.toMillis(arrivalTime);
        } else {
            return null;
        }
    }

    @Override
    public void fillFrom(Location other) {
        if (other == null) {
            return;
        }

        super.fillFrom(other);

        if (other instanceof ServiceStop) {
            ServiceStop otherStop = (ServiceStop) other;
            type = otherStop.type;
            departure = otherStop.departureSecs();
            arrivalTime = otherStop.arrivalTime;
            code = otherStop.code;
            shortName = otherStop.shortName;
            wheelchairAccessible = otherStop.wheelchairAccessible;
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeLong(departure);
        out.writeLong(arrivalTime);
        out.writeValue(relativeArrival);
        out.writeValue(relativeDeparture);
        out.writeString(code);
        out.writeString(shortName);
        out.writeValue(wheelchairAccessible);
        out.writeValue(bicycleAccessible);
        out.writeString(type == null ? null : type.toString());
    }

    public long departureSecs() {
        return departure;
    }

    public void setDepartureSecs(long secs) {
        departure = secs;
    }
}