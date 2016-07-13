package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Region;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

final class AuthServiceImpl implements AuthService {
  private final AuthApi api;

  AuthServiceImpl(@NonNull AuthApi api) {
    this.api = api;
  }

  @Override public Observable<List<AuthProvider>> fetchProvidersByRegionAsync(@NonNull final Region region) {
    return Observable.from(region.getURLs())
        .concatMapDelayError(new Func1<String, Observable<? extends List<AuthProvider>>>() {
          @Override public Observable<? extends List<AuthProvider>> call(String url) {
            return fetchProvidersAsync(
                HttpUrl.parse(url).newBuilder()
                    .addPathSegment("auth")
                    .addPathSegment(region.getName())
                    .build()
            );
          }
        })
        .first();
  }

  @Override public Observable<List<AuthProvider>> fetchProvidersAsync(@Url HttpUrl url) {
    return api.fetchProvidersAsync(url);
  }

  @Override public Observable<BookingForm> signInAsync(@Url String url) {
    return api.signInAsync(url);
  }

  @Override public Observable<LogOutResponse> logOutAsync(@Url String url) {
    return api.logOutAsync(url);
  }
}