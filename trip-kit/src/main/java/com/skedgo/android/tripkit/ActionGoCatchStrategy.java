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
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class ActionGoCatchStrategy implements ExternalActionStrategy {

  private static final String GOCATCH_PACKAGE = "com.gocatchapp.goCatch";
  private static final String GOCATCH_CODE = "tripgo";

  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;
  private final Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory;

  public ActionGoCatchStrategy(Resources resources, Func1<String, Boolean> isPackageInstalled,
                               @NonNull Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
    this.reverseGeocoderFactory = reverseGeocoderFactory;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {

    final BookingAction.Builder actionBuilder = BookingAction.builder();

    actionBuilder.bookingProvider(BookingResolver.GOCATCH);

    if (isPackageInstalled.call(GOCATCH_PACKAGE)) {

      final TripSegment segment = params.segment();
      final Location departure = segment.getFrom();
      final Location arrival = segment.getTo();

      return Observable.create(new Observable.OnSubscribe<BookingAction>() {
        @Override public void call(final Subscriber<? super BookingAction> subscriber) {
          reverseGeocoderFactory.call(
              ImmutableReverseGeocodingParams.builder()
                  .lat(arrival.getLat())
                  .lng(arrival.getLon())
                  .maxResults(1)
                  .build())
              .subscribe(new Action1<String>() {
                @Override public void call(String arrivalAddress) {
                  try {
                    arrivalAddress = URLEncoder.encode(arrivalAddress, "UTF-8");
                  } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                  }

                  // TODO: this is not working, it just opens the app. Question sent to GoCatch team

                  String url = "gocatch://referral?code=" + GOCATCH_CODE
                      + "&destination=" + arrivalAddress + "&pickup=&lat=" + departure.getLat() + "&lng=" + departure.getLon();

                  final BookingAction action = actionBuilder.hasApp(true)
                      .data(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
                      ).build();
                  subscriber.onNext(action);
                  subscriber.onCompleted();
                }
              });
        }
      });

    } else {
      final Intent data = new Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + GOCATCH_PACKAGE));
      final BookingAction action = actionBuilder
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
