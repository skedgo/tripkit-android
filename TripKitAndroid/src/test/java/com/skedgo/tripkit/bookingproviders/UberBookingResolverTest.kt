package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.routing.TripSegment
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UberBookingResolverTest : MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val isPackageInstalled: (String) -> Boolean = mockk()
    private val getAppIntent: (String) -> Intent = mockk()
    private val resolver by lazy { UberBookingResolver(isPackageInstalled, getAppIntent) }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        initRx()
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @Test
    fun `title should be null`() {
        assertThat(resolver.getTitleForExternalAction("any action")).isNull()
    }

    @Test
    fun `should return Intent to launch Uber app directly`() {
        // Arrange
        val params: com.skedgo.tripkit.ExternalActionParams = mockk()

        val origin: Location = mockk {
            every { lat } returns 1.0
            every { lon } returns 2.0
        }

        val destination: Location = mockk {
            every { lat } returns 3.0
            every { lon } returns 4.0
        }

        val segment: TripSegment = mockk {
            every { from } returns origin
            every { to } returns destination
        }

        every { params.segment() } returns segment
        every { isPackageInstalled(UBER_PACKAGE) } returns true
        every { getAppIntent(UBER_PACKAGE) } returns Intent()

        // Act
        val testObserver: TestObserver<com.skedgo.tripkit.BookingAction> = resolver
            .performExternalActionAsync(params)
            .test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()

        val bookingAction = testObserver.events[0].first() as com.skedgo.tripkit.BookingAction
        assertThat(bookingAction.data()).isNotNull
        assertThat(bookingAction.data().data.toString())
            .isEqualTo("uber://?action=setPickup&pickup[latitude]=1.0&pickup[longitude]=2.0&dropoff[latitude]=3.0&dropoff[longitude]=4.0")
        assertThat(bookingAction.hasApp()).isTrue
    }

    @Test
    fun `should return Intent to get Uber app from Play store`() {
        // Arrange
        val params: com.skedgo.tripkit.ExternalActionParams = mockk()

        every { isPackageInstalled(UBER_PACKAGE) } returns false

        // Act
        val testObserver: TestObserver<com.skedgo.tripkit.BookingAction> = resolver
            .performExternalActionAsync(params)
            .test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()

        val bookingAction = testObserver.events[0].first() as com.skedgo.tripkit.BookingAction
        assertThat(bookingAction.data()).isNotNull
        assertThat(bookingAction.data().data.toString())
            .isEqualTo("https://play.google.com/store/apps/details?id=com.ubercab")
        assertThat(bookingAction.hasApp()).isFalse
    }
}