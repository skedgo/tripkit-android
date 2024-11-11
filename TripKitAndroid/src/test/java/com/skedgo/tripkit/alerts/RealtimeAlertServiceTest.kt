package com.skedgo.tripkit.alerts

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(AndroidJUnit4::class)
class RealtimeAlertServiceTest {

    private val api: RealtimeAlertApi = mockk()
    private lateinit var service: RealtimeAlertService

    @Before
    fun setUp() {
        service = RealtimeAlertService(api)
    }

    /**
     * We manage to fetch via the first server, then we ignore the second server.
     */
    @Test
    fun `fetch realtime alerts successfully`() {
        // Arrange
        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val response = ImmutableRealtimeAlertResponse.builder().build()

        every {
            api.fetchRealtimeAlertsAsync(
                "http://tripgo.com/alerts/transit.json",
                "sydney"
            )
        } returns Observable.just(response)

        // Act
        val testObserver: TestObserver<RealtimeAlertResponse> = service
            .fetchRealtimeAlertsAsync(baseUrls, "sydney")
            .test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        testObserver.assertValue(response)

        verify(exactly = 1) {
            api.fetchRealtimeAlertsAsync(
                "http://tripgo.com/alerts/transit.json",
                "sydney"
            )
        }
    }

    /**
     * When we fail to fetch via the first server but manage via the second server.
     */
    @Test
    fun `fetch realtime alerts successfully via 2nd server`() {
        // Arrange
        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val response = ImmutableRealtimeAlertResponse.builder().build()
        val error = RuntimeException("1st server is down")

        every { api.fetchRealtimeAlertsAsync(any(), "sydney") }
            .returnsMany(
                Observable.error(error),
                Observable.just(response)
            )

        // Act
        val testObserver: TestObserver<RealtimeAlertResponse> = service
            .fetchRealtimeAlertsAsync(baseUrls, "sydney")
            .test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        testObserver.assertValue(response)

        verify(exactly = 2) { api.fetchRealtimeAlertsAsync(any(), "sydney") }
    }

    /**
     * When we fail to fetch via both servers.
     */
    @Test
    fun `fail to fetch realtime alerts`() {
        // Arrange
        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val firstError = RuntimeException("1st server is down")
        val secondError = RuntimeException("2nd server is down")

        every { api.fetchRealtimeAlertsAsync(any(), "sydney") }
            .returnsMany(
                Observable.error(firstError),
                Observable.error(secondError)
            )

        // Act
        val testObserver: TestObserver<RealtimeAlertResponse> = service
            .fetchRealtimeAlertsAsync(baseUrls, "sydney")
            .test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertError(CompositeException::class.java)

        verify(exactly = 2) { api.fetchRealtimeAlertsAsync(any(), "sydney") }
    }
}