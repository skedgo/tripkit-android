package com.skedgo.android.tripkit;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.skedgo.android.common.model.Location;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class ReverseGeocoderHelper {

  private final Context context;

  public ReverseGeocoderHelper(Context context) {
    this.context = context;
  }

  public Observable<String> getAddress(final Location location) {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {

        if (location == null) {
          subscriber.onNext(null);
        } else if (location.getAddress() != null && !location.getAddress().isEmpty()) {
          subscriber.onNext(location.getAddress());
        } else {
          Geocoder geocoder = new Geocoder(context);
          double lat = location.getLat();
          double lng = location.getLon();

          List<Address> addresses = null;
          String addressText = null;

          try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
          } catch (IOException e) {
            e.printStackTrace();
          }

          if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);

            addressText = String.format("%s, %s",
                                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                        address.getLocality()
            );
          }

          subscriber.onNext(addressText);
        }
      }
    });
  }

}