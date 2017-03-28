package skedgo.tripkit.configuration.data

import android.content.Context
import dagger.Module
import dagger.Provides
import skedgo.tripkit.configuration.domain.RegionEligibilityRepository

@Module
class ConfigurationDataModule {
  @Provides fun regionEligibilityRepository(context: Context): RegionEligibilityRepository {
    val preferences = context.getSharedPreferences(
        "RegionEligibilityPreferences",
        Context.MODE_PRIVATE
    )
    return RegionEligibilityRepositoryImpl(preferences)
  }
}
