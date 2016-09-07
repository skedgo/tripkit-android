package com.skedgo.android.bookingclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.bookingclient.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuth;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormFieldJsonAdapter;
import com.skedgo.android.tripkit.booking.InputForm;

import java.io.StringReader;

import rx.Observable;
import rx.functions.Func1;

public class OAuth2CallbackHandlerImpl implements OAuth2CallbackHandler {

  private final ExternalOAuthService externalOAuthService;
  private final BookingService bookingService;

  public OAuth2CallbackHandlerImpl(ExternalOAuthService externalOAuthService, BookingService bookingService) {
    this.externalOAuthService = externalOAuthService;
    this.bookingService = bookingService;
  }

  private BookingForm getSavedForm(Activity activity) {
    // get temp booking form
    SharedPreferences prefs = activity.getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);

    String jsonBooking = prefs.getString(BookingActivity.KEY_TEMP_BOOKING_FORM, "");
    JsonReader reader = new JsonReader(new StringReader(jsonBooking));
    reader.setLenient(true);

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
    Gson gson = builder.create();

    return gson.fromJson(reader, BookingForm.class);

  }

  public Observable<BookingForm> handleOAuthURL(Activity activity, Uri uri, String callback) {

    final BookingForm form = getSavedForm(activity);

    if (form != null) {

      SharedPreferences prefs = activity.getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);
      SharedPreferences.Editor editor = prefs.edit();
      editor.remove(BookingActivity.KEY_TEMP_BOOKING_FORM).apply();

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

  @Override public Observable<BookingForm> handleRetryURL(Activity activity, Uri uri) {
    BookingForm bookingForm = getSavedForm(activity);
    return bookingService.postFormAsync(bookingForm.getAction().getUrl(), InputForm.from(bookingForm.getForm()));
  }
}
