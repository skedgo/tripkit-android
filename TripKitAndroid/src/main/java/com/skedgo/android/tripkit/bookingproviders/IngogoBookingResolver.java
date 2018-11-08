package com.skedgo.android.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skedgo.android.tripkit.BookingAction;
import com.skedgo.android.tripkit.ExternalActionParams;
import com.skedgo.android.tripkit.R;
import com.skedgo.android.tripkit.bookingproviders.BookingResolver;

import rx.Observable;
import rx.functions.Func1;

final class IngogoBookingResolver implements BookingResolver {
  private static final String INGOGO_PACKAGE = "com.ingogo.passenger";
  private final Resources resources;
  private final Func1<String, Boolean> isPackageInstalled;

  public IngogoBookingResolver(
      @NonNull Resources resources,
      @NonNull Func1<String, Boolean> isPackageInstalled) {
    this.resources = resources;
    this.isPackageInstalled = isPackageInstalled;
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction.Builder actionBuilder = BookingAction.builder();
    actionBuilder.bookingProvider(BookingResolver.INGOGO);
    if (isPackageInstalled.call(INGOGO_PACKAGE)) {
      final BookingAction action = actionBuilder.hasApp(true).data(
          new Intent(Intent.ACTION_VIEW).setData(Uri.parse("ingogo://"))
      ).build();
      return Observable.just(action);
    } else {
      final Intent data = new Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + INGOGO_PACKAGE));
      final BookingAction action = actionBuilder
          .hasApp(false)
          .data(data)
          .build();
      return Observable.just(action);
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return resources.getString(R.string.ingogo_a_taxi);
  }
}