package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface InterAppCommunicator {

  String ACTION_BOOK = "com.skedgo.android.common.ACTION_BOOK";
  String ACTION_BOOK2 = "com.skedgo.android.common.ACTION_BOOK2";
  String KEY_URL = "url";
  String KEY_FIRST_SCREEN = "firstScreen";

  @NonNull Intent getLyftIntent(@Nullable String publisherId, PackageManager packageManager);
  @NonNull Intent getFlitWaysIntent(@NonNull String flitWaysPartnerKey, @NonNull String pick, @NonNull String destination, @NonNull String date);
  @NonNull Intent getUberIntent(@Nullable PackageManager packageManager);
  @NonNull Intent getSupportBookingIntent(String url);
  @NonNull Intent getURLIntent(String url);

}
