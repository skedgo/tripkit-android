package com.skedgo.android.bookingkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.bookingkit.api.BookingApi;
import com.skedgo.android.bookingkit.api.QuickBookingApi;
import com.skedgo.android.bookingkit.model.FormField;
import com.skedgo.android.bookingkit.model.FormFieldJsonAdapter;
import com.skedgo.android.bookingkit.model.GsonAdaptersQuickBooking;
import com.skedgo.android.bookingkit.viewmodel.AuthenticationViewModel;
import com.skedgo.android.bookingkit.viewmodel.AuthenticationViewModelImpl;
import com.skedgo.android.bookingkit.viewmodel.BookingViewModel;
import com.skedgo.android.bookingkit.viewmodel.BookingViewModelImpl;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class BookingKitModule {
  @Provides BookingApi bookingApi(OkHttpClient httpClient) {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapter(FormField.class, new FormFieldJsonAdapter())
        .create();
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
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
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(QuickBookingApi.class);
  }

  @Provides BookingViewModel bookingViewModel(BookingApi bookingApi) {
    return new BookingViewModelImpl(bookingApi);
  }

  @Provides AuthenticationViewModel authenticationViewModel() {
    return new AuthenticationViewModelImpl();
  }
}