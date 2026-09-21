package com.skedgo.tripkit.validbookingcount.data

import android.content.Context
import com.skedgo.tripkit.configuration.ServerManager
import com.skedgo.tripkit.validbookingcount.domain.ValidBookingCountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ValidBookingCountDataModule {
    @Provides
    fun validBookingCountRepository(
        httpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): ValidBookingCountRepository {
        val api = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ValidBookingCountApi::class.java)
        val preferences = context.getSharedPreferences(
            "ValidBookingCountPreferences",
            Context.MODE_PRIVATE
        )
        return ValidBookingCountRepositoryImpl(api, preferences)
    }
}
