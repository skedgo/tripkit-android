package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class TripUpdaterImpl implements TripUpdater {
  private final Resources resources;
  private final TripUpdateApi api;
  private final String apiVersion;

  TripUpdaterImpl(
      Resources resources,
      TripUpdateApi api,
      String apiVersion) {
    this.resources = resources;
    this.api = api;
    this.apiVersion = apiVersion;
  }

  @NonNull @Override
  public Observable<Trip> getUpdateAsync(@NonNull Trip trip) {
    if (TextUtils.isEmpty(trip.getUpdateURL())) {
      return Observable.empty();
    }

    return api.fetchUpdateAsync(trip.getUpdateURL(), apiVersion)
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse response) {
            response.processRawData(resources, GsonProvider.get());
            return response.getTripGroupList();
          }
        })
        .filter(new Func1<List<TripGroup>, Boolean>() {
          @Override public Boolean call(List<TripGroup> groups) {
            return CollectionUtils.isNotEmpty(groups);
          }
        })
        .map(new Func1<List<TripGroup>, Trip>() {
          @Override public Trip call(List<TripGroup> groups) {
            return groups.get(0).getDisplayTrip();
          }
        })
        .filter(new Func1<Trip, Boolean>() {
          @Override public Boolean call(Trip trip) {
            return trip != null;
          }
        })
        .subscribeOn(Schedulers.io());
  }
}