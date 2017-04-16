package com.skedgo.android.common.util;

import android.content.res.Resources;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.TripSegment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_DETAILS;
import static com.skedgo.android.common.model.TripSegment.VISIBILITY_ON_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TripSegmentListResolverTest {
  private TripSegmentListResolver resolver;

  @Before public void before() {
    final Resources resources = RuntimeEnvironment.application.getResources();
    resolver = new TripSegmentListResolver(resources);
  }

  @Test public void shouldOnlyShowDepartureSegmentInDetails() {
    TripSegment segment = new TripSegment();
    TripSegment departureSegment = resolver.createDepartureSegment(segment);
    assertThat(departureSegment.getVisibility()).isEqualTo(VISIBILITY_IN_DETAILS);
  }

  @Test public void arrivalSegmentShouldBeVisibleInMap() {
    final TripSegment segment = new TripSegment();
    final TripSegment arrivalSegment = resolver.createArrivalSegment(segment);
    assertThat(arrivalSegment.getVisibility()).isEqualTo(VISIBILITY_ON_MAP);
  }

  @Test public void shouldHaveNoTimeDifferenceForArrivalSegment() {
    TripSegment lastSegment = new TripSegment();

    // (M/D/Y @ h:m:s): 8 / 1 / 2002 @ 0:0:0 UTC
    lastSegment.setStartTimeInSecs(1028160000);

    // (M/D/Y @ h:m:s): 8 / 2 / 2002 @ 0:0:0 UTC
    lastSegment.setEndTimeInSecs(1028246400);

    TripSegment arrivalSegment = resolver.createArrivalSegment(lastSegment);

    assertThat(arrivalSegment.getType())
        .isEqualTo(SegmentType.ARRIVAL);
    assertThat(arrivalSegment.getStartTimeInSecs())
        .isEqualTo(arrivalSegment.getEndTimeInSecs())
        .isEqualTo(lastSegment.getEndTimeInSecs());
  }
}