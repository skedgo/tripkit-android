package skedgo.tripkit.urlresolver

import android.content.SharedPreferences
import com.skedgo.android.common.model.Region
import rx.Observable

const val LAST_REGION_NAME = "last_region_name"
const val LAST_REGION_URLS = "last_region_urls"

internal open class GetLastUsedRegionUrlsImpl constructor(
    private val preferences: SharedPreferences
) :GetLastUsedRegionUrls {
  override fun setLastUsedRegionUrls(region: Region): Observable<Unit>
      = Observable.fromCallable {
    if (!isSavedRegion(region))
      preferences.edit()
          .putString(LAST_REGION_NAME, region.name)
          .putStringSet(LAST_REGION_URLS, region.urLs?.toSet())
          .apply()
  }

  override fun getLastUsedRegionUrls(): Observable<List<String>>
      = Observable.fromCallable {
    val urls = preferences.getStringSet(LAST_REGION_URLS, null)
    if (urls != null) {
      ArrayList(urls)
    } else {
      emptyList<String>()
    }
  }

  open fun isSavedRegion(region: Region): Boolean {
    val savedRegion = preferences.getString(LAST_REGION_NAME, null)
    return savedRegion?.equals(region.name) ?: false
  }
}