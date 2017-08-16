package com.skedgo.android.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Location;
import skedgo.tripkit.routing.TripSegment;
import com.skedgo.android.tripkit.BookingAction;
import com.skedgo.android.tripkit.ExternalActionParams;
import com.skedgo.android.tripkit.Geocodable;
import com.skedgo.android.tripkit.R;

import rx.Observable;
import rx.functions.Func1;

final class GoCatchBookingResolver implements BookingResolver {
  private static final String GOCATCH_PACKAGE = "com.gocatchapp.goCatch";
  private static final String GOCATCH_CODE = "tripgo";

  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;
  private final Geocodable geocoderFactory;

  GoCatchBookingResolver(
      @NonNull Resources resources,
      @NonNull Func1<String, Boolean> isPackageInstalled,
      @NonNull Geocodable geocoderFactory) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
    this.geocoderFactory = geocoderFactory;
  }

  @Override
  public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    if (isPackageInstalled.call(GOCATCH_PACKAGE)) {
      final TripSegment segment = params.segment();
      final Location departure = segment.getFrom();
      final Location arrival = segment.getTo();
      return geocoderFactory.getAddress(arrival.getLat(), arrival.getLon())
          .map(new Func1<String, BookingAction>() {
            @Override public BookingAction call(String arrivalAddress) {
              final Uri uri = Uri.parse("gocatch://referral")
                  .buildUpon()
                  .appendQueryParameter("code", GOCATCH_CODE)
                  .appendQueryParameter("destination", arrivalAddress)
                  .appendQueryParameter("pickup", "")
                  .appendQueryParameter("lat", String.valueOf(departure.getLat()))
                  .appendQueryParameter("lng", String.valueOf(departure.getLon()))
                  .build();
              return BookingAction.builder()
                  .bookingProvider(BookingResolver.GOCATCH)
                  .hasApp(true)
                  .data(new Intent(Intent.ACTION_VIEW).setData(uri))
                  .build();
            }
          });
    } else {
      final Intent data = new Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + GOCATCH_PACKAGE));
      final BookingAction action = BookingAction.builder()
          .bookingProvider(BookingResolver.GOCATCH)
          .hasApp(false)
          .data(data)
          .build();
      return Observable.just(action);
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return resources.getString(R.string.gocatch_a_taxi);
  }
}