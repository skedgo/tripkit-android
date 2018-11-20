package com.skedgo.android.tripkit

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.tsp.RegionInfo
import org.amshove.kluent.`should contain all`
import org.junit.Assert.*
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