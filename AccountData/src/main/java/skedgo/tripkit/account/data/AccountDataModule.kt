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

  companion object {
    const val UserTokenPreferences = "UserTokenPreferences"
  }

  @Provides
  fun userTokenRepository(
      httpClient: OkHttpClient, @Named(UserTokenPreferences) prefs: SharedPreferences
  ): UserTokenRepository {
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
    return UserTokenRepositoryImpl(prefs, silentLoginApi, accountApi)
  }

  @Provides
  fun userRepository(generateUserKey: GenerateUserKey,
                     @Named(UserTokenPreferences) prefs: SharedPreferences): UserKeyRepository =
      UserKeyRepositoryImpl(prefs, generateUserKey)


  @Provides
  @Named(UserTokenPreferences)
  fun userTokenPreferences(context: Context): SharedPreferences =
      context.getSharedPreferences(UserTokenPreferences, Context.MODE_PRIVATE)
}