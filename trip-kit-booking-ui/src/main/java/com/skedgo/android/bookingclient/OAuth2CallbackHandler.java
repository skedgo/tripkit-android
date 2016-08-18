package com.skedgo.android.bookingclient;

import android.app.Activity;
import android.net.Uri;

import com.skedgo.android.tripkit.booking.BookingForm;

import rx.Observable;

public interface OAuth2CallbackHandler {
  Observable<BookingForm> handleOAuthURL(Activity activity, Uri uri, String callback);
  Observable<BookingForm> handleRetryURL(Activity activity, Uri uri);
}
