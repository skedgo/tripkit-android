package com.skedgo.tripkit.data.locations

import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.agenda.ConfigRepository
import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StopsFetcherTest {

    private val configRepository: ConfigRepository = mock()
    private val stopsFetcher = StopsFetcher(
        api = mock(),
        cellsLoader = mock(),
        cellsPersistor = mock(),
        stopsPersistor = mock(),
        configCreator = configRepository,
        bikePodRepository = mock(),
        freeFloatingRepository = mock(),
        carParkPersistor = mock(),
        onStreetParkingPersistor = mock(),
        carParkMapper = mock(),
        carPodMapper = mock(),
        onStreetParkingMapper = mock(),
        carPodRepository = mock(),
        facilityRepository = mock(),
        fetchCoordinator = LocationsFetchCoordinator()
    )

    @Test
    fun `non-grid regional ids force cellIDs payload when only existing cells remain`() {
        whenever(configRepository.call()).thenReturn(JsonObject())

        val region = Region().apply { name = "AU_NT_Darwin" }
        val cellIds = listOf("AU_NT_Darwin")
        val existingCells = listOf(LocationsResponse.Group(123L, "AU_NT_Darwin"))

        val requestBodies = stopsFetcher.splitIntoBodiesForNewFetchOrUpdate(
            cellIds = cellIds,
            existingCells = existingCells,
            region = region,
            level = 1
        ).toList().blockingGet()

        assertThat(requestBodies).hasSize(1)
        assertThat(requestBodies.first().cellIds).containsExactly("AU_NT_Darwin")
        assertThat(requestBodies.first().existingCells).isNull()
    }

    @Test
    fun `grid ids keep hash-code update payload`() {
        whenever(configRepository.call()).thenReturn(JsonObject())

        val region = Region().apply { name = "AU_NT_Darwin" }
        val cellIds = listOf("1#1")
        val existingCells = listOf(LocationsResponse.Group(456L, "1#1"))

        val requestBodies = stopsFetcher.splitIntoBodiesForNewFetchOrUpdate(
            cellIds = cellIds,
            existingCells = existingCells,
            region = region,
            level = 1
        ).toList().blockingGet()

        assertThat(requestBodies).hasSize(1)
        assertThat(requestBodies.first().cellIds).isNull()
        assertThat(requestBodies.first().existingCells).containsEntry("1#1", 456L)
    }

    // region #25936 — freshness must only be recorded for responses that actually arrived.

    private val regionName = "AU_NT_Darwin"
    private val cellIds = listOf("-2531#11145")

    /** Bounded spin so tests never race StopsFetcher's background request assembly. */
    private fun awaitUntil(timeoutMs: Long = 2_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (!condition() && System.currentTimeMillis() < deadline) {
            Thread.sleep(5)
        }
    }

    private fun regionWithOneUrl() = Region().apply {
        name = regionName
        setURLs(arrayListOf("https://darwin-au-nt.tripgo.skedgo.com/satapp"))
    }

    /**
     * Builds a fetcher whose only interesting collaborators are the api and the coordinator,
     * so the tests can assert on TTL bookkeeping and on how many POSTs were issued.
     */
    private fun fetcherFor(
        coordinator: LocationsFetchCoordinator,
        apiResponse: () -> Observable<LocationsResponse>
    ): StopsFetcher {
        val api: LocationsApi = mock()
        whenever(api.fetchLocationsAsync(any(), any<LocationsRequestBody>()))
            .thenAnswer { apiResponse() }

        val cellsLoader: StopsFetcher.ICellsLoader = mock()
        whenever(cellsLoader.loadSavedCellsAsync(any())).thenReturn(Observable.just(emptyList()))

        whenever(configRepository.call()).thenReturn(JsonObject())

        return StopsFetcher(
            api = api,
            cellsLoader = cellsLoader,
            cellsPersistor = mock(),
            stopsPersistor = mock(),
            configCreator = configRepository,
            bikePodRepository = mock(),
            freeFloatingRepository = mock(),
            carParkPersistor = mock(),
            onStreetParkingPersistor = mock(),
            carParkMapper = mock(),
            carPodMapper = mock(),
            onStreetParkingMapper = mock(),
            carPodRepository = mock(),
            facilityRepository = mock(),
            fetchCoordinator = coordinator
        )
    }

    @Test
    fun `a successful empty response still marks the requested cells fresh`() {
        val coordinator = LocationsFetchCoordinator()
        val fetcher = fetcherFor(coordinator) {
            Observable.just(LocationsResponse().apply { groups = arrayListOf() })
        }

        fetcher.fetchAsync(cellIds, regionWithOneUrl(), 2).test().awaitTerminalEvent()

        assertThat(coordinator.filterStaleCellIds(cellIds, regionName, 2)).isEmpty()
    }

    @Test
    fun `a failed request does not mark the requested cells fresh`() {
        val coordinator = LocationsFetchCoordinator()
        val fetcher = fetcherFor(coordinator) {
            Observable.error(java.io.IOException("network down"))
        }

        fetcher.fetchAsync(cellIds, regionWithOneUrl(), 2).test().awaitTerminalEvent()

        // Before #25936 the all-URLs-failed case still emitted an empty list, so these cells
        // were marked fresh and their markers stayed missing for the whole TTL window.
        assertThat(coordinator.filterStaleCellIds(cellIds, regionName, 2))
            .containsExactlyElementsOf(cellIds)
    }

    @Test
    fun `cells stay fetchable immediately after a failure`() {
        val coordinator = LocationsFetchCoordinator()
        var calls = 0
        val fetcher = fetcherFor(coordinator) {
            calls++
            if (calls == 1) {
                Observable.error(java.io.IOException("network down"))
            } else {
                Observable.just(LocationsResponse().apply { groups = arrayListOf() })
            }
        }

        fetcher.fetchAsync(cellIds, regionWithOneUrl(), 2).test().awaitTerminalEvent()
        fetcher.fetchAsync(cellIds, regionWithOneUrl(), 2).test().awaitTerminalEvent()

        assertThat(calls).isEqualTo(2)
        assertThat(coordinator.filterStaleCellIds(cellIds, regionName, 2)).isEmpty()
    }

    @Test
    fun `a fresh cell is not requested again`() {
        val coordinator = LocationsFetchCoordinator()
        var calls = 0
        val fetcher = fetcherFor(coordinator) {
            calls++
            Observable.just(LocationsResponse().apply { groups = arrayListOf() })
        }

        repeat(5) {
            fetcher.fetchAsync(cellIds, regionWithOneUrl(), 2).test().awaitTerminalEvent()
        }

        assertThat(calls).isEqualTo(1)
    }

    @Test
    fun `the regional request is not repeated for every local viewport evaluation`() {
        // At local zoom FetchStopsByViewport issues a level-1 regional request alongside the
        // level-2 local one. That pairing is deliberate — level 1 returns parent stops (train)
        // and level 2 returns non-parent stops (bus) — and it is only paid once per region per
        // TTL window, because the regional cell is suppressed on every later evaluation.
        //
        // This is why the dual request is left as-is for #25936: it is not a per-pan multiplier.
        val coordinator = LocationsFetchCoordinator()
        var calls = 0
        val fetcher = fetcherFor(coordinator) {
            calls++
            Observable.just(LocationsResponse().apply { groups = arrayListOf() })
        }
        val region = regionWithOneUrl()

        repeat(4) { index ->
            fetcher.fetchAsync(listOf(regionName), region, 1).test().awaitTerminalEvent()
            fetcher.fetchAsync(listOf("-2531#1114$index", regionName), region, 2)
                .test().awaitTerminalEvent()
        }

        // 1 regional call, plus one per genuinely new local cell set.
        assertThat(calls).isEqualTo(5)
    }

    @Test
    fun `a slow fetch survives losing one subscriber while another is still attached`() {
        // Underpins the #25936 prefetch change. The visible-viewport pipeline uses
        // switchMapDelayError, so its subscription is disposed whenever a newer viewport
        // arrives. Now that prefetch subscribes to FetchStopsByViewport on its own instead of
        // pushing a buffered viewport through the same relay, an in-flight request for the same
        // cells keeps a second subscriber - and the work (and its freshness) is not thrown away.
        val coordinator = LocationsFetchCoordinator()
        val upstream = io.reactivex.subjects.PublishSubject.create<LocationsResponse>()
        var apiCalls = 0
        val fetcher = fetcherFor(coordinator) {
            apiCalls++
            upstream
        }
        val region = regionWithOneUrl()

        val visible = fetcher.fetchAsync(cellIds, region, 2).test()
        val prefetch = fetcher.fetchAsync(cellIds, region, 2).test()
        // StopsFetcher builds its request bodies on Schedulers.newThread(), so wait (briefly,
        // bounded) for the call to actually reach the api rather than racing it.
        awaitUntil { apiCalls > 0 }
        assertThat(apiCalls).isEqualTo(1)

        // The visible viewport is superseded and its subscription disposed.
        visible.dispose()

        upstream.onNext(LocationsResponse().apply { groups = arrayListOf() })
        upstream.onComplete()

        prefetch.awaitTerminalEvent()
        assertThat(apiCalls).isEqualTo(1)
        assertThat(coordinator.filterStaleCellIds(cellIds, regionName, 2)).isEmpty()
    }

    @Test
    fun `a genuinely new viewport still fetches after a prefetch completed`() {
        val coordinator = LocationsFetchCoordinator()
        var calls = 0
        val fetcher = fetcherFor(coordinator) {
            calls++
            Observable.just(LocationsResponse().apply { groups = arrayListOf() })
        }
        val region = regionWithOneUrl()

        fetcher.fetchAsync(cellIds, region, 2).test().awaitTerminalEvent()
        fetcher.fetchAsync(listOf("-2999#11999"), region, 2).test().awaitTerminalEvent()

        assertThat(calls).isEqualTo(2)
    }

    // endregion
}
