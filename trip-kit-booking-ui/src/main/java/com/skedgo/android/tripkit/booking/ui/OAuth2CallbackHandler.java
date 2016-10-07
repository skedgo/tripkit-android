package com.skedgo.android.tripkit.booking.ui;

import android.net.Uri;

import com.skedgo.android.tripkit.booking.BookingForm;

import rx.Observable;

public interface OAuth2CallbackHandler {
  Observable<BookingForm> handleOAuthURL(BookingForm bookingForm, Uri uri, String callback);
  Observable<BookingForm> handleRetryURL(BookingForm bookingForm, Uri uri);
}
