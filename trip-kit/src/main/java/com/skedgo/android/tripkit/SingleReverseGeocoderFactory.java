package com.skedgo.android.tripkit;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

final class SingleReverseGeocoderFactory implements Func1<ReverseGeocodingParams, Observable<String>> {
  private final Context context;

  SingleReverseGeocoderFactory(@NonNull Context context) {
    this.context = context;
  }

  @Override public Observable<String> call(ReverseGeocodingParams params) {
    return Observable.create(new OnSubscribeReverseGeocode(context, params.lat(), params.lng(), params.maxResults()))
        .filter(new Func1<List<Address>, Boolean>() {
          @Override public Boolean call(List<Address> addresses) {
            return isNotEmpty(addresses);
          }
        })
        .map(new Func1<List<Address>, String>() {
          @Override public String call(List<Address> addresses) {
            // See http://developer.android.com/training/location/display-address.html.
            final Address address = addresses.get(0);
            String[] addressLines = new String[address.getMaxAddressLineIndex()];
            for (int i = 0; i < addressLines.length; i++) {
              addressLines[i] = address.getAddressLine(i);
            }
            return TextUtils.join(
                System.getProperty("line.separator"),
                addressLines
            );
          }
        });
  }
}