package com.skedgo.android.common.model;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.Arrays;
import java.util.Comparator;

public final class TripGroupComparators {
  public static final Transformer<TripGroup, Trip> DISPLAY_TRIP_TRANSFORMER =
      new Transformer<TripGroup, Trip>() {
        @Override
        public Trip transform(TripGroup group) {
          return group.getDisplayTrip();
        }
      };

  /**
   * @see <a href="https://redmine.buzzhives.com/issues/3967">Why deprecated?</a>
   */
  @Deprecated
  public static final Comparator<TripGroup> CARBON_COST_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.CARBON_COST_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> MONEY_COST_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.MONEY_COST_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> WEIGHTED_SCORE_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.WEIGHTED_SCORE_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> DURATION_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.DURATION_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> END_TIME_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.END_TIME_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> START_TIME_COMPARATOR =
      ComparatorUtils.nullLowComparator(ComparatorUtils.transformedComparator(
          TripComparators.START_TIME_COMPARATOR,
          DISPLAY_TRIP_TRANSFORMER
      ));

  public static final Comparator<TripGroup> DESC_VISIBILITY_COMPARATOR =
      ComparatorUtils.nullLowComparator(new Comparator<TripGroup>() {
        @Override
        public int compare(TripGroup lhs, TripGroup rhs) {
          // By descendant.
          return rhs.getVisibility().value - lhs.getVisibility().value;
        }
      });

  /**
   * To sort routes by arrive-by query.
   * <p/>
   * Q: Why reverse departure time?
   * A: "If it's an arrive-by query and you sort by time,
   * you don't care when the trips arrive as you told them
   * when they should arrive. What matters is when they leave.
   * Trips that leave later (while arriving before the time you selected) are better.
   * Hence: sort descending by arrival time." (Adrian said)
   *
   * @see <a href="https://redmine.buzzhives.com/issues/3967">Discussion</a>
   */
  public static final Comparator<TripGroup> DEPARTURE_COMPARATOR_CHAIN =
      new ComparatorChain<>(Arrays.asList(
          ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
          END_TIME_COMPARATOR,
          DESC_VISIBILITY_COMPARATOR
      ));

  /**
   * To sort routes by leave-after query.
   * <p/>
   * "In arrive-by query it's better the later they depart.
   * In a leave-after query it's better the earlier that they arrive." (Adrian said)
   *
   * @see <a href="https://redmine.buzzhives.com/issues/3967">Discussion</a>
   */
  public static final Comparator<TripGroup> ARRIVAL_COMPARATOR_CHAIN =
      new ComparatorChain<>(Arrays.asList(
          END_TIME_COMPARATOR,
          ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
          DESC_VISIBILITY_COMPARATOR
      ));

  private TripGroupComparators() {}

  /**
   * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
   */
  public static Comparator<TripGroup> createPreferredComparatorChain(boolean willArriveBy) {
    if (willArriveBy) {
      return new ComparatorChain<>(Arrays.asList(
          DESC_VISIBILITY_COMPARATOR,
          WEIGHTED_SCORE_COMPARATOR,
          ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
      ));
    } else {
      return new ComparatorChain<>(Arrays.asList(
          DESC_VISIBILITY_COMPARATOR,
          WEIGHTED_SCORE_COMPARATOR,
          END_TIME_COMPARATOR
      ));
    }
  }

  /**
   * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
   */
  public static Comparator<TripGroup> createDurationComparatorChain(boolean willArriveBy) {
    if (willArriveBy) {
      return new ComparatorChain<>(Arrays.asList(
          DURATION_COMPARATOR,
          DESC_VISIBILITY_COMPARATOR,
          ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
      ));
    } else {
      return new ComparatorChain<>(Arrays.asList(
          DURATION_COMPARATOR,
          DESC_VISIBILITY_COMPARATOR,
          END_TIME_COMPARATOR
      ));
    }
  }

  /**
   * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
   */
  public static Comparator<TripGroup> createPriceComparatorChain(boolean willArriveBy) {
    if (willArriveBy) {
      return new ComparatorChain<>(Arrays.asList(
          MONEY_COST_COMPARATOR,
          DESC_VISIBILITY_COMPARATOR,
          ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
      ));
    } else {
      return new ComparatorChain<>(Arrays.asList(
          MONEY_COST_COMPARATOR,
          DESC_VISIBILITY_COMPARATOR,
          END_TIME_COMPARATOR
      ));
    }
  }
}
