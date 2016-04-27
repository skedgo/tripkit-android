package com.skedgo.android.bookingkit.api;

import com.skedgo.android.bookingkit.model.AuthProvider;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface AuthApi {
  @GET Observable<List<AuthProvider>> fetchProvidersAsync(
      @Url HttpUrl url
  );
}