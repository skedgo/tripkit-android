package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.data.tsp.ImmutableRegionInfo
import com.skedgo.tripkit.data.tsp.Paratransit
import com.skedgo.tripkit.data.tsp.RegionInfo
import com.skedgo.tripkit.tsp.RegionInfoRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.eq
import org.mockito.Mockito.same
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.Arrays

@RunWith(AndroidJUnit4::class)
class RegionServiceImplTest : TripKitAndroidRobolectricTest() {
    internal val regionCache: com.skedgo.tripkit.Cache<List<Region>> = mock()
    internal val modeCache: com.skedgo.tripkit.Cache<Map<String, TransportMode>> = mock()
    internal val regionsFetcher: RegionsFetcher = mock()
    internal val regionInfoRepository: RegionInfoRepository = mock()
    internal val regionFinder: com.skedgo.tripkit.RegionFinder = mock()
    private val regionService: RegionService by lazy {
        RegionServiceImpl(
            regionCache,
            modeCache,
            regionsFetcher,
            regionInfoRepository,
            regionFinder
        )
    }

    @Test
    fun shouldPropagateNullPointerExceptionIfLocationIsNull() {
        val subscriber = regionService.getRegionByLocationAsync(null).test()
        subscriber.awaitTerminalEvent()
        assertThat(subscriber.events[1])
            .hasSize(1)
            .hasOnlyElementsOfType(NullPointerException::class.java)
            .extractingResultOf("getMessage")
            .containsExactly("Location is null")
    }

    @Test
    fun shouldTakeFirstFoundRegion() {
        val Sydney = Region()
        Sydney.name = "AU_NSW_Sydney"
        Sydney.encodedPolyline = "nwcvE_fno[owyR??mcjRnwyR?"

        val NewYork = Region()
        NewYork.name = "US_NY_NewYorkCity"
        NewYork.encodedPolyline = "oecvFnzhdM_}tA??o~oE~|tA?"

        whenever(regionCache.async)
            .thenReturn(Single.just(Arrays.asList(Sydney, NewYork)))
        whenever(
            regionFinder.contains(
                same(Sydney),
                eq(-33.86749),
                eq(151.20699)
            )
        ).thenReturn(true)

        val subscriber = TestObserver<Region>()
        regionService.getRegionByLocationAsync(
            Location(
                -33.86749,
                151.20699
            )
        ).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        subscriber.assertValueCount(1)
        subscriber.assertValueAt(0) { region -> region == Sydney }
    }

    @Test
    fun shouldPropagateOutOfRegionsExceptionIfNoRegionIsFound() {
        val Sydney = Region()
        Sydney.name = "AU_NSW_Sydney"
        Sydney.encodedPolyline = "nwcvE_fno[owyR??mcjRnwyR?"

        val NewYork = Region()
        NewYork.name = "US_NY_NewYorkCity"
        NewYork.encodedPolyline = "oecvFnzhdM_}tA??o~oE~|tA?"

        whenever(regionCache.async)
            .thenReturn(Single.just(Arrays.asList(Sydney, NewYork)))

        val subscriber = TestObserver<Region>()
        val location =
            Location(1.0, 2.0)
        regionService.getRegionByLocationAsync(location).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        val errors = subscriber.events[1]
        assertThat(errors)
            .hasSize(1)
            .hasOnlyElementsOfType(com.skedgo.tripkit.OutOfRegionsException::class.java)
            .extractingResultOf("getMessage")
            .containsExactly("Location lies outside covered area")

        val error = errors[0] as com.skedgo.tripkit.OutOfRegionsException
        assertThat(error.latitude()).isEqualTo(location.lat)
        assertThat(error.longitude()).isEqualTo(location.lon)
    }

    @Test
    fun shouldTakeAllCitiesInRegions() {
        val AU = Region()
        val Sydney = Region.City()
        Sydney.name = "Sydney"
        val Newcastle = Region.City()
        Newcastle.name = "Newcastle"
        AU.cities = ArrayList<Region.City>(Arrays.asList(Sydney, Newcastle))

        val US = Region()
        val NewYork = Region.City()
        NewYork.name = "New York"
        val SanJose = Region.City()
        SanJose.name = "San Jose"
        US.cities = ArrayList<Region.City>(Arrays.asList(NewYork, SanJose))

        whenever(regionCache.async)
            .thenReturn(Single.just(Arrays.asList(AU, US)))

        val subscriber = TestObserver<Location>()
        regionService.getCitiesAsync().subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        val cities = subscriber.events[0]
        assertThat(cities).containsExactly(Sydney, Newcastle, NewYork, SanJose)
    }

    @Test
    fun shouldTakeTransportModesFromModesLoader() {
        val modeMap = HashMap<String, TransportMode>()
        modeMap.put("car", TransportMode())
        modeMap.put("walk", TransportMode())
        whenever(modeCache.async).thenReturn(Single.just<Map<String, TransportMode>>(modeMap))

        val subscriber = TestObserver<Map<String, TransportMode>>()
        regionService.getTransportModesAsync().subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        val actualModeMap = subscriber.events[0]
        assertThat(actualModeMap.first()).isEqualTo(modeMap)
    }

    @Test
    fun shouldTakeRegionsFromRegionsLoader() {
        val regions = Arrays.asList(
            Region(),
            Region()
        )
        whenever(regionCache.async).thenReturn(Single.just(regions))

        val subscriber = regionService.getRegionsAsync().test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        val list = subscriber.events[0] as List<Region>
        assertThat(list.first()).isEqualTo(regions)
    }

    @Test
    fun shouldFetchParatransit() {
        val paratransit = Paratransit(
            "http://accessla.org/",
            "Access",
            "1.800.883.1295"
        )
        val regionInfo = ImmutableRegionInfo.builder()
            .paratransit(paratransit)
            .build()

        val region = Region()
        region.setURLs(ArrayList(listOf("https://lepton-us-ca-losangeles.tripgo.skedgo.com/satapp")))
        region.name = "US_CA_LosAngeles"

        whenever(regionInfoRepository.getRegionInfoByRegion(region)).thenReturn(
            Observable.just<RegionInfo>(
                regionInfo
            )
        )

        val subscriber = TestObserver<Paratransit>()
        regionService.fetchParatransitByRegionAsync(region)
            .subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        assertThat(subscriber.events[0])
            .containsExactly(paratransit)
    }

    @Test
    fun shouldInvalidateCachesAfterRefreshing() {
        whenever(regionsFetcher.fetchAsync()).thenReturn(Completable.complete())
        val subscriber = TestObserver<Void>()
        regionService.refreshAsync().subscribe(subscriber)

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        verify<com.skedgo.tripkit.Cache<Map<String, TransportMode>>>(
            modeCache,
            times(1)
        ).invalidate()
        verify<com.skedgo.tripkit.Cache<List<Region>>>(regionCache, times(1)).invalidate()
        verify<com.skedgo.tripkit.RegionFinder>(regionFinder, times(1)).invalidate()
    }
}