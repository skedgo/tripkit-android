package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import rx.functions.Action1;

public interface InterAppCommunicator {
  void performExternalAction(@NonNull String action, @NonNull Action1<String> openApp,
                             @NonNull Action1<String> openWeb);
}