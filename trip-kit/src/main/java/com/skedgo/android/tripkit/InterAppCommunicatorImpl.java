package com.skedgo.android.tripkit;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
      isAppInstalled = deviceHasUber();
      packageId = "uber://";
      urlLink = "https://m.uber.com/sign-up";
    } else if (params.action().startsWith("lyft")) {
      // Also 'lyft_line', etc.
      isAppInstalled = deviceHasLyft();
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

  public boolean deviceHasUber() {
    return isPackageInstalled(UBER_PACKAGE);
  }

  public boolean deviceHasLyft() {
    return isPackageInstalled(LYFT_PACKAGE);
  }

  public String titleForExternalAction(Resources resources, String action) {
    if (action.equals("gocatch")) {
      return resources.getString(R.string.gocatch_a_taxi);
    } else if (action.equals("uber")) {
      return deviceHasUber()
          ? resources.getString(R.string.open_uber)
          : resources.getString(R.string.get_uber);

    } else if (action.startsWith("lyft")) { // also lyft_line, etc.
      return deviceHasLyft()
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

  private boolean isPackageInstalled(String packageId) {
    try {
      packageManager.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      // ignored.
    }
    return false;
  }
}
