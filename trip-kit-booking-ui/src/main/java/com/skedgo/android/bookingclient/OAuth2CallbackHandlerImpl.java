package com.skedgo.android.bookingclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.bookingclient.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ExternalOAuth;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormFieldJsonAdapter;

import java.io.StringReader;

import rx.Observable;
import rx.functions.Func1;

public class OAuth2CallbackHandlerImpl implements OAuth2CallbackHandler {

  private final ExternalOAuthService externalOAuthService;

  public OAuth2CallbackHandlerImpl(ExternalOAuthService externalOAuthService) {

    this.externalOAuthService = externalOAuthService;
  }

  public Observable<BookingForm> handleURL(Activity activity, Uri uri) {

    // get temp booking form
    SharedPreferences prefs = activity.getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);

    String jsonBooking = prefs.getString(BookingActivity.KEY_TEMP_BOOKING_FORM, "");
    JsonReader reader = new JsonReader(new StringReader(jsonBooking));
    reader.setLenient(true);

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
    Gson gson = builder.create();

    final BookingForm form = gson.fromJson(reader, BookingForm.class);

    if (form != null) {

      SharedPreferences.Editor editor = prefs.edit();
      editor.remove(BookingActivity.KEY_TEMP_BOOKING_FORM).apply();

      // save code or show error
      String code = uri.getQueryParameter("code");
      if (code != null) {
        // get access token

        return externalOAuthService.getAccessToken(form, code, "authorization_code")
            .flatMap(new Func1<ExternalOAuth, Observable<BookingForm>>() {
              @Override public Observable<BookingForm> call(ExternalOAuth externalOAuth) {
                return Observable.just(form.setAuthData(externalOAuth));
              }
            });

      } else if (uri.getQueryParameter("error") != null) {
        return Observable.error(new Error(uri.getQueryParameter("error")));
      }
    }
    return Observable.error(new Error("No saved booking!"));
  }
}
