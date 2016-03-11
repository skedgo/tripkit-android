package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class InterAppCommunicatorImpl implements InterAppCommunicator {

  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private static final String LYFT_SITE_ID = "15284";
  private static final String LYFT_INVOKE_ID = "2908";

  @NonNull
  public Intent getLyftIntent(String publisherId, PackageManager packageManager) {

    String link;
    if (publisherId == null || isPackageInstalled(packageManager, LYFT_PACKAGE)) {
      link = "lyft://";
    } else {
      link = "https://212231.measurementapi.com/serve?action=click&publisher_id=" + publisherId + "&site_id=" + LYFT_SITE_ID + "&invoke_id=" + LYFT_INVOKE_ID;
    }

    Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
    playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    playStoreIntent.setData(Uri.parse(link));

    return playStoreIntent;

  }

  @NonNull
  public Intent getFlytwaysIntent(@NonNull String flitWaysPartnerKey, @NonNull String pick, @NonNull String destination, @NonNull String date) {

    String fkytwaysLink = "https://flitways.com/api/link?partner_key=" + flitWaysPartnerKey + "&pick=" + pick + "&destination=" + destination + "&trip_date=" + date;

    return new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse(fkytwaysLink));
  }

  @NonNull
  public Intent getUberIntent(@Nullable PackageManager packageManager) {
    if (packageManager == null) {
      return createUberSiteIntent();
    }

    try {
      packageManager.getPackageInfo(UBER_PACKAGE, PackageManager.GET_ACTIVITIES);
      return createUberAppIntent();
    } catch (PackageManager.NameNotFoundException e) {
      return createUberSiteIntent();
    }
  }

  @NonNull
  public Intent getSupportBookingIntent(String url) {
    return new Intent(ACTION_BOOK)
        .putExtra(KEY_URL, url)
        .putExtra(KEY_FIRST_SCREEN, true);
  }

  @NonNull
  public Intent getURLIntent(String url) {
    return new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse(url));
  }

  @NonNull
  private Intent createUberAppIntent() {
    return new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse("uber://"));
  }

  @NonNull
  private Intent createUberSiteIntent() {
    return new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse("https://m.uber.com/sign-up"));
  }

  private boolean isPackageInstalled(PackageManager pm, String packageId) {
    try {
      pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      // ignored.
    }
    return false;
  }
}
