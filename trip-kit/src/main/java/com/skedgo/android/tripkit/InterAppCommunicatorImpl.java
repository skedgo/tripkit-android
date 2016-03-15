package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class InterAppCommunicatorImpl implements InterAppCommunicator {
  private static final String UBER_PACKAGE = "com.ubercab";
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private final PackageManager packageManager;

  public InterAppCommunicatorImpl(@NonNull PackageManager packageManager) {
    this.packageManager = packageManager;
  }

  @Override public void performExternalAction(
      @NonNull InterAppCommunicatorParams params) {

    Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
    playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    Intent webIntent = new Intent(Intent.ACTION_VIEW);

    if (params.action().equals("uber")) {
      if (deviceHasUber()) {
        playStoreIntent.setData(Uri.parse("uber://"));
        params.openApp().call(UBER, playStoreIntent);
      } else {
        webIntent.setData(Uri.parse("https://m.uber.com/sign-up"));
        params.openWeb().call(UBER, webIntent);
      }
    } else if (params.action().startsWith("lyft")) {
      // Also 'lyft_line', etc.
      if (deviceHasLyft()) {
        playStoreIntent.setData(Uri.parse("lyft://"));
        params.openApp().call(LYFT, playStoreIntent);
      } else {
        webIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE));
        params.openWeb().call(LYFT, webIntent);
      }
    } else if (params.action().equals("flitways")) {
      webIntent.setData(Uri.parse("https://flitways.com"));
      params.openWeb().call(FLITWAYS, webIntent);
    } else if (params.action().startsWith("http")) {
      webIntent.setData(Uri.parse(params.action()));
      params.openWeb().call(WEB, webIntent);
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
