package com.skedgo.android.tripkit.booking.ui;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.skedgo.android.common.util.Gsons;
import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class BookingUiModule {
  private final Context appContext;

  public BookingUiModule(Context context) {
    this.appContext = context;
  }

  @Provides Context context() {
    return appContext;
  }

  @Provides Resources resources() {
    return appContext.getResources();
  }

  @Provides OkHttpClient httpClient() {
    return TripKit.singleton().getOkHttpClient3();
  }

  @Provides Gson gson() {
    return Gsons.createForLowercaseEnum();
  }

  @Provides @Singleton
  Picasso picasso(
      Context context,
      OkHttpClient httpClient) {
    return new Picasso.Builder(context)
        .downloader(new OkHttp3Downloader(httpClient))
        .build();
  }

  @Provides OAuth2CallbackHandler oAuth2CallbackHandler(
      ExternalOAuthService externalOAuthService,
      BookingService bookingService) {
    return new OAuth2CallbackHandlerImpl(externalOAuthService, bookingService);
  }
}
