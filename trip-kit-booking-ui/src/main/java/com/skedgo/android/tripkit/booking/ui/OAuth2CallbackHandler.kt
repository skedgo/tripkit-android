package com.skedgo.android.tripkit.booking.ui

import android.net.Uri
import com.skedgo.android.tripkit.booking.BookingForm
import io.reactivex.Observable

interface OAuth2CallbackHandler {
  fun handleOAuthURL(bookingForm: BookingForm, uri: Uri, callback: String): Observable<BookingForm>
  fun handleRetryURL(bookingForm: BookingForm, uri: Uri): Observable<BookingForm>
}