package com.skedgo.tripkit.booking;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.tripkit.booking.viewmodel.AuthenticationViewModel;
import com.skedgo.tripkit.booking.viewmodel.AuthenticationViewModelImpl;
import com.skedgo.tripkit.booking.viewmodel.BookingViewModel;
import com.skedgo.tripkit.booking.viewmodel.BookingViewModelImpl;
import com.skedgo.tripkit.configuration.ServerManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.schedulers.Schedulers;

@Module
public class BookingModule {
  @Provides BookingApi bookingApi(OkHttpClient httpClient) {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapter(FormField.class, new FormFieldJsonAdapter())
        .create();
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(BookingApi.class);
  }

  @Provides QuickBookingApi quickBookingApi(OkHttpClient httpClient) {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersQuickBooking())
        .create();
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(QuickBookingApi.class);
  }

  @Provides AuthApi authApi(OkHttpClient httpClient) {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapter(FormField.class, new FormFieldJsonAdapter())
        .registerTypeAdapterFactory(new GsonAdaptersAuthProvider())
        .registerTypeAdapterFactory(new GsonAdaptersLogOutResponse())
        .create();
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(AuthApi.class);
  }

  @Provides AuthService authService(AuthApi authApi) {
    return new AuthServiceImpl(authApi);
  }

  @Provides BookingViewModel bookingViewModel(BookingService bookingService) {
    return new BookingViewModelImpl(bookingService);
  }

  @Provides AuthenticationViewModel authenticationViewModel() {
    return new AuthenticationViewModelImpl();
  }

  @Provides ExternalOAuthServiceGenerator provideExternalOAuthServiceGenerator() {
    return new ExternalOAuthServiceGenerator(new OkHttpClient.Builder());
  }

  @Provides
  ExternalOAuthService getExternalOAuthService(ExternalOAuthServiceGenerator externalOAuthServiceGenerator) {
    return new ExternalOAuthServiceImpl(externalOAuthServiceGenerator);
  }

  @Provides
  BookingService getBookingService(BookingApi bookingApi) {
    return new BookingServiceImpl(bookingApi, new Gson());
  }

  @Provides QuickBookingService getQuickBookingService(QuickBookingApi quickBookingApi) {
    return new QuickBookingServiceImpl(quickBookingApi);
  }
}
