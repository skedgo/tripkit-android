package skedgo.tripkit.a2brouting

import android.content.SharedPreferences
import com.skedgo.android.common.model.Region
import com.skedgo.android.tripkit.RegionService
import rx.Completable
import rx.Observable
import javax.inject.Inject

internal class RequestRegionRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val regionService: RegionService
) : RequestRegionRepository {
  override fun getRequestRegion(requestId: String): Observable<Region> {
    TODO()
  }

  override fun putRequestRegion(requestId: String, region: Region): Completable {
    TODO()
  }
}