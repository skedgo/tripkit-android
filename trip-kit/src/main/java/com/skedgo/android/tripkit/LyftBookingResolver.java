package com.skedgo.android.tripkit;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Func1;

final class LyftBookingResolver implements BookingResolver {
  private static final String LYFT_PACKAGE = "me.lyft.android";
  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;

  public LyftBookingResolver(
      @NonNull Resources resources,
      @NonNull Func1<String, Boolean> isPackageInstalled) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction.Builder actionBuilder = BookingAction.builder();
    actionBuilder.bookingProvider(BookingResolver.LYFT);
    if (isPackageInstalled.call(LYFT_PACKAGE)) {
      final BookingAction action = actionBuilder.hasApp(true).data(
          new Intent(Intent.ACTION_VIEW).setData(Uri.parse("lyft://"))
      ).build();
      return Observable.just(action);
    } else {
      final Intent data = new Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE));
      final BookingAction action = actionBuilder
          .hasApp(false)
          .data(data)
          .build();
      return Observable.just(action);
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return isPackageInstalled.call(LYFT_PACKAGE)
        ? resources.getString(R.string.open_lyft)
        : resources.getString(R.string.get_lyft);
  }
}