package com.skedgo.tripkit.data.locations

import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.agenda.ConfigRepository
import com.skedgo.tripkit.common.model.region.Region
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
}
