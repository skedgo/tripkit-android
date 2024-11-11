package com.skedgo.tripkit.tsp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.data.tsp.ImmutableRegionInfo
import com.skedgo.tripkit.data.tsp.RegionInfo
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import io.reactivex.observers.TestObserver
import org.junit.After
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
class RegionInfoServiceTest: MockKTest() {


    private val api: RegionInfoApi = mockk()

    private lateinit var service: RegionInfoService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
        val apiLazy: Lazy<RegionInfoApi> = mockk {
            every { get() } returns api
        }
        service = RegionInfoService(apiLazy)
    }

    @After
    fun teardown() {
        tearDownRx()
    }

    @Test
    fun `fetch region info successfully via first server`() {
        val regionInfo = ImmutableRegionInfo.builder()
            .transitWheelchairAccessibility(true)
            .build()
        val response = ImmutableRegionInfoResponse.builder()
            .regions(listOf(regionInfo))
            .build()

        every {
            api.fetchRegionInfoAsync(
                "http://tripgo.com/regionInfo.json",
                ImmutableRegionInfoBody.of("AU")
            )
        } returns Observable.just(response)

        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val testObserver: TestObserver<RegionInfo> = service.fetchRegionInfoAsync(baseUrls, "AU").test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        testObserver.assertValue(regionInfo)
    }

    @Test
    fun `fetch region info successfully via second server after failure on first`() {
        val regionInfo = ImmutableRegionInfo.builder()
            .transitWheelchairAccessibility(true)
            .build()
        val response = ImmutableRegionInfoResponse.builder()
            .regions(listOf(regionInfo))
            .build()

        val error = RuntimeException("1st server is down")

        every {
            api.fetchRegionInfoAsync(any(), any())
        } returnsMany listOf(
            Observable.error(error),
            Observable.just(response)
        )

        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val testObserver: TestObserver<RegionInfo> = service.fetchRegionInfoAsync(baseUrls, "sydney").test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        testObserver.assertValue(regionInfo)

        verify(exactly = 2) { api.fetchRegionInfoAsync(any(), any()) }
    }

    @Test
    fun `fail to fetch region info via both servers`() {
        val firstError = RuntimeException("1st server is down")
        val secondError = RuntimeException("2nd server is down")

        every {
            api.fetchRegionInfoAsync(any(), any())
        } returnsMany listOf(
            Observable.error(firstError),
            Observable.error(secondError)
        )

        val baseUrls = listOf("http://tripgo.com/", "http://riogo.com/")
        val testObserver: TestObserver<RegionInfo> = service.fetchRegionInfoAsync(baseUrls, "sydney").test()

        testObserver.awaitTerminalEvent()
        testObserver.assertError(CompositeException::class.java)

        verify(exactly = 2) { api.fetchRegionInfoAsync(any(), any()) }
    }
}