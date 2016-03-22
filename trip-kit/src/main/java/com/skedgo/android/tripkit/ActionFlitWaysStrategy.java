package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripSegment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class ActionFlitWaysStrategy implements ExternalActionStrategy {

  private final Resources resources;
  private final Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory;

  public ActionFlitWaysStrategy(Resources resources,
                                @NonNull Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory) {
    this.resources = resources;
    this.reverseGeocoderFactory = reverseGeocoderFactory;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {

    final BookingAction.Builder actionBuilder = BookingAction.builder();

    actionBuilder.bookingProvider(BookingResolver.FLITWAYS);
    final String flitWaysPartnerKey = params.flitWaysPartnerKey();
    if (flitWaysPartnerKey == null) {
      final Intent data = new Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://flitways.com"));
      final BookingAction action = actionBuilder
          .hasApp(false)
          .data(data)
          .build();
      return Observable.just(action);
    } else {
      // See https://flitways.com/deeplink.
      final TripSegment segment = params.segment();
      final Location departure = segment.getFrom();
      final Location arrival = segment.getTo();
      final long startTimeInSecs = segment.getStartTimeInSecs();
      final String timeZone = segment.getTimeZone();
      return Observable
          .fromCallable(new Func0<HttpUrl.Builder>() {
            @Override public HttpUrl.Builder call() {
              final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
              if (timeZone != null) {
                dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
              }

              final String tripDate = dateFormat.format(new Date(startTimeInSecs * 1000));
              return HttpUrl.parse("https://flitways.com/api/link")
                  .newBuilder()
                  .addQueryParameter("trip_date", tripDate)
                  .addQueryParameter("key", flitWaysPartnerKey);
            }
          })
          .flatMap(new Func1<HttpUrl.Builder, Observable<BookingAction>>() {
            @Override public Observable<BookingAction> call(final HttpUrl.Builder builder) {
              return Observable.combineLatest(
                  reverseGeocoderFactory.call(
                      ImmutableReverseGeocodingParams.builder()
                          .lat(departure.getLat())
                          .lng(departure.getLon())
                          .maxResults(1)
                          .build()),
                  reverseGeocoderFactory.call(
                      ImmutableReverseGeocodingParams.builder()
                          .lat(arrival.getLat())
                          .lng(arrival.getLon())
                          .maxResults(1)
                          .build()),
                  new Func2<String, String, BookingAction>() {
                    @Override public BookingAction call(String departureAddress, String arrivalAddress) {
                      final String url = builder
                          .addQueryParameter("pickup", departureAddress)
                          .addQueryParameter("destination", arrivalAddress)
                          .build()
                          .toString();
                      return actionBuilder
                          .hasApp(false)
                          .data(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                          .build();
                    }
                  }
              );
            }
          })
          .subscribeOn(Schedulers.io());
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return "Book with FlitWays"; // TODO: i18n.
  }
}
