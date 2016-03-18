package com.skedgo.android.tripkit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripSegment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

public final class InterAppCommunicatorImpl implements InterAppCommunicator {
  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private final PackageManager packageManager;
  private final Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory;

  public InterAppCommunicatorImpl(
      PackageManager packageManager, @NonNull Context context,
      @NonNull Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory) {
    this.packageManager = packageManager;
    this.reverseGeocoderFactory = reverseGeocoderFactory;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(
      InterAppCommunicatorParams params) {
    final BookingAction.Builder actionBuilder = BookingAction.builder();
    final String externalAction = params.action();
    if (externalAction.equals("uber")) {
      actionBuilder.bookingProvider(UBER);
      if (isPackageInstalled(UBER_PACKAGE)) {
        final BookingAction action = actionBuilder.hasApp(true).data(
            new Intent(Intent.ACTION_VIEW).setData(Uri.parse("uber://"))
        ).build();
        return Observable.just(action);
      } else {
        final BookingAction action = actionBuilder.hasApp(false).data(
            new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://m.uber.com/sign-up"))
        ).build();
        return Observable.just(action);
      }
    } else if (externalAction.startsWith("lyft")) {
      actionBuilder.bookingProvider(LYFT);
      if (isPackageInstalled(LYFT_PACKAGE)) {
        final BookingAction action = actionBuilder.hasApp(true).data(
            new Intent(Intent.ACTION_VIEW).setData(Uri.parse("lyft://"))
        ).build();
        return Observable.just(action);
      } else {
        final Intent data = new Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE));
        final BookingAction action = actionBuilder
            .hasApp(false)
            .data(data)
            .build();
        return Observable.just(action);
      }
    } else if (externalAction.equals("flitways")) {
      actionBuilder.bookingProvider(FLITWAYS);
      final String flitwaysPartnerKey = params.flitwaysPartnerKey();
      if (flitwaysPartnerKey == null) {
        final Intent data = new Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://flitways.com"));
        final BookingAction action = actionBuilder
            .hasApp(false)
            .data(data)
            .build();
        return Observable.just(action);
      } else {
        final TripSegment segment = params.tripSegment();
        final Location departure = segment.getFrom();
        final Location arrival = segment.getTo();
        final long startTimeInSecs = segment.getStartTimeInSecs();
        final String timeZone = segment.getTimeZone();
        return Observable
            .fromCallable(new Func0<HttpUrl.Builder>() {
              @Override public HttpUrl.Builder call() {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
                if (timeZone != null) {
                  dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
                }

                final String tripDate = dateFormat.format(new Date(startTimeInSecs * 1000));
                return HttpUrl.parse("https://flitways.com/api/link")
                    .newBuilder()
                    .addQueryParameter("trip_date", tripDate)
                    .addQueryParameter("partner_key", flitwaysPartnerKey);
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
                            .addQueryParameter("pick", departureAddress)
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
    } else if (externalAction.startsWith("http")) {
      final BookingAction action = actionBuilder
          .bookingProvider(WEB)
          .hasApp(false)
          .data(new Intent(Intent.ACTION_VIEW, Uri.parse(externalAction)))
          .build();
      return Observable.just(action);
    } else {
      return Observable.error(new UnsupportedOperationException());
    }
  }

  public String titleForExternalAction(Resources resources, String action) {
    if (action.equals("gocatch")) {
      return resources.getString(R.string.gocatch_a_taxi);
    } else if (action.equals("uber")) {
      return isPackageInstalled(UBER_PACKAGE)
          ? resources.getString(R.string.open_uber)
          : resources.getString(R.string.get_uber);

    } else if (action.startsWith("lyft")) { // also lyft_line, etc.
      return isPackageInstalled(LYFT_PACKAGE)
          ? resources.getString(R.string.open_lyft)
          : resources.getString(R.string.get_lyft);

    } else if (action.equals("flitways")) {
      return "Book with FlitWays"; // TODO: replace with string resource when available

    } else if (action.startsWith("tel:")) {
      if (action.contains("name=")) {
        String name = action.substring(action.indexOf("name=") + "name=".length());
        try {
          name = URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        return resources.getString(R.string.calltaxiformat, name);
      } else {
        return resources.getString(R.string.call);
      }

    } else if (action.startsWith("sms:")) {
      return "Send SMS"; // TODO: replace with string resource when available
    } else if (action.startsWith("http:") || action.startsWith("https:")) {
      return resources.getString(R.string.show_website);
    } else {
      return null;
    }

    // TODO: handle "ingogo"
  }

  private boolean isPackageInstalled(String packageName) {
    try {
      packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      // Ignored.
    }

    return false;
  }
}