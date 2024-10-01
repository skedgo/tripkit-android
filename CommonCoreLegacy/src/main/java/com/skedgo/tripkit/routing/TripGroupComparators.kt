package com.skedgo.tripkit.routing

import org.apache.commons.collections4.ComparatorUtils
import org.apache.commons.collections4.Transformer
import org.apache.commons.collections4.comparators.ComparatorChain
import java.util.Arrays

object TripGroupComparators {
    val DISPLAY_TRIP_TRANSFORMER: Transformer<TripGroup, Trip> =
        Transformer { group -> group.displayTrip }

    /**
     * @see [Why deprecated?](https://redmine.buzzhives.com/issues/3967)
     */
    @Deprecated("")
    val CARBON_COST_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.CARBON_COST_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val MONEY_COST_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.MONEY_COST_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val WEIGHTED_SCORE_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.WEIGHTED_SCORE_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val DURATION_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.DURATION_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val END_TIME_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.END_TIME_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val START_TIME_COMPARATOR: Comparator<TripGroup> = ComparatorUtils.nullLowComparator(
        ComparatorUtils.transformedComparator(
            TripComparators.START_TIME_COMPARATOR,
            DISPLAY_TRIP_TRANSFORMER
        )
    )

    val DESC_VISIBILITY_COMPARATOR: Comparator<TripGroup> =
        ComparatorUtils.nullLowComparator { lhs, rhs -> // By descendant.
            rhs.visibility.value - lhs.visibility.value
        }

    /**
     * To sort routes by arrive-by query.
     *
     *
     * Q: Why reverse departure time?
     * A: "If it's an arrive-by query and you sort by time,
     * you don't care when the trips arrive as you told them
     * when they should arrive. What matters is when they leave.
     * Trips that leave later (while arriving before the time you selected) are better.
     * Hence: sort descending by arrival time." (Adrian said)
     *
     * @see [Discussion](https://redmine.buzzhives.com/issues/3967)
     */
    val DEPARTURE_COMPARATOR_CHAIN: Comparator<TripGroup> = ComparatorChain(
        Arrays.asList(
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
            END_TIME_COMPARATOR,
            DESC_VISIBILITY_COMPARATOR
        )
    )

    /**
     * To sort routes by leave-after query.
     *
     *
     * "In arrive-by query it's better the later they depart.
     * In a leave-after query it's better the earlier that they arrive." (Adrian said)
     *
     * @see [Discussion](https://redmine.buzzhives.com/issues/3967)
     */
    val ARRIVAL_COMPARATOR_CHAIN: Comparator<TripGroup> = ComparatorChain(
        Arrays.asList(
            END_TIME_COMPARATOR,
            ComparatorUtils.reversedComparator(START_TIME_COMPARATOR),
            DESC_VISIBILITY_COMPARATOR
        )
    )

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    fun createPreferredComparatorChain(willArriveBy: Boolean): Comparator<TripGroup> {
        return if (willArriveBy) {
            ComparatorChain(
                Arrays.asList(
                    DESC_VISIBILITY_COMPARATOR,
                    WEIGHTED_SCORE_COMPARATOR,
                    ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
                )
            )
        } else {
            ComparatorChain(
                Arrays.asList(
                    DESC_VISIBILITY_COMPARATOR,
                    WEIGHTED_SCORE_COMPARATOR,
                    END_TIME_COMPARATOR
                )
            )
        }
    }

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    fun createDurationComparatorChain(willArriveBy: Boolean): Comparator<TripGroup> {
        return if (willArriveBy) {
            ComparatorChain(
                Arrays.asList(
                    DURATION_COMPARATOR,
                    DESC_VISIBILITY_COMPARATOR,
                    ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
                )
            )
        } else {
            ComparatorChain(
                Arrays.asList(
                    DURATION_COMPARATOR,
                    DESC_VISIBILITY_COMPARATOR,
                    END_TIME_COMPARATOR
                )
            )
        }
    }

    /**
     * @param willArriveBy True, it's arrive-by query. Otherwise, leave-after query.
     */
    fun createPriceComparatorChain(willArriveBy: Boolean): Comparator<TripGroup> {
        return if (willArriveBy) {
            ComparatorChain(
                Arrays.asList(
                    MONEY_COST_COMPARATOR,
                    DESC_VISIBILITY_COMPARATOR,
                    ComparatorUtils.reversedComparator(START_TIME_COMPARATOR)
                )
            )
        } else {
            ComparatorChain(
                Arrays.asList(
                    MONEY_COST_COMPARATOR,
                    DESC_VISIBILITY_COMPARATOR,
                    END_TIME_COMPARATOR
                )
            )
        }
    }
}
