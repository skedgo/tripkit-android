package com.skedgo.tripkit.common.model;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class TimeTagTest {
  @Test
  public void shouldParcel() throws Exception {
    final TimeTag expected = new TimeTag();
    expected.setIsDynamic(true);
    expected.setTimeInSecs(1234);
    expected.setType(TimeTag.TIME_TYPE_ARRIVE_BY);

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    final TimeTag actual = TimeTag.CREATOR.createFromParcel(parcel);
    assertThat(actual)
        .describedAs("Should parcel properly")
        .isEqualToComparingFieldByField(expected);
  }

  @Test
  public void shouldCreateForLeaveNow() {
    TimeTag actual = TimeTag.createForLeaveNow();
    assertThat(actual).isNotNull();
    assertThat(actual.isDynamic()).isTrue();
    assertThat(TimeUnit.SECONDS.toMillis(actual.getTimeInSecs()))
        .describedAs("Should be less than current time")
        .isGreaterThan(0L)
        .isLessThan(System.currentTimeMillis());
    assertThat(actual.getType()).isEqualTo(TimeTag.TIME_TYPE_LEAVE_AFTER);
  }

  @Test
  public void shouldCreateForLeaveAfter() {
    long departureTime = 1000L;
    TimeTag actual = TimeTag.createForLeaveAfter(departureTime);
    assertThat(actual).isNotNull();
    assertThat(actual.isDynamic()).isFalse();
    assertThat((actual.getTimeInSecs())).isEqualTo(departureTime);
    assertThat(actual.getType()).isEqualTo(TimeTag.TIME_TYPE_LEAVE_AFTER);
  }

  @Test
  public void shouldCreateForArriveBy() {
    long arrivalTime = 1000L;
    TimeTag actual = TimeTag.createForArriveBy(arrivalTime);
    assertThat(actual).isNotNull();
    assertThat(actual.isDynamic()).isFalse();
    assertThat((actual.getTimeInSecs())).isEqualTo(arrivalTime);
    assertThat(actual.getType()).isEqualTo(TimeTag.TIME_TYPE_ARRIVE_BY);
  }

  @Test
  public void shouldCreateForTimeType() {
    long time = 1000L;
    int timeType = TimeTag.TIME_TYPE_ARRIVE_BY;
    TimeTag actual = TimeTag.createForTimeType(timeType, time);
    assertThat(actual).isNotNull();
    assertThat(actual.isDynamic()).isFalse();
    assertThat((actual.getTimeInSecs())).isEqualTo(time);
    assertThat(actual.getType()).isEqualTo(timeType);
  }
}