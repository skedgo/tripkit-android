package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripSegment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observable;
import rx.functions.Func1;

final class GoCatchBookingResolver implements BookingResolver {
  private static final String GOCATCH_PACKAGE = "com.gocatchapp.goCatch";
  private static final String GOCATCH_CODE = "tripgo";

  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;
  private final Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory;

  public GoCatchBookingResolver(
      @NonNull Resources resources,
      @NonNull Func1<String, Boolean> isPackageInstalled,
      @NonNull Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
    this.reverseGeocoderFactory = reverseGeocoderFactory;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    if (isPackageInstalled.call(GOCATCH_PACKAGE)) {
      final TripSegment segment = params.segment();
      final Location departure = segment.getFrom();
      final Location arrival = segment.getTo();
      return reverseGeocoderFactory.call(
          ImmutableReverseGeocodingParams.builder()
              .lat(arrival.getLat())
              .lng(arrival.getLon())
              .maxResults(1)
              .build()
      ).map(new Func1<String, BookingAction>() {
        @Override public BookingAction call(String arrivalAddress) {
          try {
            arrivalAddress = URLEncoder.encode(arrivalAddress, "UTF-8");
          } catch (UnsupportedEncodingException ignored) {
          }

          // TODO: This is not working, it just opens the app. Question sent to GoCatch team.
          String url = "gocatch://referral?code=" + GOCATCH_CODE
              + "&destination=" + arrivalAddress
              + "&pickup=&lat=" + departure.getLat()
              + "&lng=" + departure.getLon();
          return BookingAction.builder()
              .bookingProvider(BookingResolver.GOCATCH)
              .hasApp(true)
              .data(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
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