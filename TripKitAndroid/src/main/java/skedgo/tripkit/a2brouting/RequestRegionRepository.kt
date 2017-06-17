package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Region
import rx.Completable
import rx.Observable

interface RequestRegionRepository {
  fun getRequestRegion(requestId: String): Observable<Region>
  fun putRequestRegion(requestId: String, region: Region): Completable
}
