package com.skedgo.android.tripkit.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skedgo.android.common.model.Region;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.http.Url;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

final class AuthServiceImpl implements AuthService {
  private final AuthApi api;

  AuthServiceImpl(@NonNull AuthApi api) {
    this.api = api;
  }

  @Override
  public Observable<List<AuthProvider>> fetchProvidersByRegionAsync(@NonNull final Region region,
                                                                    @Nullable final String mode,
                                                                    final boolean bsb) {
    return Observable.fromIterable(region.getURLs())
        .concatMapDelayError(new Function<String, Observable<? extends List<AuthProvider>>>() {
          @Override public Observable<? extends List<AuthProvider>> apply(String url) {

            HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder()
                .addPathSegment("auth")
                .addPathSegment(region.getName());

            if (mode != null) {
              builder.addQueryParameter("mode", mode);
            }

            if (bsb){
              builder.addQueryParameter("bsb", "1");
            }

            return fetchProvidersAsync(builder.build());
          }
        })
        .firstElement().toObservable();
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