package com.skedgo.tripkit.a2brouting

import android.content.Context
import com.google.gson.Gson
import com.skedgo.tripkit.RoutingUserError
import com.skedgo.tripkit.routing.RoutingResponse
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*
import java.util.*

@RunWith(AndroidJUnit4::class)
class FailoverA2bRoutingApiTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var api: A2bRoutingApi

    private lateinit var failoverA2bRoutingApi: FailoverA2bRoutingApi

    @Before
    fun before() {
        failoverA2bRoutingApi = FailoverA2bRoutingApi(
            ApplicationProvider.getApplicationContext<Context>().resources,
            Gson(),
            api
        )
    }

    @Test
    fun shouldFailSilentlyIfMissingUrls() {
        val subscriber = TestObserver<List<TripGroup>>()
        failoverA2bRoutingApi.fetchRoutesAsync(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
    }

    @Test
    fun shouldFailSilentlyIfAllRequestsFail() {
        `when`(api.execute(
            anyString(),
            anyList<String>(),
            anyList<String>(),
            anyList<String>(),
            anyMap<String, Any>()
        )).thenThrow(RuntimeException::class.java)

        val subscriber = TestObserver<List<TripGroup>>()
        failoverA2bRoutingApi.fetchRoutesAsync(
            listOf("https://www.abc.com", "https://www.def.com"),
            listOf("hyperloop", "drone"),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
    }

    @Test
    fun shouldFailSilentlyIfNoTripGroupsFoundOnAllUrls() {
        val response = RoutingResponse()
        `when`(api.execute(
            anyString(),
            anyList<String>(),
            anyList<String>(),
            anyList<String>(),
            anyMap<String, Any>()
        )).thenReturn(Observable.just(response))

        val subscriber = TestObserver<List<TripGroup>>()
        failoverA2bRoutingApi.fetchRoutesAsync(
            listOf("https://www.abc.com", "https://www.def.com"),
            listOf("hyperloop", "drone"),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
    }

    /* Disabled functions to mockito exception
    @Test
    fun throwUserErrorWhenRoutingWithMultipleUrls() {
        val response = mock(RoutingResponse::class.java)
        `when`(response.errorMessage).thenReturn("Some user error")
        `when`(response.hasError()).thenReturn(true)
        `when`(api.execute(
            eq("https://www.abc.com/routing.json"),
            anyList<String>(),
            anyList<String>(),
            anyList<String>(),
            anyMap<String, Any>()
        )).thenReturn(Observable.just(response))

        val subscriber = TestObserver<List<TripGroup>>()
        failoverA2bRoutingApi.fetchRoutesAsync(
            listOf("https://www.abc.com", "https://www.def.com"),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertError(RoutingUserError::class.java)
        subscriber.assertNoValues()
    }

    @Test
    fun shouldThrowUserError() {
        val response = mock(RoutingResponse::class.java)
        `when`(response.errorMessage).thenReturn("Some user error")
        `when`(response.hasError()).thenReturn(true)
        `when`(api.execute(
            eq("Link 0"),
            anyList<String>(),
            anyList<String>(),
            anyList<String>(),
            anyMap<String, Any>()
        )).thenReturn(Observable.just(response))

        val subscriber = TestObserver<RoutingResponse>()
        failoverA2bRoutingApi.fetchRoutesPerUrlAsync(
            "Link 0",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertError(RoutingUserError::class.java)
        subscriber.assertNoValues()
    }

    @Test
    fun shouldEmitNothingIfHavingErrorAndNotUserError() {
        val response = mock(RoutingResponse::class.java)
        `when`(response.errorMessage).thenReturn("Some error")
        `when`(response.hasError()).thenReturn(false)
        `when`(api.execute(
            eq("Link 0"),
            anyList<String>(),
            anyList<String>(),
            anyList<String>(),
            anyMap<String, Any>()
        )).thenReturn(Observable.just(response))

        val subscriber = TestObserver<RoutingResponse>()
        failoverA2bRoutingApi.fetchRoutesPerUrlAsync(
            "Link 0",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyMap()
        ).subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }
     */
}
