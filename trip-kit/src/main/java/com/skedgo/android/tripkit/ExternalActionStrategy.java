package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import rx.Observable;

public interface ExternalActionStrategy {

  Observable<BookingAction> performExternalActionAsync(ExternalActionParams params);
  @Nullable String getTitleForExternalAction(String externalAction);
}
