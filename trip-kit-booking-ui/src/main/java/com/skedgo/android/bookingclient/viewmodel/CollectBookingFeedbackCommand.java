package com.skedgo.android.bookingclient.viewmodel;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.uservoice.uservoicesdk.Config;

import rx.Observable;

public interface CollectBookingFeedbackCommand extends Command<CollectBookingFeedbackCommand.Param, Config> {

  Observable<Config> executeAsync(final Param param);

  final class Param {
    public final FragmentActivity activity;

    public Param(@NonNull FragmentActivity activity) {
      this.activity = activity;
    }
  }
}