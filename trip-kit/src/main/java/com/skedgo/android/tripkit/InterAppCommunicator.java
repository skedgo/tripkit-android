package com.skedgo.android.tripkit;

import rx.Observable;

public interface InterAppCommunicator {
  int UBER = 0;
  int LYFT = UBER + 1;
  int FLITWAYS = LYFT + 1;
  int WEB = FLITWAYS + 1;

  Observable<BookingAction> performExternalActionAsync(InterAppCommunicatorParams params);
}