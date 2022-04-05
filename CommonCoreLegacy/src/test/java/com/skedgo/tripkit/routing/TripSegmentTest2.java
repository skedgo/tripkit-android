package com.skedgo.tripkit.routing;

import android.content.Context;
import android.content.res.Resources;

import com.skedgo.tripkit.common.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static com.skedgo.tripkit.routing.Visibilities.VISIBILITY_HIDDEN;
import static com.skedgo.tripkit.routing.Visibilities.VISIBILITY_IN_DETAILS;
import static com.skedgo.tripkit.routing.Visibilities.VISIBILITY_IN_SUMMARY;
import static com.skedgo.tripkit.routing.Visibilities.VISIBILITY_ON_MAP;

@RunWith(RobolectricTestRunner.class)
public class TripSegmentTest2 {
  private Context context;

  @Before public void setUp() {
    context = ApplicationProvider.getApplicationContext().getApplicationContext();
  }

  @Test public void shouldShowHideSegmentProperly() {
    TripSegment segment = new TripSegment();
    segment.setVisibility(VISIBILITY_IN_DETAILS);
    assertFalse(segment.isVisibleInContext(null));
    assertTrue(segment.isVisibleInContext(VISIBILITY_IN_DETAILS));
    assertFalse(segment.isVisibleInContext(VISIBILITY_IN_SUMMARY));
    assertFalse(segment.isVisibleInContext(VISIBILITY_ON_MAP));
    assertFalse(segment.isVisibleInContext(VISIBILITY_HIDDEN));

    segment.setVisibility(VISIBILITY_IN_SUMMARY);
    assertFalse(segment.isVisibleInContext(null));
    assertTrue(segment.isVisibleInContext(VISIBILITY_IN_DETAILS));
    assertTrue(segment.isVisibleInContext(VISIBILITY_IN_SUMMARY));
    assertTrue(segment.isVisibleInContext(VISIBILITY_ON_MAP));
    assertFalse(segment.isVisibleInContext(VISIBILITY_HIDDEN));

    segment.setVisibility(VISIBILITY_ON_MAP);
    assertFalse(segment.isVisibleInContext(null));
    assertTrue(segment.isVisibleInContext(VISIBILITY_IN_DETAILS));
    assertTrue(segment.isVisibleInContext(VISIBILITY_IN_SUMMARY));
    assertTrue(segment.isVisibleInContext(VISIBILITY_ON_MAP));
    assertFalse(segment.isVisibleInContext(VISIBILITY_HIDDEN));

    segment.setVisibility(VISIBILITY_HIDDEN);
    assertFalse(segment.isVisibleInContext(null));
    assertFalse(segment.isVisibleInContext(VISIBILITY_IN_DETAILS));
    assertFalse(segment.isVisibleInContext(VISIBILITY_IN_SUMMARY));
    assertFalse(segment.isVisibleInContext(VISIBILITY_ON_MAP));
    assertFalse(segment.isVisibleInContext(VISIBILITY_HIDDEN));
  }

  @Test public void shouldGiveCorrectRealtimeStatusText() {
    Resources resources = context.getResources();
    {
      final ModeInfo car = new ModeInfo();
      car.setLocalIconName("car");

      final TripSegment segment = new TripSegment();
      segment.setRealTime(true);
      segment.setModeInfo(car);
      assertThat(segment.getRealTimeStatusText(resources))
          .isEqualTo(resources.getString(R.string.live_traffic));
    }
    {
      final ModeInfo car = new ModeInfo();
      car.setLocalIconName("car");

      final TripSegment segment = new TripSegment();
      segment.setRealTime(false);
      segment.setModeInfo(car);
      assertThat(segment.getRealTimeStatusText(resources)).isNullOrEmpty();
    }
    {
      final ModeInfo train = new ModeInfo();
      train.setLocalIconName("train");

      final TripSegment segment = new TripSegment();
      segment.setRealTime(true);
      segment.setModeInfo(train);
      assertThat(segment.getRealTimeStatusText(resources))
          .isEqualTo(resources.getString(R.string.real_minustime));
    }
    {
      final ModeInfo train = new ModeInfo();
      train.setLocalIconName("train");

      final TripSegment segment = new TripSegment();
      segment.setRealTime(false);
      segment.setModeInfo(train);
      assertThat(segment.getRealTimeStatusText(resources)).isNullOrEmpty();
    }
  }

  @Test public void getDarkVehicleIcon() {
    final ModeInfo ferry = new ModeInfo();
    ferry.setLocalIconName("ferry");

    final TripSegment segment = new TripSegment();
    segment.setModeInfo(ferry);
    segment.setRealTime(true);

    assertThat(segment.getDarkVehicleIcon())
        .describedAs("Should give correct icon for realtime ferry")
        .isEqualTo(R.drawable.ic_ferry_realtime);

    segment.setRealTime(false);
    assertThat(segment.getDarkVehicleIcon())
        .describedAs("Should give correct icon for non-realtime ferry")
        .isEqualTo(R.drawable.ic_ferry);
  }

  @Test public void shouldHandleNullityForDisplayNotes() {
    final TripSegment segment = new TripSegment();
    segment.setNotes(null);
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));

    assertThat(segment.getDisplayNotes(context, true))
        .isNull();
  }

  @Test public void displayNotesShouldHaveDurationTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("Stay on train for <DURATION>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("Stay on train for 1min");
  }

  @Test public void displayNotesShouldHavePlatformTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <PLATFORM>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setPlatform("Platform 2");

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("To City Circle. Platform: Platform 2");
  }

  @Test public void shouldRemovePlatformTemplateIfNoPlatformAvailable() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <PLATFORM>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setPlatform(null);

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("To City Circle. ");
  }

  @Test public void displayNotesShouldHaveStopsTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(3);

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("To City Circle. 3 stops");
  }

  @Test public void shouldRemoveStopsTemplateIfNoStopAvailable() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(0);

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("To City Circle. ");
  }

  @Test public void shouldRemoveStopsTemplateIfStopCountIsLessThanZero() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(-2);

    assertThat(segment.getDisplayNotes(context, true))
        .isEqualTo("To City Circle. ");
  }
}