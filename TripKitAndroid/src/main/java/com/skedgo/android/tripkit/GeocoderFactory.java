package com.skedgo.android.tripkit;

import android.content.Context;
import android.location.Address;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GeocoderFactory {
  private final Context context;

  public GeocoderFactory(Context context) {
    this.context = context;
  }

  public Observable<String> firstAddressAsync(
      double latitude,
      double longitude,
      int maxResults) {
    final OnSubscribeReverseGeocode onSubscribe = new OnSubscribeReverseGeocode(
        context, latitude, longitude, maxResults
    );
    return Observable.create(onSubscribe)
        .filter(new Func1<List<Address>, Boolean>() {
          @Override public Boolean call(List<Address> addresses) {
            return addresses != null && !addresses.isEmpty();
          }
        })
        .map(new Func1<List<Address>, String>() {
          @Override public String call(List<Address> addresses) {
            // The old way used solution in
            // http://developer.android.com/training/location/display-address.html.
            // but we get empty string when getMaxAddressLineIndex = 0
            // while getAddressLine() has got their string address.
            // So that I suggest that we should calculate addressLine based on
            // the actual addressLine of address.
            final Address address = addresses.get(0);
            String addressDetail = address.getAddressLine(0) != null
                ? address.getAddressLine(0) : "";
            int index = 1;
            while (address.getAddressLine(index) != null) {
              addressDetail = addressDetail.concat(" " + address.getAddressLine(index++));
            }
            return addressDetail;
          }
        })
        .subscribeOn(Schedulers.io());
  }
}