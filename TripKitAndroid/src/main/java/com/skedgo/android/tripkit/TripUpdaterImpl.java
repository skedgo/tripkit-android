package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import skedgo.tripkit.routing.RoutingResponse;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class TripUpdaterImpl implements TripUpdater {
  private final Resources resources;
  private final TripUpdateApi api;
  private final String apiVersion;
  private final Gson gson;

  TripUpdaterImpl(
      @NonNull Resources resources,
      @NonNull TripUpdateApi api,
      @NonNull String apiVersion,
      @NonNull Gson gson) {
    this.resources = resources;
    this.api = api;
    this.apiVersion = apiVersion;
    this.gson = gson;
  }

  @NonNull @Override
  public Observable<Trip> getUpdateAsync(@NonNull String tripUrl) {
    Trip trip = new Trip();
    trip.setUpdateURL(tripUrl);
    return getUpdateAsync(trip);
  }

  @NonNull @Override
  public Observable<Trip> getUpdateAsync(@NonNull Trip trip) {
    if (TextUtils.isEmpty(trip.getUpdateURL())) {
      return Observable.empty();
    }

    return api.fetchUpdateAsync(trip.getUpdateURL(), apiVersion)
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse response) {
            response.processRawData(resources, gson);
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