package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Func1;

final class UberBookingResolver implements BookingResolver {
  private static final String UBER_PACKAGE = "com.ubercab";
  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;

  public UberBookingResolver(
      @NonNull Resources resources,
      @NonNull Func1<String, Boolean> isPackageInstalled) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction.Builder actionBuilder = BookingAction.builder();
    actionBuilder.bookingProvider(BookingResolver.UBER);
    if (isPackageInstalled.call(UBER_PACKAGE)) {
      final BookingAction action = actionBuilder.hasApp(true).data(
          new Intent(Intent.ACTION_VIEW).setData(Uri.parse("uber://"))
      ).build();
      return Observable.just(action);
    } else {
      final BookingAction action = actionBuilder.hasApp(false).data(
          new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://m.uber.com/sign-up"))
      ).build();
      return Observable.just(action);
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return isPackageInstalled.call(UBER_PACKAGE)
        ? resources.getString(R.string.open_uber)
        : resources.getString(R.string.get_uber);
  }
}