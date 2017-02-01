package com.skedgo.android.tripkit.waypoints;

import com.google.gson.Gson;
import com.skedgo.android.tripkit.TripKit;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class WaypointsModule {

  @Provides WaypointApi waypointApi(okhttp3.OkHttpClient httpClient, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(WaypointApi.class);
  }

  @Provides WaypointService getWaypointService(WaypointApi api, Gson gson) {
    return new WaypointService(api, TripKit.singleton().configs().context().getResources(), gson);
  }

  @Provides GetTripByChangingService getTripsByChangingService(WaypointService waypointService,
                                                               WaypointSegmentAdapterUtils waypointSegmentAdapterUtils) {
    return new GetTripsForChangingServiceImpl(waypointService, waypointSegmentAdapterUtils);
  }

  @Provides GetTripByChangingStop getTripsByChangingStop(WaypointService waypointService,
                                                         WaypointSegmentAdapterUtils waypointSegmentAdapterUtils) {

    return new GetTripsForChangingStopImpl(waypointService, waypointSegmentAdapterUtils);
  }

  @Provides
  WaypointsComponent getWaypointsComponent(GetTripByChangingService getTripByChangingService,
                                           GetTripByChangingStop getTripByChangingStop) {

    return new WaypointsComponentImpl(getTripByChangingStop, getTripByChangingService);
  }

}
