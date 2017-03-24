package com.skedgo.android.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.tripkit.BookingAction;
import com.skedgo.android.tripkit.ExternalActionParams;
import com.skedgo.android.tripkit.R;
import com.skedgo.android.tripkit.bookingproviders.BookingResolver;

import rx.Observable;

final class WebBookingResolver implements BookingResolver {
  private final Resources resources;

  public WebBookingResolver(@NonNull Resources resources) {
    this.resources = resources;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction action = BookingAction.builder()
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