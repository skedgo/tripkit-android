package com.skedgo.android.common.model;

import com.skedgo.android.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripSegmentsTest {
  @Test
  public void shouldReturnColorForPublicTransport() {
    final ServiceColor color = new ServiceColor(1, 2, 3);
    final TripSegment segment = new TripSegment();
    segment.setServiceColor(color);
    assertThat(TripSegments.getTransportColor(segment)).isSameAs(color);
  }

  @Test
  public void shouldReturnColorForPrivateTransport() {
    final ServiceColor color = new ServiceColor(1, 2, 3);
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setColor(color);
    final TripSegment segment = new TripSegment();
    segment.setModeInfo(modeInfo);
    assertThat(TripSegments.getTransportColor(segment)).isSameAs(color);
  }

  @Test
  public void shouldReturnNullTransportColor() {
    assertThat(TripSegments.getTransportColor(null)).isNull();
    assertThat(TripSegments.getTransportColor(new TripSegment())).isNull();
  }

  @Test
  public void shouldPickColorForPublicTransportFirst() {
    final ServiceColor privateTransportColor = new ServiceColor(1, 2, 3);
    final ModeInfo modeInfo = new ModeInfo();
    modeInfo.setColor(privateTransportColor);
    final TripSegment segment = new TripSegment();
    segment.setModeInfo(modeInfo);
    final ServiceColor publicTransportColor = new ServiceColor(4, 5, 6);
    segment.setServiceColor(publicTransportColor);
    assertThat(TripSegments.getTransportColor(segment)).isSameAs(publicTransportColor);
  }
}