package com.skedgo.android.tripkit;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.TripSegment;

import rx.functions.Action1;

public class InterAppCommunicatorImpl implements InterAppCommunicator {

  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";


  @Override public void performExternalAction(@NonNull String action,
                                              @NonNull Action1<String> openApp, @NonNull Action1<String> openWeb,
                                              PackageManager packageManager) {

    // Check if the external app is installed

    boolean isAppInstalled = false;
    String packageId = null;
    String urlLink = null;

    if (action.equals(TripSegment.UBER)) {
      isAppInstalled = isPackageInstalled(packageManager, UBER_PACKAGE);
      packageId = "uber://";
      urlLink = "https://m.uber.com/sign-up";
    } else if (action.startsWith(TripSegment.LYFT)) { // also lyft_line, etc.
      isAppInstalled = isPackageInstalled(packageManager, LYFT_PACKAGE);
      packageId = "lyft://";
      urlLink = "https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE;
    } else if (action.equals(TripSegment.FLITWAYS)) {
      // web action handled will do the job
    } else if (action.startsWith("http")) {
      urlLink = action;
    }

    if (isAppInstalled) {
      openApp.call(packageId);
    } else {
      openWeb.call(urlLink);
    }

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
