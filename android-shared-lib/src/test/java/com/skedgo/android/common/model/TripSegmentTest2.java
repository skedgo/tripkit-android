package com.skedgo.android.common.model;

import android.content.Context;
import android.content.res.Resources;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.R;
import com.skedgo.android.common.TestRunner;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import rx.functions.Action1;

import static com.skedgo.android.common.model.TripSegment.VISIBILITY_HIDDEN;
import static com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_DETAILS;
import static com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_SUMMARY;
import static com.skedgo.android.common.model.TripSegment.VISIBILITY_ON_MAP;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripSegmentTest2 {
  private Context context;

  @Before public void setUp() {
    context = RuntimeEnvironment.application.getApplicationContext();
  }

  @Test public void parcel() {
    final TripSegment expected = new TripSegment();
    expected.setServiceDirection("Fake Service Direction");
    expected.setTerms("Fake terms");
    expected.setTransportModeId("wa_wal");
    expected.setServiceOperator("Fake Operator");
    expected.setRealTime(true);
    expected.setDurationWithoutTraffic(123L);
    expected.setTemplateHashCode(12L);
    expected.setModeInfo(new ModeInfo());
    expected.setBooking(ImmutableBooking.builder().build());
    expected.setPlatform("Platform 2");
    expected.setStopCount(7);

    final TripSegment actual = TripSegment.CREATOR.createFromParcel(Utils.parcel(expected));

    assertThat(expected.getServiceDirection()).isEqualTo(actual.getServiceDirection());
    assertThat(expected.getTerms()).isEqualTo(actual.getTerms());
    assertThat(expected.getTransportModeId()).isEqualTo(actual.getTransportModeId());
    assertThat(expected.getServiceOperator()).isEqualTo(actual.getServiceOperator());
    assertThat(actual.isRealTime()).isTrue();
    assertThat(actual.getDurationWithoutTraffic()).isEqualTo(expected.getDurationWithoutTraffic());
    assertThat(actual.getTemplateHashCode()).isEqualTo(expected.getTemplateHashCode());
    assertThat(actual.getModeInfo()).isNotNull();
    assertThat(actual.getBooking()).isNotNull();
    assertThat(actual.getPlatform()).isEqualTo(expected.getPlatform());
    assertThat(actual.getStopCount()).isEqualTo(expected.getStopCount());
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

  @Test public void shouldNotifyStartTimeChangedEvent() {
    TripSegment segment = new TripSegment();

    // (M/D/Y @ h:m:s): 8 / 2 / 2002 @ 0:0:0 UTC
    int expectedStartTimeInSecs1 = 1028246400;
    segment.setStartTimeInSecs(expectedStartTimeInSecs1);

    final AtomicLong timeRecorder = new AtomicLong();
    segment.whenTimeChanged().subscribe(new Action1<TripSegment>() {
      @Override
      public void call(TripSegment segment) {
        timeRecorder.set(segment.getStartTimeInSecs());
      }
    });

    assertThat(timeRecorder.get())
        .isEqualTo(expectedStartTimeInSecs1);

    // (M/D/Y @ h:m:s): 8 / 1 / 2002 @ 0:0:0 UTC
    int expectedStartTimeInSecs2 = 1028160000;
    segment.setStartTimeInSecs(expectedStartTimeInSecs2);

    assertThat(timeRecorder.get())
        .isEqualTo(expectedStartTimeInSecs2);
  }

  @Test public void shouldNotifyEndTimeChangedEvent() {
    TripSegment segment = new TripSegment();

    // (M/D/Y @ h:m:s): 8 / 2 / 2002 @ 0:0:0 UTC
    int expectedEndTimeInSecs1 = 1028246400;
    segment.setEndTimeInSecs(expectedEndTimeInSecs1);

    final AtomicLong timeRecorder = new AtomicLong();
    segment.whenTimeChanged().subscribe(new Action1<TripSegment>() {
      @Override
      public void call(TripSegment segment) {
        timeRecorder.set(segment.getEndTimeInSecs());
      }
    });

    assertThat(timeRecorder.get())
        .isEqualTo(expectedEndTimeInSecs1);

    // (M/D/Y @ h:m:s): 8 / 1 / 2002 @ 0:0:0 UTC
    int expectedEndTimeInSecs2 = 1028160000;
    segment.setEndTimeInSecs(expectedEndTimeInSecs2);

    assertThat(timeRecorder.get())
        .isEqualTo(expectedEndTimeInSecs2);
  }

  /**
   * A {@link ServiceStop} can let us know which bus/train segment it belongs to.
   * When updating stop list for a bus/train segment, we must change the segment of each of stop.
   */
  @Test public void shouldUpdateSegmentForServiceStops() {
    final TripSegment segment = new TripSegment();

    List<ServiceStop> stops = Arrays.asList(
        new ServiceStop(),
        new ServiceStop(),
        new ServiceStop()
    );

    segment.stops().put(stops);

    assertThat(stops).are(new Condition<ServiceStop>() {
      @Override
      public boolean matches(ServiceStop value) {
        return segment == value.segment().value();
      }
    });
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

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isNull();
  }

  @Test public void displayNotesShouldHaveDurationTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("Stay on train for <DURATION>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("Stay on train for 1min");
  }

  @Test public void displayNotesShouldHavePlatformTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <PLATFORM>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setPlatform("Platform 2");

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("To City Circle. Platform: Platform 2");
  }

  @Test public void shouldRemovePlatformTemplateIfNoPlatformAvailable() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <PLATFORM>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setPlatform(null);

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("To City Circle. ");
  }

  @Test public void displayNotesShouldHaveStopsTemplateProcessed() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(3);

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("To City Circle. 3 stops");
  }

  @Test public void shouldRemoveStopsTemplateIfNoStopAvailable() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(0);

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("To City Circle. ");
  }

  @Test public void shouldRemoveStopsTemplateIfStopCountIsLessThanZero() {
    final TripSegment segment = new TripSegment();
    segment.setNotes("To City Circle. <STOPS>");
    segment.setStartTimeInSecs(TimeUnit.MINUTES.toSeconds(2));
    segment.setEndTimeInSecs(TimeUnit.MINUTES.toSeconds(3));
    segment.setStopCount(-2);

    assertThat(segment.getDisplayNotes(context.getResources()))
        .isEqualTo("To City Circle. ");
  }
}