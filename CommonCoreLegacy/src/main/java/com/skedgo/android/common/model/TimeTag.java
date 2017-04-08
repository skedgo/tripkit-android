package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

@Deprecated
public class TimeTag implements Parcelable {
  public static final Creator<TimeTag> CREATOR = new Creator<TimeTag>() {
    @Override
    public TimeTag createFromParcel(Parcel in) {
      return new TimeTag(in);
    }

    @Override
    public TimeTag[] newArray(int size) {
      return new TimeTag[size];
    }
  };

  public static final int TIME_TYPE_ARRIVE_BY = 1;
  public static final int TIME_TYPE_LEAVE_AFTER = 0;

  @TimeType
  private int type = TIME_TYPE_LEAVE_AFTER;
  private long timeInSecs = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
  private boolean isDynamic = false;

  protected TimeTag(Parcel in) {
    // noinspection ResourceType
    type = in.readInt();
    timeInSecs = in.readLong();
    isDynamic = in.readByte() == 1;
  }

  protected TimeTag() {
  }

  public static TimeTag createForLeaveNow() {
    final TimeTag timeTag = new TimeTag();
    timeTag.setIsDynamic(true);
    timeTag.setTimeInSecs(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    timeTag.setType(TimeTag.TIME_TYPE_LEAVE_AFTER);
    return timeTag;
  }

  public static TimeTag createForLeaveAfter(long seconds) {
    final TimeTag timeTag = new TimeTag();
    timeTag.setIsDynamic(false);
    timeTag.setTimeInSecs(seconds);
    timeTag.setType(TimeTag.TIME_TYPE_LEAVE_AFTER);
    return timeTag;
  }

  public static TimeTag createForArriveBy(long seconds) {
    final TimeTag timeTag = new TimeTag();
    timeTag.setIsDynamic(false);
    timeTag.setTimeInSecs(seconds);
    timeTag.setType(TimeTag.TIME_TYPE_ARRIVE_BY);
    return timeTag;
  }

  public static TimeTag createForTimeType(@TimeType int timeType, long seconds) {
    final TimeTag timeTag = new TimeTag();
    timeTag.setTimeInSecs(seconds);
    timeTag.setType(timeType);
    timeTag.setIsDynamic(false);
    return timeTag;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(type);
    dest.writeLong(timeInSecs);
    dest.writeByte((byte) (isDynamic ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @TimeType
  public int getType() {
    return type;
  }

  public void setType(@TimeType int type) {
    this.type = type;
  }

  public long getTimeInSecs() {
    return timeInSecs;
  }

  public void setTimeInSecs(long timeInSecs) {
    this.timeInSecs = timeInSecs;
  }

  /**
   * @return True if the time was chosen as 'Now'. Otherwise, false.
   */
  public boolean isDynamic() {
    return isDynamic;
  }

  public void setIsDynamic(boolean isDynamic) {
    this.isDynamic = isDynamic;
  }

  /**
   * See: http://tools.android.com/tech-docs/support-annotations.
   */
  @IntDef({TIME_TYPE_LEAVE_AFTER, TIME_TYPE_ARRIVE_BY})
  @Retention(RetentionPolicy.SOURCE)
  public @interface TimeType {}
}