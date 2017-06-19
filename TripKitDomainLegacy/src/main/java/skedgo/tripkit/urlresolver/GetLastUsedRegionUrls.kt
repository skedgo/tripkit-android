package skedgo.tripkit.urlresolver

import com.skedgo.android.common.model.Region
import rx.Observable

interface GetLastUsedRegionUrls {
  open fun setLastUsedRegionUrls(region: Region): Observable<Unit>
  open fun getLastUsedRegionUrls(): Observable<List<String>>
}