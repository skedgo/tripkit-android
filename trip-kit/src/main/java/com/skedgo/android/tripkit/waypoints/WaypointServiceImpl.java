package com.skedgo.android.tripkit.waypoints;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.TripGroup;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static rx.plugins.RxJavaHooks.onError;

public class WaypointServiceImpl implements WaypointService {

  private final WaypointApi api;
  private final Resources resources;
  private final Gson gson;

  @Inject public WaypointServiceImpl(WaypointApi api, Resources resources, Gson gson) {
    this.api = api;
    this.resources = resources;
    this.gson = gson;
  }

  @Override
  public Observable<List<TripGroup>> fetchChangedTripAsync(ConfigurationParams config, List<WaypointSegmentAdapter> segments) {

    WaypointBody body = ImmutableWaypointBody.builder()
        .config(config)
        .segments(segments)
        .build();
    return api.getChangedTrip(body)
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse routingResponse) {

            routingResponse.processRawData(resources, gson);
            ArrayList<TripGroup> tripGroups = routingResponse.getTripGroupList();
            if (CollectionUtils.isEmpty(tripGroups) || CollectionUtils.isEmpty(tripGroups.get(0).getTrips())) {
              onError(new RuntimeException("No groups found"));

            }

            return routingResponse.getTripGroupList();
          }
        });

  }
}
