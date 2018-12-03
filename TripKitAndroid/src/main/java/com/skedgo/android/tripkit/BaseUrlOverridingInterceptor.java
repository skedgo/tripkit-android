package com.skedgo.android.tripkit;

import androidx.annotation.NonNull;
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
    boolean isFromSkedGo = pathSegments.get(0).equals("satapp") || requestUrl.host().contains("skedgo.com") || requestUrl.host().contains("tripgo.com");
    if (!TextUtils.isEmpty(newBaseUrl) && isFromSkedGo && !requestUrl.host().contains("payments.tripgo.com")) {
      final HttpUrl tempUrl = requestUrl.newBuilder().removePathSegment(0).build();
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