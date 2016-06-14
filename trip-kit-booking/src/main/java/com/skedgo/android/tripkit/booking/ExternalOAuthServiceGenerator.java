package com.skedgo.android.tripkit.booking;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class ExternalOAuthServiceGenerator {

  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  public static <S> S createService(Class<S> serviceClass, String baseUrl, String username, String password) {
    if (username != null && password != null) {
      String credentials = username + ":" + password;
      final String basic =
          "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

      httpClient.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
          Request original = chain.request();

          Request.Builder requestBuilder = original.newBuilder()
              .header("Authorization", basic)
              .header("Accept", "application/json")
              .method(original.method(), original.body());

          Request request = requestBuilder.build();
          return chain.proceed(request);
        }

      });

      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      httpClient.interceptors().add(logging);
    }

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(baseUrl + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()));

    OkHttpClient client = httpClient.build();
    Retrofit retrofit = builder.client(client).build();
    return retrofit.create(serviceClass);
  }
}
