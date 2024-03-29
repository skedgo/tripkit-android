package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import com.skedgo.tripkit.routing.TripSegment;
import com.skedgo.tripkit.geocoding.ReverseGeocodable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.HttpUrl;

final class FlitWaysBookingResolver implements BookingResolver {
  private final ReverseGeocodable geocoderFactory;

  FlitWaysBookingResolver(ReverseGeocodable geocoderFactory) {
    this.geocoderFactory = geocoderFactory;
  }

  @Override
  public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction.Builder actionBuilder = BookingAction.builder()
        .bookingProvider(FLITWAYS);
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
          .fromCallable(() -> {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
            if (timeZone != null) {
              dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            }

            final String tripDate = dateFormat.format(new Date(startTimeInSecs * 1000));
            return HttpUrl.parse("https://flitways.com/api/link")
                .newBuilder()
                .addQueryParameter("trip_date", tripDate)
                .addQueryParameter("key", flitWaysPartnerKey);
          })
          .flatMap((Function<HttpUrl.Builder, Observable<BookingAction>>) builder -> Observable.combineLatest(
              geocoderFactory.getAddress(departure.getLat(), departure.getLon()),
              geocoderFactory.getAddress(arrival.getLat(), arrival.getLon()),
                  (BiFunction<String, String, BookingAction>) (departureAddress, arrivalAddress) -> {
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
          ))
          .subscribeOn(Schedulers.io());
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return "Book with FlitWays"; // TODO: i18n.
  }
}