package com.skedgo.tripkit.tsp

import com.skedgo.tripkit.common.model.Region
import com.skedgo.tripkit.data.tsp.RegionInfo
import io.reactivex.Observable
import javax.inject.Inject

open class RegionInfoRepository @Inject constructor(
    private val regionInfoService: com.skedgo.tripkit.tsp.RegionInfoService
) {

  private val regionInfoMap = HashMap<String, RegionInfo>()

  open fun getRegionInfoByRegion(region: Region): Observable<RegionInfo> =
      if (regionInfoMap.containsKey(region.name)) {
          Observable.just(regionInfoMap[region.name])
      } else {
          regionInfoService.fetchRegionInfoAsync(region.urLs!!, region.name)
                  .doOnNext { regionInfoMap[region.name!!] = it }
      }
}