package com.skedgo.android.tripkit.booking.ui;

import android.net.Uri;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuth;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.skedgo.android.tripkit.booking.InputForm;

import rx.Observable;
import rx.functions.Func1;

public class OAuth2CallbackHandlerImpl implements OAuth2CallbackHandler {
  private final ExternalOAuthService externalOAuthService;
  private final BookingService bookingService;

  public OAuth2CallbackHandlerImpl(ExternalOAuthService externalOAuthService, BookingService bookingService) {
    this.externalOAuthService = externalOAuthService;
    this.bookingService = bookingService;
  }

  public Observable<BookingForm> handleOAuthURL(final BookingForm form, Uri uri, String callback) {
    if (form != null) {
      // save code or show error
      String code = uri.getQueryParameter("code");
      if (code != null) {
        // get access token

        return externalOAuthService.getAccessToken(form, code, "authorization_code", callback)
            .flatMap(new Func1<ExternalOAuth, Observable<BookingForm>>() {
              @Override public Observable<BookingForm> call(ExternalOAuth externalOAuth) {
                return Observable.just(form.setAuthData(externalOAuth));
              }
            })
            .flatMap(new Func1<BookingForm, Observable<BookingForm>>() {
              @Override public Observable<BookingForm> call(BookingForm bookingForm) {
                return bookingService.postFormAsync(bookingForm.getAction().getUrl(), InputForm.from(bookingForm.getForm()));
              }
            });

      } else if (uri.getQueryParameter("error") != null) {
        return Observable.error(new Error(uri.getQueryParameter("error")));
      }
    }
    return Observable.error(new Error("No saved booking!"));
  }

  @Override public Observable<BookingForm> handleRetryURL(final BookingForm bookingForm, Uri uri) {
    return bookingService.postFormAsync(bookingForm.getAction().getUrl(), InputForm.from(bookingForm.getForm()));
  }
}
