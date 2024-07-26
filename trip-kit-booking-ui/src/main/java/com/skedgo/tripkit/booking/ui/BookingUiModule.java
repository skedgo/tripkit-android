package com.skedgo.tripkit.booking.ui;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.skedgo.TripKit;
import com.skedgo.tripkit.booking.BookingService;
import com.skedgo.tripkit.booking.ExternalOAuthService;
import com.skedgo.tripkit.common.util.Gsons;
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

    @Provides
    Context context() {
        return appContext;
    }

    @Provides
    Resources resources() {
        return appContext.getResources();
    }

    @Provides
    OkHttpClient httpClient() {
        return TripKit.getInstance().getOkHttpClient3();
    }

    @Provides
    Gson gson() {
        return Gsons.createForLowercaseEnum();
    }

    @Provides
    @Singleton
    Picasso picasso(
        Context context,
        OkHttpClient httpClient) {
        return new Picasso.Builder(context)
            .downloader(new OkHttp3Downloader(httpClient))
            .build();
    }

    @Provides
    OAuth2CallbackHandler oAuth2CallbackHandler(
        ExternalOAuthService externalOAuthService,
        BookingService bookingService) {
        return new OAuth2CallbackHandlerImpl(externalOAuthService, bookingService);
    }
}
