package com.skedgo.android.tripkit.tsp

import com.skedgo.android.common.model.Region
import rx.Observable
import javax.inject.Inject

class RegionInfoRepository @Inject constructor(
    private val regionInfoService: RegionInfoService
) {

  private val regionInfoMap = HashMap<String, RegionInfo>()

  fun getRegionInfoByRegion(region: Region): Observable<RegionInfo> =
      if (regionInfoMap.containsKey(region.name))
        Observable.just(regionInfoMap[region.name])
  else
        regionInfoService.fetchRegionInfoAsync(region.urLs!!, region.name)
            .doOnNext { regionInfoMap[region.name!!] = it}

}