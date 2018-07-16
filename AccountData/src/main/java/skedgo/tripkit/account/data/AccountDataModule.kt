package skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import skedgo.tripkit.account.domain.GenerateUserKey
import skedgo.tripkit.account.domain.UserKeyRepository
import skedgo.tripkit.account.domain.UserTokenRepository
import skedgo.tripkit.configuration.Server
import javax.inject.Named

@Module
class AccountDataModule {
  @Provides
  fun userTokenRepository(
      httpClient: OkHttpClient,
      context: Context
  ): UserTokenRepository {
    val preferences = context.getSharedPreferences(
        "UserTokenPreferences",
        Context.MODE_PRIVATE
    )
    val silentLoginApi = Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.value)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(SilentLoginApi::class.java)

    val accountApi = Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.value)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(AccountApi::class.java)
    return UserTokenRepositoryImpl(preferences, silentLoginApi, accountApi)
  }

  @Provides
  fun userRepository(generateUserKey: GenerateUserKey,
                     @Named(AccountDataPrefsModule.AccountDataPrefs) prefs: SharedPreferences
  ): UserKeyRepository = UserKeyRepositoryImpl(prefs, generateUserKey)
}
