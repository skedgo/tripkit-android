package com.skedgo.android.tripkit;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

final class SingleReverseGeocoderFactory implements Func1<ReverseGeocodingParams, Observable<String>> {
  private final Context context;

  SingleReverseGeocoderFactory(@NonNull Context context) {
    this.context = context;
  }

  @Override public Observable<String> call(ReverseGeocodingParams params) {
    return Observable.create(new OnSubscribeReverseGeocode(context, params.lat(), params.lng(), params.maxResults()))
        .map(new Func1<List<Address>, String>() {
          @Override public String call(List<Address> addresses) {
            final Address address = addresses.get(0);
            return String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getLocality());
          }
        });
  }
}