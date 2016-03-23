package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import rx.Observable;

public interface BookingResolver {
  int UBER = 0;
  int LYFT = UBER + 1;
  int FLITWAYS = LYFT + 1;
  int GOCATCH = FLITWAYS + 1;
  int SMS = GOCATCH + 1;
  int OTHERS = SMS + 1;

  Observable<BookingAction> performExternalActionAsync(ExternalActionParams params);
  @Nullable String getTitleForExternalAction(String externalAction);
}