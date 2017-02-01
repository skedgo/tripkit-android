package com.skedgo.android.tripkit.waypoints

import com.google.gson.Gson
import com.skedgo.android.tripkit.TripKit
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers

@Module class WaypointsModuleKt {


    @Provides fun waypointApi(httpClient: okhttp3.OkHttpClient, gson: Gson) =
            Retrofit.Builder()
                    .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient)
                    .build()
                    .create(WaypointApi::class.java)


    @Provides fun getWaypointService(api: WaypointApi, gson: Gson): WaypointService =
            WaypointService(api, TripKit.singleton().configs().context().resources, gson)


    @Provides fun getTripsByChangingService(waypointService: WaypointService,
                                            waypointSegmentAdapterUtils: WaypointSegmentAdapterUtils)
            : GetTripByChangingService =
            GetTripsForChangingServiceImpl(waypointService, waypointSegmentAdapterUtils)


    @Provides fun getTripsByChangingStop(waypointService: WaypointService,
                                         waypointSegmentAdapterUtils: WaypointSegmentAdapterUtils)
            : GetTripByChangingStop =
            GetTripsForChangingStopImpl(waypointService, waypointSegmentAdapterUtils)

    @Provides fun getWaypointsComponent(getTripByChangingService: GetTripByChangingService,
                                        getTripByChangingStop: GetTripByChangingStop): WaypointsComponent =
            WaypointsComponentImpl(getTripByChangingStop, getTripByChangingService)

}