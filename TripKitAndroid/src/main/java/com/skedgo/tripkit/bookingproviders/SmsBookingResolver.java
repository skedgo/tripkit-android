package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import com.skedgo.tripkit.bookingproviders.BookingResolver;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import io.reactivex.Observable;

final class SmsBookingResolver implements BookingResolver {
  public SmsBookingResolver() {}

  /**
   * @param uri e.g. 'sms:12345' where '12345' is phone number.
   */
  @NonNull
  static Intent createSmsIntentByUri(String uri, String smsBody) {
    // Put EXTRA_TEXT to avoid missing body on Samsung devices running 5.0.
    return new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse(uri))
        .putExtra("sms_body", smsBody)
        .putExtra(Intent.EXTRA_TEXT, smsBody);
  }

  /**
   * @param action e.g. 'sms:12345?Body goes here'
   *               where '12345' is phone number and 'Body goes here' is sms body.
   */
  @NonNull
  static Intent createSmsIntentByAction(@NonNull String action) {
    final String[] parts = action.split("\\?");
    final String uri = parts.length > 0 ? parts[0] : null;
    final String smsBody = parts.length > 1 ? parts[1] : null;
    return createSmsIntentByUri(uri, smsBody);
  }

  @Override public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final BookingAction action = BookingAction.builder()
        .bookingProvider(SMS)
        .hasApp(false)
        .data(createSmsIntentByAction(params.action()))
        .build();
    return Observable.just(action);
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    return "Send SMS"; // TODO: i18n.
  }
}