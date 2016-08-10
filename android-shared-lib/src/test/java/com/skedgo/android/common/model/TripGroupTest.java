package com.skedgo.android.common.model;

import com.skedgo.android.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.skedgo.android.common.model.TripGroup.Visibility.COMPACT;
import static com.skedgo.android.common.model.TripGroup.Visibility.FULL;
import static com.skedgo.android.common.model.TripGroup.Visibility.GONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripGroupTest {
  @Test public void shouldParcelProperly() {
    TripGroup expected = new TripGroup();
    TripGroup actual = TripGroup.CREATOR.createFromParcel(Utils.parcel(expected));
  }

  @Test public void parcelVisibility() {
    {
      TripGroup expected = new TripGroup();
      expected.visibility().put(FULL);
      TripGroup actual = TripGroup.CREATOR.createFromParcel(Utils.parcel(expected));
      assertThat(actual.visibility().value()).isEqualTo(FULL);
    }
    {
      TripGroup expected = new TripGroup();
      expected.visibility().put(COMPACT);
      TripGroup actual = TripGroup.CREATOR.createFromParcel(Utils.parcel(expected));
      assertThat(actual.visibility().value()).isEqualTo(COMPACT);
    }
    {
      TripGroup expected = new TripGroup();
      expected.visibility().put(GONE);
      TripGroup actual = TripGroup.CREATOR.createFromParcel(Utils.parcel(expected));
      assertThat(actual.visibility().value()).isEqualTo(GONE);
    }
  }

  @Test public void addAsDisplayTrip() {
    final Trip a = mock(Trip.class);
    when(a.getId()).thenReturn(1L);
    final Trip b = mock(Trip.class);
    when(b.getId()).thenReturn(3L);
    final Trip c = mock(Trip.class);
    when(c.getId()).thenReturn(6L);

    final Trip d = new Trip();
    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<>(Arrays.asList(a, b, c)));
    group.addAsDisplayTrip(d);

    assertThat(group.getTrips()).contains(d);
    assertThat(group.getDisplayTrip()).isSameAs(d);
    assertThat(d.getId()).isEqualTo(7L);
  }

  @Test(expected = IllegalStateException.class)
  public void addAsDisplayTrip_throwException() {
    final Trip a = mock(Trip.class);
    when(a.getId()).thenReturn(1L);
    final Trip b = mock(Trip.class);
    when(b.getId()).thenReturn(3L);
    final Trip c = mock(Trip.class);
    when(c.getId()).thenReturn(6L);

    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<>(Arrays.asList(a, b, c)));
    group.addAsDisplayTrip(c);
  }

  @Test public void fullCompactGoneOrder() {
    assertThat(FULL.value).isGreaterThan(COMPACT.value);
    assertThat(COMPACT.value).isGreaterThan(GONE.value);
  }

  @Test public void arrangedByFullCompactGone() {
    TripGroup fullGroup = new TripGroup();
    fullGroup.visibility().put(FULL);

    TripGroup compactGroup = new TripGroup();
    compactGroup.visibility().put(COMPACT);

    TripGroup goneGroup = new TripGroup();
    goneGroup.visibility().put(GONE);

    List<TripGroup> groups = new ArrayList<>(Arrays.asList(
        compactGroup,
        goneGroup,
        fullGroup
    ));

    Collections.sort(groups, TripGroup.Comparators.DESC_VISIBILITY_COMPARATOR);

    assertThat(groups).containsExactly(
        fullGroup,
        compactGroup,
        goneGroup
    );
  }

  @Test public void arrivalComparatorChain() {
    TripGroup group0 = new TripGroup();
    Trip trip0 = new Trip();
    trip0.setStartTimeInSecs(2);
    trip0.setEndTimeInSecs(3);
    group0.addTrip(trip0);

    TripGroup group1 = new TripGroup();
    Trip trip1 = new Trip();
    trip1.setStartTimeInSecs(2);
    trip1.setEndTimeInSecs(4);
    group1.addTrip(trip1);
    group1.visibility().put(COMPACT);

    TripGroup group2 = new TripGroup();
    Trip trip2 = new Trip();
    trip2.setStartTimeInSecs(2);
    trip2.setEndTimeInSecs(6);
    group2.addTrip(trip2);

    TripGroup group3 = new TripGroup();
    Trip trip3 = new Trip();
    trip3.setStartTimeInSecs(2);
    trip3.setEndTimeInSecs(4);
    group3.addTrip(trip3);
    group3.visibility().put(FULL);

    List<TripGroup> groups = new ArrayList<>(Arrays.asList(
        group1, group0, group2, group3
    ));
    Collections.sort(groups, TripGroup.Comparators.ARRIVAL_COMPARATOR_CHAIN);
    assertThat(groups).containsExactly(
        group0, group3, group1, group2
    );
  }

  @Test public void changeDisplayTrip() {
    final Trip a = mock(Trip.class);
    when(a.getId()).thenReturn(1L);
    final Trip b = mock(Trip.class);
    when(b.getId()).thenReturn(2L);
    final Trip c = mock(Trip.class);
    when(c.getId()).thenReturn(3L);
    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<>(Arrays.asList(a, b, c)));
    group.changeDisplayTrip(b);
    assertThat(group.getDisplayTrip()).isSameAs(b);
    group.changeDisplayTrip(a);
    assertThat(group.getDisplayTrip()).isSameAs(a);
    group.changeDisplayTrip(c);
    assertThat(group.getDisplayTrip()).isSameAs(c);
  }

  @Test(expected = IllegalStateException.class)
  public void changeDisplayTrip_throwException() {
    final Trip a = mock(Trip.class);
    when(a.getId()).thenReturn(1L);
    final Trip b = mock(Trip.class);
    when(b.getId()).thenReturn(2L);
    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<>(Arrays.asList(a, b)));

    final Trip c = mock(Trip.class);
    when(c.getId()).thenReturn(3L);
    group.changeDisplayTrip(c);
  }
}