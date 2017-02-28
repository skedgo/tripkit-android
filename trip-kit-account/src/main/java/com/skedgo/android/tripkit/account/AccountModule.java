package com.skedgo.android.tripkit.account;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class AccountModule {
  @Provides AccountApi accountApi(OkHttpClient httpClient) {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersSignUpBody())
        .registerTypeAdapterFactory(new GsonAdaptersSignUpResponse())
        .registerTypeAdapterFactory(new GsonAdaptersLogInBody())
        .registerTypeAdapterFactory(new GsonAdaptersLogInResponse())
        .registerTypeAdapterFactory(new GsonAdaptersLogOutResponse())
        .create();
    return new Retrofit.Builder()
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(AccountApi.class);
  }

  @Provides UserTokenRepository userTokenStore(Context context) {
    return new UserTokenRepositoryImpl(accountPreferences(context));
  }

  @Provides AccountService accountService(
      AccountApi accountApi,
      UserTokenRepository userTokenRepository,
      Context context) {
    return new AccountService(
        accountApi,
        userTokenRepository,
        accountPreferences(context)
    );
  }

  private SharedPreferences accountPreferences(Context context) {
    return context.getSharedPreferences(
        "AccountPreferences",
        Context.MODE_PRIVATE
    );
  }
}
