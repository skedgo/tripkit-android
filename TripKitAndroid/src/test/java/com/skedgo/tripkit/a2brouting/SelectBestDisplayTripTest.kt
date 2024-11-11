package com.skedgo.tripkit.a2brouting

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Arrays

@RunWith(AndroidJUnit4::class)
class SelectBestDisplayTripTest {

    @Test
    fun `select display trip having lowest weighted score`() {
        // Arrange
        val tripA = Trip().apply {
            tripId = 0
            weightedScore = 3f
        }

        val tripB = Trip().apply {
            tripId = 1
            weightedScore = 1f
        }

        val tripC = Trip().apply {
            tripId = 2
            weightedScore = 2f
        }

        val group = TripGroup().apply {
            setTrips(ArrayList(listOf(tripA, tripB, tripC)))
        }

        // Act
        val actual = SelectBestDisplayTrip().apply(group)

        // Assert
        assertThat(actual).isNotNull.isSameAs(group)
        assertThat(actual.trips)
            .describedAs("Sort trips by weighted score")
            .containsExactly(tripB, tripC, tripA)
        assertThat(actual.displayTripId)
            .describedAs("Select display trip having lowest weighted score")
            .isEqualTo(tripB.tripId)
        assertThat(actual.displayTrip)
            .describedAs("Select display trip having lowest weighted score")
            .isSameAs(tripB)
    }

    @Test
    fun `do nothing if no trips available - null`() {
        // Arrange
        val group = TripGroup().apply {
            setTrips(null)
        }

        // Act
        val actual = SelectBestDisplayTrip().apply(group)

        // Assert
        assertThat(actual).isNotNull.isSameAs(group)
    }

    @Test
    fun `do nothing if no trips available - empty`() {
        // Arrange
        val group = TripGroup().apply {
            setTrips(ArrayList(emptyList()))
        }

        // Act
        val actual = SelectBestDisplayTrip().apply(group)

        // Assert
        assertThat(actual).isNotNull.isSameAs(group)
    }
}