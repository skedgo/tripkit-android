package skedgo.tripkit.configuration.data

import android.content.Context
import dagger.Module
import dagger.Provides
import skedgo.tripkit.configuration.domain.ApiKeyRepository

@Module
class ConfigurationDataModule {
  @Provides fun apiKeyRepository(context: Context): ApiKeyRepository
      = ApiKeyRepositoryImpl(context.getSharedPreferences(
      "ApiKeyPrefs",
      Context.MODE_PRIVATE
  ))
}
