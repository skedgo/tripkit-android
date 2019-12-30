package com.skedgo.tripkit

import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import com.skedgo.tripkit.configuration.GetAppVersion
import java.util.*

/**
 * @suppress
 */
@Module
open class HttpClientModule {

  @Provides open fun httpClientBuilder(): OkHttpClient.Builder
      = OkHttpClient.Builder()

  @Provides internal fun addCustomHeaders(
          getAppVersion: GetAppVersion,
          uuidProviderLazy: Lazy<com.skedgo.tripkit.UuidProvider>,
          configs: com.skedgo.tripkit.Configs
  ): com.skedgo.tripkit.AddCustomHeaders
      = com.skedgo.tripkit.AddCustomHeaders(
          getAppVersion,
          { Locale.getDefault() },
          if (configs.isUuidOptedOut) null else uuidProviderLazy.get(),
          configs.userTokenProvider(),
          { configs.key().call() })
}
