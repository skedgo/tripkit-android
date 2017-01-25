package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import rx.functions.Func0;

/**
 * Provides capability of {@link BaseUrlOverridingInterceptor} for {@link OkHttpClient} (pre v3).
 */
final class BaseUrlOverridingInterceptorCompat implements Interceptor {
  private final Func0<String> baseUrlAdapter;

  BaseUrlOverridingInterceptorCompat(@NonNull Func0<String> baseUrlAdapter) {
    this.baseUrlAdapter = baseUrlAdapter;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    final String newBaseUrl = baseUrlAdapter.call();
    final Request request = chain.request();
    final HttpUrl requestUrl = request.httpUrl();
    final List<String> pathSegments = requestUrl.pathSegments();
    if (!TextUtils.isEmpty(newBaseUrl) && pathSegments.get(0).equals("satapp")) {
      final HttpUrl tempUrl = requestUrl.newBuilder().removePathSegment(0).build();
      final String query = tempUrl.query();
      final HttpUrl.Builder newBuilder = HttpUrl.parse(newBaseUrl).newBuilder();
      for (String pathSegment : tempUrl.pathSegments()) {
        newBuilder.addPathSegment(pathSegment);
      }

      final HttpUrl newUrl = newBuilder.query(query).build();
      final Request newRequest = request.newBuilder().url(newUrl).build();
      return chain.proceed(newRequest);
    } else {
      return chain.proceed(request);
    }
  }
}