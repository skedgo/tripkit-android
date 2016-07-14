package com.skedgo.android.bookingclient;

import android.app.Activity;
import android.net.Uri;

import com.skedgo.android.tripkit.booking.BookingForm;

import rx.Observable;

public interface OAuth2CallbackHandler {
  Observable<BookingForm> handleURL(Activity activity, Uri uri, String callback);
}
