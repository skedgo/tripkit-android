package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import rx.Observable;

public class ActionGoCatchStrategy implements ExternalActionStrategy {

  private final Resources resources;

  public ActionGoCatchStrategy(Resources resources) {
    this.resources = resources;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    return Observable.error(new UnsupportedOperationException());
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return resources.getString(R.string.gocatch_a_taxi);
  }
}
