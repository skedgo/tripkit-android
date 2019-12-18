package com.skedgo.tripkit

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.data.tsp.RegionInfo
import org.amshove.kluent.`should contain all`
import org.junit.Test
import skedgo.tripkit.routing.ModeInfo

class TransitModeFilterTest {

  val transitModeFilter = object : TransitModeFilter {}
  @Test
  fun getFilteredTransitModes() {
    val regionInfo = mock<RegionInfo>()
    val excludedTransitModes = listOf<ModeInfo>(mock(), mock(), mock())
    whenever(regionInfo.transitModes()).thenReturn(excludedTransitModes)

    transitModeFilter.filterTransitModes(regionInfo) `should contain all` excludedTransitModes
  }
}