package com.skedgo.tripkit.booking;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.schedulers.Schedulers;

public class ExternalOAuthServiceGenerator {
  private final OkHttpClient.Builder builder;

  public ExternalOAuthServiceGenerator(OkHttpClient.Builder builder) {
    this.builder = builder;
  }

  public ExternalOAuthApi createService(String baseUrl, String username, String password,
                                        final boolean credentialsInHeader) {
    if (username != null && password != null) {
      String credentials = username + ":" + password;
      final String basic =
          "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

      builder.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
          Request original = chain.request();

          Request.Builder requestBuilder = original.newBuilder();

          if (credentialsInHeader) {
            requestBuilder = requestBuilder.header("authorization", basic)
                .header("accept-encoding", "gzip");
          } else {
            requestBuilder = requestBuilder.header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json");
          }

          requestBuilder.method(original.method(), original.body());

          Request request = requestBuilder.build();
          return chain.proceed(request);
        }

      });
    }
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersAccessTokenResponse())
        .create();

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(baseUrl + "/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

    OkHttpClient client = this.builder.build();
    Retrofit retrofit = builder.client(client).build();
    return retrofit.create(ExternalOAuthApi.class);
  }
}
