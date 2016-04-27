package com.skedgo.android.bookingkit.api;

import com.skedgo.android.bookingkit.model.AuthProvider;
import com.skedgo.android.bookingkit.model.BookingForm;
import com.skedgo.android.bookingkit.model.LogOutResponse;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface AuthApi {
  /**
   * @param url Should be in form of 'auth/{regionName}'.
   */
  @GET Observable<List<AuthProvider>> fetchProvidersAsync(@Url HttpUrl url);

  /**
   * @param url This might be obtained by {@link AuthProvider#url()}.
   */
  @GET Observable<BookingForm> signInAsync(@Url String url);

  /**
   * @param url This might be obtained by {@link AuthProvider#url()}.
   */
  @GET Observable<LogOutResponse> logOutAsync(@Url String url);
}