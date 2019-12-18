package com.skedgo.tripkit.routing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.skedgo.tripkit.routing.TripComparators.CARBON_COST_COMPARATOR;
import static com.skedgo.tripkit.routing.TripComparators.DURATION_COMPARATOR;
import static com.skedgo.tripkit.routing.TripComparators.MONEY_COST_COMPARATOR;
import static com.skedgo.tripkit.routing.TripComparators.TIME_COMPARATOR_CHAIN;
import static com.skedgo.tripkit.routing.TripComparators.WEIGHTED_SCORE_COMPARATOR;
import static com.skedgo.tripkit.routing.TripComparators.compareLongs;

@RunWith(RobolectricTestRunner.class)
public class TripComparatorsTest {
  @Test public void compareWeightedScores() {
    final Trip lhs = mock(Trip.class);
    when(lhs.getWeightedScore()).thenReturn(4f);

    final Trip rhs = mock(Trip.class);
    when(lhs.getWeightedScore()).thenReturn(5f);

    assertThat(WEIGHTED_SCORE_COMPARATOR.compare(lhs, rhs)).isGreaterThan(0);
    assertThat(WEIGHTED_SCORE_COMPARATOR.compare(lhs, lhs)).isZero();
    assertThat(WEIGHTED_SCORE_COMPARATOR.compare(rhs, lhs)).isLessThan(0);
  }

  @Test public void timeComparatorChain() {
    final Trip trip0 = mock(Trip.class);
    when(trip0.getStartTimeInSecs()).thenReturn(2L);
    when(trip0.getEndTimeInSecs()).thenReturn(3L);

    final Trip trip1 = mock(Trip.class);
    when(trip1.getStartTimeInSecs()).thenReturn(3L);
    when(trip1.getEndTimeInSecs()).thenReturn(5L);

    final Trip trip2 = mock(Trip.class);
    when(trip2.getStartTimeInSecs()).thenReturn(3L);
    when(trip2.getEndTimeInSecs()).thenReturn(4L);

    final List<Trip> trips = new ArrayList<>(Arrays.asList(trip1, trip2, trip0));
    Collections.sort(trips, TIME_COMPARATOR_CHAIN);

    assertThat(trips).containsExactly(trip0, trip2, trip1);
  }

  @Test public void durationComparator() {
    final Trip lhs = new Trip();
    lhs.setStartTimeInSecs(2);
    lhs.setEndTimeInSecs(3);

    final Trip rhs = new Trip();
    rhs.setStartTimeInSecs(3);
    rhs.setEndTimeInSecs(5);

    assertThat(DURATION_COMPARATOR.compare(lhs, rhs)).isLessThan(0);
  }

  @Test public void moneyCostComparator() {
    {
      final Trip lhs = new Trip();
      lhs.setMoneyCost(5f);

      final Trip rhs = new Trip();
      rhs.setMoneyCost(6f);

      assertThat(MONEY_COST_COMPARATOR.compare(lhs, rhs)).isLessThan(0);
    }
    {
      final Trip lhs = new Trip();
      lhs.setMoneyCost(5f);

      final Trip rhs = new Trip();
      rhs.setMoneyCost(5f);

      assertThat(MONEY_COST_COMPARATOR.compare(lhs, rhs)).isZero();
    }
  }

  @Test public void carbonCostComparator() {
    {
      final Trip lhs = new Trip();
      lhs.setCarbonCost(5f);

      final Trip rhs = new Trip();
      rhs.setCarbonCost(6f);

      assertThat(CARBON_COST_COMPARATOR.compare(lhs, rhs)).isLessThan(0);
    }
    {
      final Trip lhs = new Trip();
      lhs.setCarbonCost(5f);

      final Trip rhs = new Trip();
      rhs.setCarbonCost(5f);

      assertThat(CARBON_COST_COMPARATOR.compare(lhs, rhs)).isZero();
    }
  }

  @Test public void shouldCompareLongsCorrectly() {
    assertThat(compareLongs(1L, 2L)).isEqualTo(-1);
    assertThat(compareLongs(1L, 1L)).isEqualTo(0);
    assertThat(compareLongs(3L, 2L)).isEqualTo(1);
  }
}