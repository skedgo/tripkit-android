package skedgo.tripkit.urlresolver

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class UrlResolverDataModule {

  @Provides fun getLastUsedRegionUrls(@Named("TripKitPrefs")
                                      preferences: SharedPreferences): GetLastUsedRegionUrls
      = GetLastUsedRegionUrlsImpl(preferences)
}