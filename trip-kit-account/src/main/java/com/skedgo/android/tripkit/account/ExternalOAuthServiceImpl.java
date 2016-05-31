package com.skedgo.android.tripkit.account;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {

  @Override public Observable<AccessToken> getAccessToken(String baseUrl, String clientId, String clientSecret,
                                                          final String code, String grantType) {

    final ExternalOAuthApi externalOAuthApi = ExternalOAuthServiceGenerator.createService(ExternalOAuthApi.class,
                                                                                          baseUrl, clientId, clientSecret);

    return Observable.create(new Observable.OnSubscribe<AccessToken>() {
      @Override public void call(Subscriber<? super AccessToken> subscriber) {
        final Call<AccessToken> call = externalOAuthApi.getAccessToken(code, "authorization_code");

        try {
          Response<AccessToken> resp = call.execute();
          AccessToken accessToken = resp.body();

          if (accessToken != null) {
            subscriber.onNext(accessToken);
          } else {
            subscriber.onError(new Error(resp.errorBody().string()));
          }

          subscriber.onCompleted();

        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    });
  }

}
