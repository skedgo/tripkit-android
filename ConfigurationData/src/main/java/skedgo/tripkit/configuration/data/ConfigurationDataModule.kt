package skedgo.tripkit.configuration.data

import android.content.Context
import dagger.Module
import dagger.Provides
import skedgo.tripkit.configuration.domain.ApiKeyRepository
import skedgo.tripkit.configuration.domain.RegionEligibilityRepository

@Module
class ConfigurationDataModule {
  @Provides fun regionEligibilityRepository(context: Context): RegionEligibilityRepository
      = RegionEligibilityRepositoryImpl(context.getSharedPreferences(
      "RegionEligibilityPreferences",
      Context.MODE_PRIVATE
  ))

  @Provides fun apiKeyRepository(context: Context): ApiKeyRepository
      = ApiKeyRepositoryImpl(context.getSharedPreferences(
      "ApiKeyPrefs",
      Context.MODE_PRIVATE
  ))
}
