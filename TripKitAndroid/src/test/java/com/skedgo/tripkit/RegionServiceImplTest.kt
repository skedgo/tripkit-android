package com.skedgo.tripkit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.data.tsp.ImmutableRegionInfo
import com.skedgo.tripkit.data.tsp.Paratransit
import com.skedgo.tripkit.data.tsp.RegionInfo
import com.skedgo.tripkit.tsp.RegionInfoRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.internal.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionServiceImplTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val regionCache: com.skedgo.tripkit.Cache<List<Region>> = mockk(relaxed = true)
    private val modeCache: com.skedgo.tripkit.Cache<Map<String, TransportMode>> = mockk(relaxed = true)
    private val regionsFetcher: RegionsFetcher = mockk(relaxed = true)
    private val regionInfoRepository: RegionInfoRepository = mockk(relaxed = true)
    private val regionFinder: com.skedgo.tripkit.RegionFinder = mockk(relaxed = true)
    private val regionService: RegionService by lazy {
        RegionServiceImpl(
            regionCache,
            modeCache,
            regionsFetcher,
            regionInfoRepository,
            regionFinder
        )
    }

    @Before
    fun setUp() {
        initRx()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        tearDownRx()
        clearAllMocks()
    }

    @Test
    fun `should propagate NullPointerException if location is null`() {
        val subscriber = regionService.getRegionByLocationAsync(null).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertError(NullPointerException::class.java)
        assertEquals("Location is null", subscriber.errors()[0].message)
    }

    @Test
    fun `should take first found region`() {
        val sydney = Region().apply {
            name = "AU_NSW_Sydney"
            encodedPolyline = "nwcvE_fno[owyR??mcjRnwyR?"
        }
        val newYork = Region().apply {
            name = "US_NY_NewYorkCity"
            encodedPolyline = "oecvFnzhdM_}tA??o~oE~|tA?"
        }

        every { regionCache.getAsync() } returns Single.just(listOf(sydney, newYork))
        every { regionFinder.contains(sydney, -33.86749, 151.20699) } returns true

        val subscriber = TestObserver<Region>()
        regionService.getRegionByLocationAsync(Location(-33.86749, 151.20699)).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(sydney)
    }

    @Test
    fun `should propagate OutOfRegionsException if no region is found`() {
        val sydney = Region().apply { name = "AU_NSW_Sydney" }
        val newYork = Region().apply { name = "US_NY_NewYorkCity" }

        every { regionCache.getAsync() } returns Single.just(listOf(sydney, newYork))

        val location = Location(1.0, 2.0)
        val subscriber = regionService.getRegionByLocationAsync(location).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertError(com.skedgo.tripkit.OutOfRegionsException::class.java)
        val error = subscriber.errors()[0] as com.skedgo.tripkit.OutOfRegionsException
        assertEquals(location.lat, error.latitude())
        assertEquals(location.lon, error.longitude())
    }

    @Test
    fun `should take all cities in regions`() {
        val sydney = Region.City().apply { name = "Sydney" }
        val newcastle = Region.City().apply { name = "Newcastle" }
        val au = Region().apply { cities = ArrayList(listOf(sydney, newcastle)) }

        val newYork = Region.City().apply { name = "New York" }
        val sanJose = Region.City().apply { name = "San Jose" }
        val us = Region().apply { cities = ArrayList(listOf(newYork, sanJose)) }

        every { regionCache.getAsync() } returns Single.just(listOf(au, us))

        val subscriber = regionService.getCitiesAsync().test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValues(sydney, newcastle, newYork, sanJose)
    }

    @Test
    fun `should take transport modes from modes loader`() {
        val modeMap = mapOf("car" to TransportMode(), "walk" to TransportMode())
        every { modeCache.getAsync() } returns Single.just(modeMap)

        val subscriber = regionService.getTransportModesAsync().test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(modeMap)
    }

    @Test
    fun `should take regions from regions loader`() {
        val regions = listOf(Region(), Region())
        every { regionCache.getAsync() } returns Single.just(regions)

        val subscriber = regionService.getRegionsAsync().test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(regions)
    }

    @Test
    fun `should fetch paratransit`() {
        val paratransit = Paratransit("http://accessla.org/", "Access", "1.800.883.1295")
        val regionInfo = ImmutableRegionInfo.builder().paratransit(paratransit).build()

        val region = Region().apply {
            setURLs(
                ArrayList(listOf("https://lepton-us-ca-losangeles.tripgo.skedgo.com/satapp"))
            )
            name = "US_CA_LosAngeles"
        }

        every { regionInfoRepository.getRegionInfoByRegion(region) } returns Observable.just(regionInfo)

        val subscriber = regionService.fetchParatransitByRegionAsync(region).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(paratransit)
    }

    @Test
    fun `should invalidate caches after refreshing`() {
        every { regionsFetcher.fetchAsync() } returns Completable.complete()

        val subscriber = regionService.refreshAsync().test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        verify(exactly = 1) { modeCache.invalidate() }
        verify(exactly = 1) { regionCache.invalidate() }
        verify(exactly = 1) { regionFinder.invalidate() }
    }
}