package com.skedgo.tripkit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.data.regions.RegionService
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationInfoServiceImplTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var api: LocationInfoApi
    private lateinit var regionService: RegionService
    private lateinit var service: LocationInfoServiceImpl

    @Before
    fun setUp() {
        initRx()
        api = mockk()
        regionService = mockk()
        service = LocationInfoServiceImpl(api, regionService)
    }

    @After
    fun after() {
        tearDownRx()
    }

    @Test
    fun fetchLocationInfoByRegionUrl() {
        // Create mock objects
        val region = mockk<Region>()
        val location = Location(1.0, 2.0)
        val locationInfo = mockk<LocationInfo>()

        // Stubbing the region mock
        every { region.getURLs() } returns arrayListOf("https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp")

        // Stubbing the regionService mock
        every { regionService.getRegionByLocationAsync(any()) } returns Observable.just(region)

        // Stubbing the api mock
        every {
            api.fetchLocationInfoAsync(
                "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json",
                location.lat,
                location.lon
            )
        } returns Observable.just(locationInfo)

        // Act: Call the service method and subscribe to it
        val subscriber: TestObserver<LocationInfo> = service.getLocationInfoAsync(location).test()

        // Assert: Validate the test observer's response
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(locationInfo)
    }

    @Test
    fun fetchLocationInfoByRegionUrls() {
        val region = mockk<Region>()
        every { region.getURLs() } returns arrayListOf(
            "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp",
            "https://inflationary-au-nsw-sydney.tripgo.skedgo.com/satapp",
            "https://hadron-fr-b-bordeaux.tripgo.skedgo.com/satapp"
        )

        every { regionService.getRegionByLocationAsync(any()) } returns Observable.just(region)

        val location = Location(1.0, 2.0)
        val locationInfo = mockk<LocationInfo>()

        every {
            api.fetchLocationInfoAsync(any(), location.lat, location.lon)
        } answers {
            val url = firstArg<String>()
            when (url) {
                "https://inflationary-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json" -> Observable.just(locationInfo)
                "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/locationInfo.json" -> Observable.empty()
                else -> Observable.error(RuntimeException())
            }
        }

        val subscriber: TestObserver<LocationInfo> = service.getLocationInfoAsync(location).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(locationInfo)
    }
}