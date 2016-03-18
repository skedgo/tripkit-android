package com.skedgo.android.tripkit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;

public class InterAppCommunicatorImpl implements InterAppCommunicator {
  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private final Context context;
  private final ReverseGeocoderHelper helper;

  public InterAppCommunicatorImpl(@NonNull Context context) {
    this.context = context;
    this.helper = new ReverseGeocoderHelper(context);
  }

  @Override public void performExternalAction(
      @NonNull final InterAppCommunicatorParams params) {

    final Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
    playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    final Intent webIntent = new Intent(Intent.ACTION_VIEW);

    if (params.action().equals("uber")) {
      if (isPackageInstalled(UBER_PACKAGE)) {
        playStoreIntent.setData(Uri.parse("uber://"));
        params.openApp().call(UBER, playStoreIntent);
      } else {
        webIntent.setData(Uri.parse("https://m.uber.com/sign-up"));
        params.openWeb().call(UBER, webIntent);
      }
    } else if (params.action().startsWith("lyft")) {
      // Also 'lyft_line', etc.
      if (isPackageInstalled(LYFT_PACKAGE)) {
        playStoreIntent.setData(Uri.parse("lyft://"));
        params.openApp().call(LYFT, playStoreIntent);
      } else {
        webIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE));
        params.openWeb().call(LYFT, webIntent);
      }
    } else if (params.action().equals("flitways")) {
      resolveFlitWaysURL(params).subscribe(new Action1<String>() {
        @Override public void call(String link) {
          webIntent.setData(Uri.parse(link));
          params.openWeb().call(FLITWAYS, webIntent);
        }
      });

    } else if (params.action().startsWith("http")) {
      webIntent.setData(Uri.parse(params.action()));
      params.openWeb().call(WEB, webIntent);
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

  protected Observable<String> resolveFlitWaysURL(final InterAppCommunicatorParams params) {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(final Subscriber<? super String> mainSubscriber) {
        String urlString = null;

        String flitwaysPartnerkey = TripKit.singleton().getInterAppConfiguration().getFlitwaysPartnerkey();
        if (flitwaysPartnerkey != null) {
          // https://flitways.com/api/link?partner_key=PARTNER_KEY&pick=PICKUP_ADDRESS&destination=DESTINATION_ADDRESS&trip_date=PICKUP_DATETIME
          // Partner Key – Required
          // Pick Up Address – Optional
          // Destination – Optional
          // Pickup DateTime – Optional

          final HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
              .scheme("https")
              .host("flitways.com")
              .addPathSegment("api")
              .addPathSegment("link")
              .addQueryParameter("partner_key", flitwaysPartnerkey);
          if (params.tripSegment() != null) {
            long startTime = params.tripSegment().getStartTimeInSecs();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone(params.tripSegment().getTimeZone()));

            String time = dateFormat.format(new Date(startTime * 1000));
            urlBuilder.addQueryParameter("trip_date", time);

            Observable.combineLatest(
                helper.getAddress(params.tripSegment().getFrom()),
                helper.getAddress(params.tripSegment().getTo()),
                new Func2<String, String, Void>() {
                  @Override public Void call(String fromAddress, String toAddress) {
                    if (fromAddress != null) {
                      urlBuilder.addQueryParameter("pick", fromAddress);
                    }
                    if (toAddress != null) {
                      urlBuilder.addQueryParameter("destination", toAddress);
                    }
                    mainSubscriber.onNext(urlBuilder.build().toString());

                    return null;
                  }
                }
            ).subscribe();

          }

        } else {
          urlString = "https://flitways.com";

          mainSubscriber.onNext(urlString);
        }

      }
    });

  }

  private boolean isPackageInstalled(String packageId) {
    try {
      context.getPackageManager().getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      // ignored.
    }
    return false;
  }
}