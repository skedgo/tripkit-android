package com.skedgo.tripkit.routing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.routing.GroupVisibility.COMPACT
import com.skedgo.tripkit.routing.GroupVisibility.FULL
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripGroupTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun addAsDisplayTrip() {
        val tripGroup = TripGroup()
        val tripA = mockk<Trip>()
        tripA.apply {
            every { tripId } returns 1L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripB = mockk<Trip>()
        tripB.apply {
            every { tripId } returns 3L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripC = mockk<Trip>()
        tripC.apply {
            every { tripId } returns 6L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripD = Trip()

        tripGroup.setTrips(arrayListOf(tripA, tripB, tripC))
        tripGroup.addAsDisplayTrip(tripD)

        assertThat(tripGroup.getTrips()).contains(tripD)
        assertThat(tripGroup.getDisplayTrip()).isSameAs(tripD)
        assertThat(tripD.tripId).isEqualTo(7L)
    }

    @Test(expected = IllegalStateException::class)
    fun addAsDisplayTrip_throwException() {
        val tripGroup = TripGroup()
        val tripA = mockk<Trip>()
        tripA.apply {
            every { tripId } returns 1L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripB = mockk<Trip>()
        tripB.apply {
            every { tripId } returns 3L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripC = mockk<Trip>()
        tripC.apply {
            every { tripId } returns 6L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        tripGroup.setTrips(arrayListOf(tripA, tripB, tripC))
        tripGroup.addAsDisplayTrip(tripC)
    }

    @Test
    fun fullIsGreaterThanCompact() {
        assertThat(FULL.value).isGreaterThan(COMPACT.value)
    }

    @Test
    fun arrangedByFullCompact() {
        val fullGroup = TripGroup().apply { setVisibility(FULL) }
        val compactGroup = TripGroup().apply { setVisibility(COMPACT) }

        val groups = listOf(compactGroup, fullGroup).sortedWith(TripGroupComparators.DESC_VISIBILITY_COMPARATOR)

        assertThat(groups).containsExactly(fullGroup, compactGroup)
    }

    @Test
    fun arrivalComparatorChain() {
        val group0 = TripGroup().apply {
            addTrip(Trip().apply {
                startTimeInSecs = 2
                endTimeInSecs = 3
            })
        }

        val group1 = TripGroup().apply {
            addTrip(Trip().apply {
                startTimeInSecs = 2
                endTimeInSecs = 4
            })
            setVisibility(COMPACT)
        }

        val group2 = TripGroup().apply {
            addTrip(Trip().apply {
                startTimeInSecs = 2
                endTimeInSecs = 6
            })
        }

        val group3 = TripGroup().apply {
            addTrip(Trip().apply {
                startTimeInSecs = 2
                endTimeInSecs = 4
            })
            setVisibility(FULL)
        }

        val groups = listOf(group1, group0, group2, group3).sortedWith(TripGroupComparators.ARRIVAL_COMPARATOR_CHAIN)

        assertThat(groups).containsExactly(group0, group3, group1, group2)
    }

    @Test
    fun changeDisplayTrip() {
        val tripGroup = TripGroup()
        val tripA = mockk<Trip>()
        tripA.apply {
            every { tripId } returns 1L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripB = mockk<Trip>()
        tripB.apply {
            every { tripId } returns 2L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripC = mockk<Trip>()
        tripC.apply {
            every { tripId } returns 3L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        tripGroup.setTrips(arrayListOf(tripA, tripB, tripC))

        tripGroup.changeDisplayTrip(tripB)
        assertThat(tripGroup.getDisplayTrip()).isSameAs(tripB)

        tripGroup.changeDisplayTrip(tripA)
        assertThat(tripGroup.getDisplayTrip()).isSameAs(tripA)

        tripGroup.changeDisplayTrip(tripC)
        assertThat(tripGroup.getDisplayTrip()).isSameAs(tripC)
    }

    @Test(expected = IllegalStateException::class)
    fun changeDisplayTrip_throwException() {
        val tripGroup = TripGroup()
        val tripA = mockk<Trip>()
        tripA.apply {
            every { tripId } returns 1L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        val tripB = mockk<Trip>()
        tripB.apply {
            every { tripId } returns 2L
            every { group } returns tripGroup
            every { group = any() } just Runs
        }

        tripGroup.setTrips(arrayListOf(tripA, tripB))

        val c = mockk<Trip>()
        every { c.tripId } returns 3L

        tripGroup.changeDisplayTrip(c)
    }
}