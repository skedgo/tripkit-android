package com.skedgo.android.tripkit.booking;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {

  private ExternalOAuthStore externalOAuthStore;

  public ExternalOAuthServiceImpl(ExternalOAuthStore externalOAuthStore) {
    this.externalOAuthStore = externalOAuthStore;
  }

  @Override public Observable<ExternalOAuth> getAccessToken(final BookingForm form,
                                                            final String code, String grantType) {

    String clientId = form.getClientID();
    String clientSecret = form.getClientSecret();
    String baseUrl = form.getTokenURL();

    final ExternalOAuthApi externalOAuthApi = ExternalOAuthServiceGenerator.createService(ExternalOAuthApi.class,
                                                                                          baseUrl, clientId, clientSecret);

    return Observable.create(new Observable.OnSubscribe<ExternalOAuth>() {
      @Override public void call(Subscriber<? super ExternalOAuth> subscriber) {
        final Call<AccessToken> call = externalOAuthApi.getAccessToken(code, "authorization_code", "force", "offline");

        try {
          Response<AccessToken> resp = call.execute();
          AccessToken accessToken = resp.body();

          if (accessToken != null) {

            subscriber.onNext(ImmutableExternalOAuth.builder()
                                  .authServiceId(form.getValue().toString())
                                  .token(accessToken.getAccessToken())
                                  .expiresIn(accessToken.getExpiresIn())
                                  .build());

            if (accessToken.getRefreshToken() != null) {
              // save token
              externalOAuthStore.updateExternalOauth(ImmutableExternalOAuth.builder()
                                                         .authServiceId(form.getValue().toString())
                                                         .token(accessToken.getAccessToken())
                                                         .expiresIn(accessToken.getExpiresIn())
                                                         .build()).subscribe();
            }

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
