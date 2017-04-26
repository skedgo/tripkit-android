package skedgo.tripkit.routing;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class SegmentTypeTest {
  @Test
  public void shouldConvertIntoDeparture() {
    assertThat(SegmentTypeKt.from("departure")).isEqualTo(SegmentType.DEPARTURE);
  }

  @Test
  public void shouldConvertIntoDepartureEvenIfUppercaseString() {
    assertThat(SegmentTypeKt.from("DEPARTURE")).isEqualTo(SegmentType.DEPARTURE);
  }

  @Test
  public void shouldConvertIntoScheduled() {
    assertThat(SegmentTypeKt.from("scheduled")).isEqualTo(SegmentType.SCHEDULED);
  }

  @Test
  public void shouldConvertIntoScheduledEvenIfUppercaseString() {
    assertThat(SegmentTypeKt.from("SCHEDULED")).isEqualTo(SegmentType.SCHEDULED);
  }

  @Test
  public void shouldConvertIntoUnscheduled() {
    assertThat(SegmentTypeKt.from("unscheduled")).isEqualTo(SegmentType.UNSCHEDULED);
  }

  @Test
  public void shouldConvertIntoUnscheduledEvenIfUppercaseString() {
    assertThat(SegmentTypeKt.from("UNSCHEDULED")).isEqualTo(SegmentType.UNSCHEDULED);
  }

  @Test
  public void shouldConvertIntoStationary() {
    assertThat(SegmentTypeKt.from("stationary")).isEqualTo(SegmentType.STATIONARY);
  }

  @Test
  public void shouldConvertIntoStationaryEvenIfUppercaseString() {
    assertThat(SegmentTypeKt.from("STATIONARY")).isEqualTo(SegmentType.STATIONARY);
  }

  @Test
  public void shouldConvertIntoArrival() {
    assertThat(SegmentTypeKt.from("arrival")).isEqualTo(SegmentType.ARRIVAL);
  }

  @Test
  public void shouldConvertIntoArrivalEvenIfUppercaseString() {
    assertThat(SegmentTypeKt.from("ARRIVAL")).isEqualTo(SegmentType.ARRIVAL);
  }

  @Test
  public void shouldConvertIntoNullIfInvalidString() {
    assertThat(SegmentTypeKt.from("I'm not an enum!")).isNull();
  }
}