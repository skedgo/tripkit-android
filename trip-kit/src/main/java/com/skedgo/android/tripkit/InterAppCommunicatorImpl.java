package com.skedgo.android.tripkit;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import rx.functions.Action1;

public class InterAppCommunicatorImpl implements InterAppCommunicator {
  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private final PackageManager packageManager;

  public InterAppCommunicatorImpl(@NonNull PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  @Override public void performExternalAction(
      @NonNull InterAppCommunicatorParams params) {

    // Check if the external app is installed

    boolean isAppInstalled = false;
    String packageId = null;
    String urlLink = null;

    if (params.action().equals("uber")) {
      isAppInstalled = isPackageInstalled(packageManager, UBER_PACKAGE);
      packageId = "uber://";
      urlLink = "https://m.uber.com/sign-up";
    } else if (params.action().startsWith("lyft")) {
      // Also 'lyft_line', etc.
      isAppInstalled = isPackageInstalled(packageManager, LYFT_PACKAGE);
      packageId = "lyft://";
      urlLink = "https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE;
    } else if (params.action().equals("flitways")) {
      // web action handled will do the job
    } else if (params.action().startsWith("http")) {
      urlLink = params.action();
    }

    if (isAppInstalled) {
      params.openApp().call(packageId);
    } else {
      params.openWeb().call(urlLink);
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
