package com.skedgo.android.bookingclient.module;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.skedgo.android.bookingclient.BuildConfig;
import com.skedgo.android.bookingclient.OAuth2CallbackHandler;
import com.skedgo.android.bookingclient.OAuth2CallbackHandlerImpl;
import com.skedgo.android.bookingclient.R;
import com.skedgo.android.bookingclient.viewmodel.BookingErrorViewModel;
import com.skedgo.android.common.util.Gsons;
import com.skedgo.android.common.util.MainThreadBus;
import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import rx.functions.Action1;

@Module
public class BookingClientModule {

  private static final String TAG = "bookingclient";

  private final Context appContext;

  public BookingClientModule(Context context) {
    this.appContext = context;
  }

  @Provides Context provideContext() { return appContext;}

  @Provides OkHttpClient provideHttpClient() {
    return TripKit.singleton().getOkHttpClient3();
  }

  @Provides Gson provideGson() {
    return Gsons.createForLowercaseEnum();
  }

  @Provides @Singleton Bus provideBus() {
    return new MainThreadBus(new Action1<Throwable>() {
      @Override public void call(Throwable error) {
        if (BuildConfig.DEBUG) {
          Log.e(TAG, null, error);
        }
      }
    });
  }

  @Provides BookingErrorViewModel bookingErrorViewModel(Gson gson) {
    return new BookingErrorViewModel(appContext.getString(R.string.nicely_informed_error));
  }

  @Provides @Singleton Picasso picasso(
      Context context,
      okhttp3.OkHttpClient httpClient) {
    return new Picasso.Builder(context)
        .downloader(new OkHttp3Downloader(httpClient))
        .build();
  }

  @Provides OAuth2CallbackHandler getOAuth2CallbackHandler(ExternalOAuthService externalOAuthService, BookingService bookingService) {
    return new OAuth2CallbackHandlerImpl(externalOAuthService, bookingService);
  }
}