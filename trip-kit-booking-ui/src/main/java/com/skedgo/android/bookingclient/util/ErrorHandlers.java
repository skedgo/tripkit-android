package com.skedgo.android.bookingclient.util;

import android.util.Log;


import com.crashlytics.android.Crashlytics;
import com.skedgo.android.bookingclient.BuildConfig;

import rx.functions.Action1;

public final class ErrorHandlers {
  private ErrorHandlers() {}

  /**
   * Just prints the error out for the sake of debugging.
   */
  public static Action1<Throwable> logError() {
    return new Action1<Throwable>() {
      @Override public void call(Throwable error) {
        if (BuildConfig.DEBUG) {
          Log.e(ErrorHandlers.class.getSimpleName(), null, error);
        }
      }
    };
  }

  /**
   * If debuggable, it just simply prints the error out.
   * Otherwise, it'll upload that error to Fabric for further investigation.
   * <p/>
   * Should use this to catch some errors that we may not be aware of.
   */
  public static Action1<Throwable> trackError() {
    return new Action1<Throwable>() {
      @Override public void call(Throwable error) {
        if (BuildConfig.DEBUG) {
          Log.e(ErrorHandlers.class.getSimpleName(), null, error);
        } else {
          Crashlytics.logException(error);
        }
      }
    };
  }
}