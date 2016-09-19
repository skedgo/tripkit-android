package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Func0;

final class BaseUrlOverridingInterceptor implements Interceptor {
  private final Func0<String> baseUrlAdapter;

  BaseUrlOverridingInterceptor(@NonNull Func0<String> baseUrlAdapter) {
    this.baseUrlAdapter = baseUrlAdapter;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    final String newBaseUrl = baseUrlAdapter.call();
    final Request request = chain.request();
    final HttpUrl requestUrl = request.url();
    final List<String> pathSegments = requestUrl.pathSegments();

    if (!TextUtils.isEmpty(newBaseUrl) &&
        (pathSegments.get(0).equals("satapp") || request.url().toString().contains("account"))) {

      HttpUrl tempUrl = null;
      if (pathSegments.get(0).equals("satapp")) {
        tempUrl = requestUrl.newBuilder().removePathSegment(0).build();
      } else
      // TODO: Mariano needs to update the server, this should not be necessary
      if (request.url().toString().contains("account")) {
        tempUrl = requestUrl;
      }

      final String query = tempUrl.query();
      final String encodedPath = TextUtils.join("/", tempUrl.encodedPathSegments());
      final HttpUrl newUrl = HttpUrl.parse(newBaseUrl)
          .newBuilder()
          .addEncodedPathSegments(encodedPath)
          .query(query)
          .build();
      final Request newRequest = request.newBuilder().url(newUrl).build();
      return chain.proceed(newRequest);
    } else {
      return chain.proceed(request);
    }
  }
}