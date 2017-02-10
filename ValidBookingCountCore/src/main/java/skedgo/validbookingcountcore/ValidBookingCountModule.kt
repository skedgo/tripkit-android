package skedgo.validbookingcountcore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Named

@Module
class ValidBookingCountModule {
  @Provides
  fun validBookingCountApi(httpClient: OkHttpClient, gson: Gson): ValidBookingCountApi {
    return Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://tripgo.skedgo.com/satapp/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ValidBookingCountApi::class.java)
  }

  @Provides @Named("ValidBookingCountPreferences")
  fun validBookingCountPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("ValidBookingCountPreferences", Context.MODE_PRIVATE);
  }
}
