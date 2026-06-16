package com.skedgo.tripkit.data.locations

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class LocationsFetchCoordinatorTest {

    private val now = AtomicLong(1_000_000_000L)
    private val ttl = TimeUnit.MINUTES.toMillis(10)
    private val coordinator = LocationsFetchCoordinator(ttlMs = ttl, clock = { now.get() })

    @Test
    fun `filterStaleCellIds returns all when nothing recorded`() {
        val result = coordinator.filterStaleCellIds(
            cellIds = listOf("1#1", "1#2", "1#3"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        assertThat(result).containsExactlyInAnyOrder("1#1", "1#2", "1#3")
    }

    @Test
    fun `recently fetched cells are filtered out within TTL`() {
        coordinator.recordFetched(
            cellIds = listOf("1#1", "1#2"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        val result = coordinator.filterStaleCellIds(
            cellIds = listOf("1#1", "1#2", "1#3"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        assertThat(result).containsExactly("1#3")
    }

    @Test
    fun `cells expire once TTL elapses`() {
        coordinator.recordFetched(
            cellIds = listOf("1#1"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        now.set(now.get() + ttl + 1)
        val result = coordinator.filterStaleCellIds(
            cellIds = listOf("1#1"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        assertThat(result).containsExactly("1#1")
    }

    @Test
    fun `tracking is scoped per region and level`() {
        coordinator.recordFetched(
            cellIds = listOf("1#1"),
            regionName = "AU_NT_Darwin",
            level = 1
        )
        // Same cell id but different region/level → still stale.
        val differentRegion = coordinator.filterStaleCellIds(
            cellIds = listOf("1#1"),
            regionName = "AU_NSW_Sydney",
            level = 1
        )
        val differentLevel = coordinator.filterStaleCellIds(
            cellIds = listOf("1#1"),
            regionName = "AU_NT_Darwin",
            level = 2
        )
        assertThat(differentRegion).containsExactly("1#1")
        assertThat(differentLevel).containsExactly("1#1")
    }

    @Test
    fun `shareInFlight returns the same upstream subscription for concurrent callers`() {
        val networkCalls = AtomicInteger(0)
        // Use a PublishSubject as a stand-in for an in-flight network call that has not
        // yet emitted. The dedup window only matters while the upstream is still pending.
        val pendingNetwork = PublishSubject.create<List<LocationsResponse.Group>>()
        val upstream: () -> Observable<List<LocationsResponse.Group>> = {
            networkCalls.incrementAndGet()
            pendingNetwork
        }

        val key = "AU_NT_Darwin|1|1#1,1#2"
        val first = coordinator.shareInFlight(key, upstream).test()
        val second = coordinator.shareInFlight(key, upstream).test()

        // Simulate the network completing after both callers have subscribed.
        pendingNetwork.onNext(emptyList())
        pendingNetwork.onComplete()

        first.assertComplete()
        second.assertComplete()
        // Without the coordinator the two subscriptions would have hit the network twice.
        assertThat(networkCalls.get()).isEqualTo(1)
    }

    @Test
    fun `shareInFlight key is released after completion so a later fetch can run`() {
        val networkCalls = AtomicInteger(0)
        val upstream: () -> Observable<List<LocationsResponse.Group>> = {
            networkCalls.incrementAndGet()
            Observable.just(emptyList<LocationsResponse.Group>())
        }

        val key = "AU_NT_Darwin|1|1#1"
        coordinator.shareInFlight(key, upstream).test().assertComplete()
        coordinator.shareInFlight(key, upstream).test().assertComplete()

        // Two separate (sequential) fetches → two network calls; dedup is only for concurrent ones.
        assertThat(networkCalls.get()).isEqualTo(2)
    }
}
