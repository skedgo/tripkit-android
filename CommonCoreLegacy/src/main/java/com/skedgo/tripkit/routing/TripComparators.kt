package com.skedgo.tripkit.routing

import org.apache.commons.collections4.ComparatorUtils
import org.apache.commons.collections4.comparators.ComparatorChain
import java.util.Arrays

object TripComparators {
    val START_TIME_COMPARATOR: Comparator<Trip> = ComparatorUtils.nullLowComparator { lhs, rhs ->
        compareLongs(
            lhs.startTimeInSecs,
            rhs.startTimeInSecs
        )
    }

    val END_TIME_COMPARATOR: Comparator<Trip> = ComparatorUtils.nullLowComparator { lhs, rhs ->
        compareLongs(
            lhs.endTimeInSecs,
            rhs.endTimeInSecs
        )
    }

    val TIME_COMPARATOR_CHAIN: Comparator<Trip> = ComparatorChain(
        Arrays.asList(
            START_TIME_COMPARATOR,
            END_TIME_COMPARATOR
        )
    )

    @JvmField
    val WEIGHTED_SCORE_COMPARATOR: Comparator<Trip> =
        ComparatorUtils.nullLowComparator { lhs, rhs ->
            java.lang.Float.compare(
                lhs.weightedScore,
                rhs.weightedScore
            )
        }

    val DURATION_COMPARATOR: Comparator<Trip> = ComparatorUtils.nullLowComparator { lhs, rhs ->
        java.lang.Float.compare(
            lhs.getTimeCost(),
            rhs.getTimeCost()
        )
    }

    val MONEY_COST_COMPARATOR: Comparator<Trip> = ComparatorUtils.nullLowComparator { lhs, rhs ->
        java.lang.Float.compare(
            lhs.moneyCost,
            rhs.moneyCost
        )
    }

    val CARBON_COST_COMPARATOR: Comparator<Trip> = ComparatorUtils.nullLowComparator { lhs, rhs ->
        java.lang.Float.compare(
            lhs.carbonCost,
            rhs.carbonCost
        )
    }

    fun compareLongs(lhs: Long, rhs: Long): Int {
        return if (lhs < rhs) -1 else (if (lhs == rhs) 0 else 1)
    }
}