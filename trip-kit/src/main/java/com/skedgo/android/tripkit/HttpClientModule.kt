package com.skedgo.android.tripkit

import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import skedgo.tripkit.configuration.GetAppVersion
import java.util.*

@Module
open class HttpClientModule {
  @Provides open fun httpClientBuilder(): OkHttpClient.Builder
      = OkHttpClient.Builder()

  @Provides internal fun addCustomHeaders(
      getAppVersion: GetAppVersion,
      uuidProviderLazy: Lazy<UuidProvider>,
      configs: Configs
  ): AddCustomHeaders
      = AddCustomHeaders(
      getAppVersion,
      { Locale.getDefault() },
      if (configs.isUuidOptedOut) null else uuidProviderLazy.get(),
      configs.userTokenProvider(),
      { configs.key().call() })
}
