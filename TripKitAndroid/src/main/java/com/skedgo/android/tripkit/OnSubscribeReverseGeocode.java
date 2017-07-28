package com.skedgo.android.tripkit;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

final class OnSubscribeReverseGeocode implements Observable.OnSubscribe<List<Address>> {
  private final Context context;
  private final double latitude;
  private final double longitude;
  private final int maxResults;

  public OnSubscribeReverseGeocode(
      Context context,
      double latitude,
      double longitude,
      int maxResults) {
    this.context = context;
    this.latitude = latitude;
    this.longitude = longitude;
    this.maxResults = maxResults;
  }

  @Override public void call(Subscriber<? super List<Address>> subscriber) {
    if (!Geocoder.isPresent()) {
      subscriber.onError(new UnsupportedOperationException("Geocoder Not available"));
      return;
    }
    final Geocoder geocoder = new Geocoder(context);
    try {
      subscriber.onNext(geocoder.getFromLocation(latitude, longitude, maxResults));
      subscriber.onCompleted();
    } catch (Exception e) {
      subscriber.onError(e);
    }
  }
}