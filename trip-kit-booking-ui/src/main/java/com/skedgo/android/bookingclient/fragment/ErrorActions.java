package com.skedgo.android.bookingclient.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.skedgo.android.bookingclient.R;

import rx.functions.Action1;

import static com.skedgo.android.common.util.LogUtils.LOGE;
import static com.skedgo.android.common.util.LogUtils.makeTag;

public final class ErrorActions {
  private ErrorActions() {}

  public static Action1<Throwable> toast(@NonNull final Context context,
                                         @StringRes final int message) {
    return new Action1<Throwable>() {
      @Override
      public void call(Throwable error) {
        LOGE(makeTag(ErrorActions.class), error.getMessage(), error);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      }
    };
  }

  public static Action1<Throwable> showUnexpectedError(@NonNull final Context context) {
    return toast(context, R.string.nicely_informed_error);
  }
}