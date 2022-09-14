package com.skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import com.skedgo.tripkit.account.domain.UserKeyRepository
import com.skedgo.tripkit.account.domain.UserTokenRepository
import com.skedgo.tripkit.configuration.Server
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class AccountDataModule {

  companion object {
    const val UserTokenPreferences = "UserTokenPreferences"
  }

  @Provides
  fun userTokenRepository(
      httpClient: OkHttpClient, @Named(UserTokenPreferences) prefs: SharedPreferences
  ): UserTokenRepository {
    val silentLoginApi = Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.value)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(SilentLoginApi::class.java)

    val accountApi = Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.value)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(AccountApi::class.java)
    return UserTokenRepositoryImpl(prefs, silentLoginApi, accountApi)
  }

  @Provides
  fun userRepository(@Named(UserTokenPreferences) prefs: SharedPreferences): UserKeyRepository =
      UserKeyRepositoryImpl(prefs)


  @Provides
  @Named(UserTokenPreferences)
  fun userTokenPreferences(context: Context): SharedPreferences =
      context.getSharedPreferences(UserTokenPreferences, Context.MODE_PRIVATE)
}