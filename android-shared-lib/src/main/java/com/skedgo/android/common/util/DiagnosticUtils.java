package com.skedgo.android.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import static com.skedgo.android.common.util.LogUtils.LOGE;
import static com.skedgo.android.common.util.LogUtils.makeTag;

public class DiagnosticUtils {

  private static final String TAG = makeTag("DiagnosticUtils");

  public static String getDeviceInfo() {
    return Build.MANUFACTURER + " " + Build.MODEL + " (api_level=" + Api.LEVEL + ")";
  }

  public static String getAppVersionName(Context context) {
    PackageManager packageManager = context.getPackageManager();
    try {
      if (packageManager != null) {
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName;
      }
    } catch (PackageManager.NameNotFoundException e) {
      LOGE(TAG, "Error while getting app version name", e);
    }

    return null;
  }

  public static int getAppVersionCode(Context context) {
    PackageManager packageManager = context.getPackageManager();
    try {
      if (packageManager != null) {
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
      }
    } catch (PackageManager.NameNotFoundException e) {
      LOGE(TAG, "Error while getting app version code", e);
    }

    return -1;
  }

  public static String getAppName(Context context) {
    PackageManager packageManager = context.getPackageManager();
    ApplicationInfo appInfo = null;
    try {
      if (packageManager != null) {
        appInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
      }
    } catch (PackageManager.NameNotFoundException e) {
      LOGE(TAG, "Error while getting app name", e);
    }

    return (String) (appInfo != null ? packageManager.getApplicationLabel(appInfo) : "Unknown app");
  }

  public static void setComponentEnabledSetting(Context context, Class<?> componentClass, int state) {
    ComponentName componentName = new ComponentName(context, componentClass);
    PackageManager packageManager = context.getPackageManager();
    if (packageManager == null) {
      return;
    }

    try {
      packageManager.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
    } catch (Exception e) {
      LOGE(TAG, "Error during setComponentEnabledSetting()", e);
    }
  }
}