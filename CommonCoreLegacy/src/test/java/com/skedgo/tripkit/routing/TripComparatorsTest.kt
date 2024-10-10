package com.skedgo.tripkit.routing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.routing.TripComparators.CARBON_COST_COMPARATOR
import com.skedgo.tripkit.routing.TripComparators.DURATION_COMPARATOR
import com.skedgo.tripkit.routing.TripComparators.MONEY_COST_COMPARATOR
import com.skedgo.tripkit.routing.TripComparators.TIME_COMPARATOR_CHAIN
import com.skedgo.tripkit.routing.TripComparators.WEIGHTED_SCORE_COMPARATOR
import com.skedgo.tripkit.routing.TripComparators.compareLongs
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripComparatorsTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun compareWeightedScores() {
        val lhs = mockk<Trip>()
        every { lhs.weightedScore } returns 5f

        val rhs = mockk<Trip>()
        every { rhs.weightedScore } returns 4f

        val a = lhs.weightedScore
        val b = rhs.weightedScore

        assertThat(WEIGHTED_SCORE_COMPARATOR.compare(lhs, rhs)).isGreaterThan(0)
        assertThat(WEIGHTED_SCORE_COMPARATOR.compare(lhs, lhs)).isZero()
        assertThat(WEIGHTED_SCORE_COMPARATOR.compare(rhs, lhs)).isLessThan(0)
    }

    @Test
    fun timeComparatorChain() {
        val trip0 = mockk<Trip>()
        every { trip0.startTimeInSecs } returns 2L
        every { trip0.endTimeInSecs } returns 3L

        val trip1 = mockk<Trip>()
        every { trip1.startTimeInSecs } returns 3L
        every { trip1.endTimeInSecs } returns 5L

        val trip2 = mockk<Trip>()
        every { trip2.startTimeInSecs } returns 3L
        every { trip2.endTimeInSecs } returns 4L

        val trips = listOf(trip1, trip2, trip0).sortedWith(TIME_COMPARATOR_CHAIN)

        assertThat(trips).containsExactly(trip0, trip2, trip1)
    }

    @Test
    fun durationComparator() {
        val lhs = Trip()
        lhs.startTimeInSecs = 2L
        lhs.endTimeInSecs = 3L

        val rhs = Trip()
        rhs.startTimeInSecs = 3L
        rhs.endTimeInSecs = 5L

        assertThat(DURATION_COMPARATOR.compare(lhs, rhs)).isLessThan(0)
    }

    @Test
    fun moneyCostComparator() {
        val lhs = Trip()
        lhs.moneyCost = 5f

        val rhs = Trip()
        rhs.moneyCost = 6f

        assertThat(MONEY_COST_COMPARATOR.compare(lhs, rhs)).isLessThan(0)

        val lhsEqual = Trip()
        lhsEqual.moneyCost = 5f

        val rhsEqual = Trip()
        rhsEqual.moneyCost = 5f

        assertThat(MONEY_COST_COMPARATOR.compare(lhsEqual, rhsEqual)).isZero()
    }

    @Test
    fun carbonCostComparator() {
        val lhs = Trip()
        lhs.carbonCost = 5f

        val rhs = Trip()
        rhs.carbonCost = 6f

        assertThat(CARBON_COST_COMPARATOR.compare(lhs, rhs)).isLessThan(0)

        val lhsEqual = Trip()
        lhsEqual.carbonCost = 5f

        val rhsEqual = Trip()
        rhsEqual.carbonCost = 5f

        assertThat(CARBON_COST_COMPARATOR.compare(lhsEqual, rhsEqual)).isZero()
    }

    @Test
    fun shouldCompareLongsCorrectly() {
        assertThat(compareLongs(1L, 2L)).isEqualTo(-1)
        assertThat(compareLongs(1L, 1L)).isEqualTo(0)
        assertThat(compareLongs(3L, 2L)).isEqualTo(1)
    }
}