package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.Nullable;

import rx.Observable;

public class ActionHttpStrategy implements ExternalActionStrategy {

  private final Resources resources;

  public ActionHttpStrategy(Resources resources) {
    this.resources = resources;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {

    final BookingAction.Builder actionBuilder = BookingAction.builder();

    final BookingAction action = actionBuilder
        .bookingProvider(BookingResolver.OTHERS)
        .hasApp(false)
        .data(new Intent(Intent.ACTION_VIEW, Uri.parse(params.action())))
        .build();
    return Observable.just(action);
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return resources.getString(R.string.show_website);
  }
}
