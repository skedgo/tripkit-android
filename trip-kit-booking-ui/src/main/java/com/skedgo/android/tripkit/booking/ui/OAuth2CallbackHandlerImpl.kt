package com.skedgo.android.tripkit.booking.ui

import android.net.Uri
import com.skedgo.android.tripkit.booking.*
import rx.Observable
import rx.functions.Func1


class OAuth2CallbackHandlerImpl(private val externalOAuthService: ExternalOAuthService, private val bookingService: BookingService) : OAuth2CallbackHandler {

  override fun handleOAuthURL(form: BookingForm, uri: Uri, callback: String): Observable<BookingForm> {
    if (form != null) {
      // save code or show error
      val code = uri.getQueryParameter("code")
      if (code != null) {
        // get access token

        return externalOAuthService.getAccessToken(form, code, "authorization_code", callback)
            .flatMap { externalOAuth -> Observable.just(form.setAuthData(externalOAuth)) }
            .flatMap { bookingForm -> bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form)) }

      } else if (uri.getQueryParameter("error") != null) {
        return Observable.error<BookingForm>(Error(uri.getQueryParameter("error")))
      }
    }
    return Observable.error<BookingForm>(Error("No saved booking!"))
  }

  override fun handleRetryURL(bookingForm: BookingForm, uri: Uri): Observable<BookingForm> {
    return bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form))
  }
}