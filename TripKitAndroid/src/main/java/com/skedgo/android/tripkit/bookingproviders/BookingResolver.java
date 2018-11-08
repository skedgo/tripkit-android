package com.skedgo.android.tripkit.bookingproviders;

import androidx.annotation.Nullable;

import com.skedgo.android.tripkit.BookingAction;
import com.skedgo.android.tripkit.ExternalActionParams;

import rx.Observable;

public interface BookingResolver {
  int UBER = 0;
  int LYFT = UBER + 1;
  int FLITWAYS = LYFT + 1;
  int GOCATCH = FLITWAYS + 1;
  int INGOGO = GOCATCH + 1;
  int MTAXI = INGOGO + 1;
  int SMS = MTAXI + 1;
  int OTHERS = SMS + 1;

  Observable<BookingAction> performExternalActionAsync(ExternalActionParams params);
  @Nullable String getTitleForExternalAction(String externalAction);
}