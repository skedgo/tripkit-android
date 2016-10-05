package com.skedgo.android.common.model;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.Arrays;
import java.util.Comparator;

public final class TripComparators {
  public static final Comparator<Trip> START_TIME_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return compareLongs(
              lhs.getStartTimeInSecs(),
              rhs.getStartTimeInSecs()
          );
        }
      });

  public static final Comparator<Trip> END_TIME_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return compareLongs(
              lhs.getEndTimeInSecs(),
              rhs.getEndTimeInSecs()
          );
        }
      });

  public static final Comparator<Trip> TIME_COMPARATOR_CHAIN =
      new ComparatorChain<>(Arrays.asList(
          START_TIME_COMPARATOR,
          END_TIME_COMPARATOR
      ));

  public static final Comparator<Trip> WEIGHTED_SCORE_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return Float.compare(lhs.getWeightedScore(), rhs.getWeightedScore());
        }
      });

  public static final Comparator<Trip> DURATION_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return Float.compare(lhs.getTimeCost(), rhs.getTimeCost());
        }
      });

  public static final Comparator<Trip> MONEY_COST_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return Float.compare(lhs.getMoneyCost(), rhs.getMoneyCost());
        }
      });

  public static final Comparator<Trip> CARBON_COST_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<Trip>() {
        @Override
        public int compare(Trip lhs, Trip rhs) {
          return Float.compare(lhs.getCarbonCost(), rhs.getCarbonCost());
        }
      });

  private TripComparators() {}

  public static int compareLongs(long lhs, long rhs) {
    return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
  }
}