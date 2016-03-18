package com.skedgo.android.tripkit;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TimeTag;
import com.skedgo.android.common.model.TransportEmissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

public final class RouteOptions {
  public static final int CYCLING_SPEED_AVERAGE = 1;
  public static final int CYCLING_SPEED_FAST = 2;
  public static final int CYCLING_SPEED_SLOW = 0;

  public static final int WALKING_SPEED_AVERAGE = 1;
  public static final int WALKING_SPEED_FAST = 2;
  public static final int WALKING_SPEED_SLOW = 0;

  private final Location arrival;
  private final Location departure;
  private final long millis;
  private final int timeType;
  private final int cyclingSpeed;
  private final int walkingSpeed;
  private final int transferTime;

  private final TransportEmissions transportEmissions;

  /**
   * In minutes. Default value is 0 which means ‘no limit'.
   * See more: https://www.flowdock.com/app/skedgo/androiddev/threads/nZJbtLU0jgsgziQpuoqhcaB-U9A.
   */
  private final int maxWalkingTime;

  private RouteOptions(@NonNull Builder builder) {
    departure = builder.departure;
    arrival = builder.arrival;
    millis = builder.millis;
    timeType = builder.timeType;
    cyclingSpeed = builder.cyclingSpeed;
    walkingSpeed = builder.walkingSpeed;
    transferTime = builder.transferTime;
    maxWalkingTime = builder.maxWalkingTime;
    transportEmissions = builder.transportEmissions;
  }

  @NonNull
  public Location getArrival() {
    return arrival;
  }

  @NonNull
  public Location getDeparture() {
    return departure;
  }

  public int getTimeType() {
    return timeType;
  }

  public long getMillis() {
    return millis;
  }

  @CYCLING_SPEED_TYPE
  public int getCyclingSpeed() {
    return cyclingSpeed;
  }

  @WALKING_SPEED_TYPE
  public int getWalkingSpeed() {
    return walkingSpeed;
  }

  /**
   * In minutes.
   */
  public int getTransferTime() {
    return transferTime;
  }

  /**
   * In minutes. Default value is 0 which means ‘no limit'.
   * Note that this is only used for XUM project.
   */
  public int getMaxWalkingTime() {
    return maxWalkingTime;
  }

  public TransportEmissions getTransportEmissions() {
    return transportEmissions;
  }

  @NonNull Query toQuery() {
    final Query query = new Query();
    query.setFromLocation(departure);
    query.setToLocation(arrival);

    final TimeTag timeTag = TimeTag.createForTimeType(
        timeType,
        TimeUnit.MILLISECONDS.toSeconds(millis)
    );
    query.setTimeTag(timeTag);
    query.setWalkingSpeed(walkingSpeed);
    query.setCyclingSpeed(cyclingSpeed);
    query.setTransferTime(transferTime);
    query.setMaxWalkingTime(maxWalkingTime);
    query.setTransportEmissions(transportEmissions);
    return query;
  }

  /**
   * See: http://tools.android.com/tech-docs/support-annotations.
   */
  @IntDef({WALKING_SPEED_SLOW, WALKING_SPEED_AVERAGE, WALKING_SPEED_FAST})
  @Retention(RetentionPolicy.SOURCE)
  public @interface WALKING_SPEED_TYPE {}

  /**
   * See: http://tools.android.com/tech-docs/support-annotations.
   */
  @IntDef({CYCLING_SPEED_SLOW, CYCLING_SPEED_AVERAGE, CYCLING_SPEED_FAST})
  @Retention(RetentionPolicy.SOURCE)
  public @interface CYCLING_SPEED_TYPE {}

  public static final class Builder {
    private final Location departure;
    private final Location arrival;
    private long millis;
    private int timeType = 0;
    private int transferTime;
    private int cyclingSpeed;
    private int walkingSpeed;
    private int maxWalkingTime;
    private TransportEmissions transportEmissions;

    public Builder(@NonNull Location departure,
                   @NonNull Location arrival) {
      this.departure = departure;
      this.arrival = arrival;
    }

    public Builder departAfter(long millis) {
      this.millis = millis;
      timeType = 0;
      return this;
    }

    public Builder arriveBy(long millis) {
      this.millis = millis;
      timeType = 1;
      return this;
    }

    public RouteOptions build() {
      return new RouteOptions(this);
    }

    public Builder cyclingSpeed(@CYCLING_SPEED_TYPE int cyclingSpeed) {
      this.cyclingSpeed = cyclingSpeed;
      return this;
    }

    public Builder walkingSpeed(@WALKING_SPEED_TYPE int walkingSpeed) {
      this.walkingSpeed = walkingSpeed;
      return this;
    }

    /**
     * Minimum transfer time in minutes. Default value is 0.
     */
    public Builder transferTime(int minutes) {
      this.transferTime = minutes;
      return this;
    }

    /**
     * In minutes. Default value is 0 which means ‘no limit'.
     * Note that this is only used for XUM project.
     */
    public Builder maxWalkingTime(int minutes) {
      this.maxWalkingTime = minutes;
      return this;
    }

    /**
     * transportEmissions.gramsCO2PerKm Emissions for supplied mode identifier in grams of CO2 per kilometre
     * transportEmissions.modeIdentifier Mode identifier for which to apply these emissions
     */
    public Builder transportEmissions(TransportEmissions transportEmissions) {
      this.transportEmissions = transportEmissions;
      return this;
    }
  }
}