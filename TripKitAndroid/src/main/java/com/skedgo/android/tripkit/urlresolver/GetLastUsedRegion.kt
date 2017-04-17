package com.skedgo.android.tripkit.urlresolver

import com.skedgo.android.common.model.Region
import com.skedgo.android.tripkit.RegionService
import rx.Observable
import javax.inject.Inject

open class GetLastUsedRegion @Inject constructor(
    private val regionService: RegionService
) {
  open fun execute(): Observable<Region> {
    return regionService.lastUsedRegion
  }
}