package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.bookingproviders.BookingResolver.Companion.FLITWAYS
import com.skedgo.tripkit.bookingproviders.BookingResolver.Companion.LYFT
import com.skedgo.tripkit.bookingproviders.BookingResolver.Companion.OTHERS
import com.skedgo.tripkit.bookingproviders.BookingResolver.Companion.SMS
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.geocoding.ReverseGeocodable
import com.skedgo.tripkit.routing.TripSegment
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.amshove.kluent.internal.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

@RunWith(AndroidJUnit4::class)
class BookingResolverImplTest : MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val packageManager: PackageManager = mockk()
    private val geocoderFactory: ReverseGeocodable = mockk()
    private lateinit var bookingResolver: BookingResolverImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
        bookingResolver = BookingResolverImpl(
            mockk(),
            packageManager,
            geocoderFactory
        )
    }

    @After
    fun teardown() {
        tearDownRx()
    }

    @Test
    fun `has Lyft app installed`() {
        every {
            packageManager.getPackageInfo(
                "me.lyft.android",
                PackageManager.GET_ACTIVITIES
            )
        } returns PackageInfo()

        val params = mockk<ExternalActionParams> {
            every { action() } returns "lyft"
            every { segment() } returns mockk()
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val expectedAction = BookingAction.builder()
            .bookingProvider(LYFT)
            .hasApp(true)
            .data(Intent(Intent.ACTION_VIEW, Uri.parse("lyft://")))
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertEquals(
            testObserver.values()[0].data().data.toString(),
            expectedAction.data().data.toString()
        )
    }

    @Test
    fun `has no Lyft app installed`() {
        every {
            packageManager.getPackageInfo(
                "me.lyft.android",
                PackageManager.GET_ACTIVITIES
            )
        } throws PackageManager.NameNotFoundException()

        val params = mockk<ExternalActionParams> {
            every { action() } returns "lyft"
            every { segment() } returns mockk()
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val expectedData = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=me.lyft.android")
        )
        val expectedAction = BookingAction.builder()
            .bookingProvider(LYFT)
            .hasApp(false)
            .data(expectedData)
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertEquals(
            testObserver.values()[0].data().data,
            expectedAction.data().data
        )
    }

    @Test
    fun `handle Flitways without partner key`() {

        every { geocoderFactory.getAddress(1.0, 2.0) } returns Observable.just("A")
        every { geocoderFactory.getAddress(3.0, 4.0) } returns Observable.just("B")

        val segment = mockk<TripSegment> {
            every { from } returns Location(1.0, 2.0)
            every { to } returns Location(3.0, 4.0)
            every { timeZone } returns "Australia/Sydney"
            every { startTimeInSecs } returns MILLISECONDS.toSeconds(Calendar.getInstance().timeInMillis)
        }
        val params = mockk<ExternalActionParams> {
            every { action() } returns "flitways"
            every { segment() } returns segment
            every { flitWaysPartnerKey() } returns "25251325"
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val expectedAction = BookingAction.builder()
            .bookingProvider(FLITWAYS)
            .hasApp(false)
            .data(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://flitways.com/api/link?trip_date=11%2F11%2F2024%2008%3A44%20PM&key=25251325&pickup=A&destination=B")
                )
            )
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertTrue(
            areUrlsEquivalent(
                testObserver.values()[0].data().data.toString(),
                expectedAction.data().data.toString()
            )
        )
    }

    @Test
    fun `handle Flitways with partner key`() {
        every { geocoderFactory.getAddress(any(), any()) } answers {
            val lat = firstArg<Double>()
            if (lat == 1.0) Observable.just("A") else Observable.just("B")
        }

        val segment = mockk<TripSegment> {
            every { from } returns Location(1.0, 2.0)
            every { to } returns Location(3.0, 4.0)
            every { timeZone } returns "Australia/Sydney"
            every { startTimeInSecs } returns TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().timeInMillis)
        }

        val params = mockk<ExternalActionParams> {
            every { action() } returns "flitways"
            every { segment() } returns segment
            every { flitWaysPartnerKey() } returns "25251325"
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val url = "https://flitways.com/api/link?trip_date=&key=25251325&pickup=A&destination=B"
        val expectedAction = BookingAction.builder()
            .bookingProvider(FLITWAYS)
            .hasApp(false)
            .data(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertTrue(
            areUrlsEquivalent(
                testObserver.values()[0].data().data.toString(),
                expectedAction.data().data.toString()
            )
        )
    }

    @Test
    fun `handle SMS without body`() {
        val params = mockk<ExternalActionParams> {
            every { action() } returns "sms:12345"
            every { segment() } returns mockk()
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:12345"))
        val expectedAction = BookingAction.builder()
            .bookingProvider(SMS)
            .hasApp(false)
            .data(intent)
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertEquals(
            testObserver.values()[0].data().data.toString(),
            expectedAction.data().data.toString()
        )
    }

    @Test
    fun `handle tel action`() {
        val params = mockk<ExternalActionParams> {
            every { action() } returns "tel:12345"
            every { segment() } returns mockk()
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()

        val expectedAction = BookingAction.builder()
            .bookingProvider(OTHERS)
            .hasApp(false)
            .data(Intent(Intent.ACTION_VIEW, Uri.parse("tel:12345")))
            .build()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()).hasSize(1)
        assertEquals(
            testObserver.values()[0].bookingProvider(),
            expectedAction.bookingProvider()
        )
        assertEquals(
            testObserver.values()[0].data().action,
            expectedAction.data().action
        )
        assertEquals(
            testObserver.values()[0].data().data.toString(),
            expectedAction.data().data.toString()
        )
    }

    @Test
    fun `strange external action throws exception`() {
        val params = mockk<ExternalActionParams> {
            every { action() } returns "Some strange action"
            every { segment() } returns mockk()
        }

        val testObserver = bookingResolver.performExternalActionAsync(params).test()
        testObserver.assertError(UnsupportedOperationException::class.java)
    }

    //to compare urls disregarding the trip_date
    private fun areUrlsEquivalent(url1: String, url2: String): Boolean {
        val uri1 = Uri.parse(url1)
        val uri2 = Uri.parse(url2)

        // Extract the query parameters excluding "trip_date"
        val queryParams1 = uri1.queryParameterNames
            .filter { it != "trip_date" }
            .associateWith { uri1.getQueryParameter(it) }

        val queryParams2 = uri2.queryParameterNames
            .filter { it != "trip_date" }
            .associateWith { uri2.getQueryParameter(it) }

        // Compare the base URL (without query parameters)
        val baseUri1 = uri1.buildUpon().clearQuery().build().toString()
        val baseUri2 = uri2.buildUpon().clearQuery().build().toString()

        return baseUri1 == baseUri2 && queryParams1 == queryParams2
    }
}
