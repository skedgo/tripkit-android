package com.skedgo.android.tripkit

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.tripkit.tsp.*
import dagger.internal.Factory
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.*
import rx.Completable
import rx.Observable
import rx.observers.TestSubscriber
import java.util.*

class RegionServiceImplTest : TripKitAndroidRobolectricTest() {
  internal val regionCache: Cache<List<Region>> = mock()
  internal val modeCache: Cache<Map<String, TransportMode>> = mock()
  internal val regionsFetcher: RegionsFetcher = mock()
  internal val regionInfoRepository: RegionInfoRepository = mock()
  internal val regionFinder: RegionFinder = mock()
  private val regionService: RegionService by lazy {
    RegionServiceImpl(
        regionCache,
        modeCache,
        regionsFetcher,
        regionInfoRepository,
        regionFinder
    )
  }

  @Test fun shouldPropagateNullPointerExceptionIfLocationIsNull() {
    val subscriber = regionService.getRegionByLocationAsync(null).test()
    subscriber.awaitTerminalEvent()
    assertThat(subscriber.onErrorEvents)
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException::class.java)
        .extractingResultOf("getMessage")
        .containsExactly("Location is null")
  }

  @Test fun shouldTakeFirstFoundRegion() {
    val Sydney = Region()
    Sydney.setName("AU_NSW_Sydney")
    Sydney.setEncodedPolyline("nwcvE_fno[owyR??mcjRnwyR?")

    val NewYork = Region()
    NewYork.setName("US_NY_NewYorkCity")
    NewYork.setEncodedPolyline("oecvFnzhdM_}tA??o~oE~|tA?")

    whenever(regionCache.async)
        .thenReturn(Observable.just(Arrays.asList(Sydney, NewYork)))
    whenever(regionFinder.contains(
        same(Sydney),
        eq(-33.86749),
        eq(151.20699)
    )).thenReturn(true)

    val subscriber = TestSubscriber<Region>()
    regionService.getRegionByLocationAsync(Location(-33.86749, 151.20699))
        .subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()

    val regions = subscriber.onNextEvents
    assertThat(regions).hasSize(1)
    assertThat(regions[0]).isSameAs(Sydney)
  }

  @Test fun shouldPropagateOutOfRegionsExceptionIfNoRegionIsFound() {
    val Sydney = Region()
    Sydney.name = "AU_NSW_Sydney"
    Sydney.encodedPolyline = "nwcvE_fno[owyR??mcjRnwyR?"

    val NewYork = Region()
    NewYork.name = "US_NY_NewYorkCity"
    NewYork.encodedPolyline = "oecvFnzhdM_}tA??o~oE~|tA?"

    whenever(regionCache.async)
        .thenReturn(Observable.just(Arrays.asList(Sydney, NewYork)))

    val subscriber = TestSubscriber<Region>()
    val location = Location(1.0, 2.0)
    regionService.getRegionByLocationAsync(location).subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    val errors = subscriber.onErrorEvents
    assertThat(errors)
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException::class.java)
        .extractingResultOf("getMessage")
        .containsExactly("Location lies outside covered area")

    val error = errors[0] as OutOfRegionsException
    assertThat(error.latitude()).isEqualTo(location.lat)
    assertThat(error.longitude()).isEqualTo(location.lon)
  }

  @Test fun shouldTakeAllCitiesInRegions() {
    val AU = Region()
    val Sydney = Region.City()
    Sydney.name = "Sydney"
    val Newcastle = Region.City()
    Newcastle.name = "Newcastle"
    AU.setCities(ArrayList<Region.City>(Arrays.asList(Sydney, Newcastle)))

    val US = Region()
    val NewYork = Region.City()
    NewYork.name = "New York"
    val SanJose = Region.City()
    SanJose.name = "San Jose"
    US.setCities(ArrayList<Region.City>(Arrays.asList(NewYork, SanJose)))

    whenever(regionCache.async)
        .thenReturn(Observable.just(Arrays.asList(AU, US)))

    val subscriber = TestSubscriber<Location>()
    regionService.getCitiesAsync().subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()
    val cities = subscriber.onNextEvents
    assertThat(cities).containsExactly(Sydney, Newcastle, NewYork, SanJose)
  }

  @Test fun shouldTakeTransportModesFromModesLoader() {
    val modeMap = HashMap<String, TransportMode>()
    modeMap.put("car", TransportMode())
    modeMap.put("walk", TransportMode())
    whenever(modeCache.async).thenReturn(Observable.just<Map<String, TransportMode>>(modeMap))

    val subscriber = TestSubscriber<Map<String, TransportMode>>()
    regionService.getTransportModesAsync().subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()
    val actualModeMap = subscriber.onNextEvents[0]
    assertThat(actualModeMap).isEqualTo(modeMap)
  }

  @Test fun shouldTakeRegionsFromRegionsLoader() {
    val regions = Arrays.asList(Region(), Region())
    whenever(regionCache.async).thenReturn(Observable.just(regions))

    val subscriber = TestSubscriber<List<Region>>()
    regionService.getRegionsAsync().subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()
    assertThat(subscriber.onNextEvents[0]).isEqualTo(regions)
  }

  @Test fun shouldFetchParatransit() {
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
    region.setName("US_CA_LosAngeles")

    whenever(regionInfoRepository.getRegionInfoByRegion(region)).thenReturn(Observable.just<RegionInfo>(regionInfo))

    val subscriber = TestSubscriber<Paratransit>()
    regionService.fetchParatransitByRegionAsync(region)
        .subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()

    assertThat(subscriber.onNextEvents)
        .containsExactly(paratransit)
  }

  @Test fun shouldInvalidateCachesAfterRefreshing() {
    whenever(regionsFetcher.fetchAsync()).thenReturn(Completable.complete())
    val subscriber = TestSubscriber<Void>()
    regionService.refreshAsync().subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()

    verify<Cache<Map<String, TransportMode>>>(modeCache, times(1)).invalidate()
    verify<Cache<List<Region>>>(regionCache, times(1)).invalidate()
    verify<RegionFinder>(regionFinder, times(1)).invalidate()
  }
}