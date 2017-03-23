package com.skedgo.android.common.model;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static com.skedgo.android.common.model.SegmentType.from;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class SegmentTypeTest {
  @Test
  public void shouldConvertIntoDeparture() {
    assertThat(from("departure")).isEqualTo(SegmentType.DEPARTURE);
  }

  @Test
  public void shouldConvertIntoDepartureEvenIfUppercaseString() {
    assertThat(from("DEPARTURE")).isEqualTo(SegmentType.DEPARTURE);
  }

  @Test
  public void shouldConvertIntoScheduled() {
    assertThat(from("scheduled")).isEqualTo(SegmentType.SCHEDULED);
  }

  @Test
  public void shouldConvertIntoScheduledEvenIfUppercaseString() {
    assertThat(from("SCHEDULED")).isEqualTo(SegmentType.SCHEDULED);
  }

  @Test
  public void shouldConvertIntoUnscheduled() {
    assertThat(from("unscheduled")).isEqualTo(SegmentType.UNSCHEDULED);
  }

  @Test
  public void shouldConvertIntoUnscheduledEvenIfUppercaseString() {
    assertThat(from("UNSCHEDULED")).isEqualTo(SegmentType.UNSCHEDULED);
  }

  @Test
  public void shouldConvertIntoStationary() {
    assertThat(from("stationary")).isEqualTo(SegmentType.STATIONARY);
  }

  @Test
  public void shouldConvertIntoStationaryEvenIfUppercaseString() {
    assertThat(from("STATIONARY")).isEqualTo(SegmentType.STATIONARY);
  }

  @Test
  public void shouldConvertIntoArrival() {
    assertThat(from("arrival")).isEqualTo(SegmentType.ARRIVAL);
  }

  @Test
  public void shouldConvertIntoArrivalEvenIfUppercaseString() {
    assertThat(from("ARRIVAL")).isEqualTo(SegmentType.ARRIVAL);
  }

  @Test
  public void shouldConvertIntoNullIfInvalidString() {
    assertThat(from("I'm not an enum!")).isNull();
  }
}