package com.skedgo.android.tripkit;

import rx.Observable;

public interface BookingResolver {
  int UBER = 0;
  int LYFT = UBER + 1;
  int FLITWAYS = LYFT + 1;
  int OTHERS = FLITWAYS + 1;

  Observable<BookingAction> performExternalActionAsync(ExternalActionParams params);
  String getTitleForExternalAction(String externalAction);
}