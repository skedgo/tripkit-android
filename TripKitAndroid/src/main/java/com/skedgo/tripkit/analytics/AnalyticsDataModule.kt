/* FIXME: Move this class to the TripKitData module. */
package com.skedgo.tripkit.analytics

import com.google.gson.Gson
import com.skedgo.tripkit.configuration.ServerManager
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @suppress
 */
@Module
class AnalyticsDataModule {
    @Provides
    fun markTripAsPlannedWithUserInfo(
        gson: Gson,
        httpClient: OkHttpClient
    ): MarkTripAsPlannedWithUserInfo {
        val markTripAsPlannedApi: MarkTripAsPlannedApi = reportingApi(gson, httpClient)
        return MarkTripAsPlannedWithUserInfoImpl(markTripAsPlannedApi)
    }

    private fun reportingApi(gson: Gson, httpClient: OkHttpClient): MarkTripAsPlannedApi =
        Retrofit.Builder()
            /* This base url is ignored as the api relies on @Url. */
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(MarkTripAsPlannedApi::class.java)
}
