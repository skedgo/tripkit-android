package com.skedgo.tripkit.booking.ui

import android.net.Uri
import com.skedgo.tripkit.booking.*
import io.reactivex.Observable


class OAuth2CallbackHandlerImpl(private val externalOAuthService: ExternalOAuthService, private val bookingService: BookingService) : OAuth2CallbackHandler {

  override fun handleOAuthURL(bookingForm: BookingForm, uri: Uri, callback: String): Observable<BookingForm> {

    // save code or show error
    val code = uri.getQueryParameter("code")
    if (code != null) {
      // get access token
      return externalOAuthService.getAccessToken(bookingForm, code, "authorization_code", callback)
          .flatMap { externalOAuth -> Observable.just(bookingForm.setAuthData(externalOAuth)) }
          .flatMap { bookingForm -> bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form)).toObservable() }

    } else if (uri.getQueryParameter("error") != null) {
      return Observable.error<BookingForm>(Error(uri.getQueryParameter("error")))
    }

    return Observable.error<BookingForm>(Error("No saved booking!"))
  }

  override fun handleRetryURL(bookingForm: BookingForm, uri: Uri): Observable<BookingForm> {
    return bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form)).toObservable()
  }
}