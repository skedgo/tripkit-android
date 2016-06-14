package com.skedgo.android.tripkit.booking;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {

  private ExternalOAuthStore externalOAuthStore;

  public ExternalOAuthServiceImpl(ExternalOAuthStore externalOAuthStore) {
    this.externalOAuthStore = externalOAuthStore;
  }

  @Override public Observable<ExternalOAuth> getAccessToken(final BookingForm form,
                                                            final String code, String grantType) {

    final String clientId = form.getClientID();
    final String clientSecret = form.getClientSecret();
    final String baseUrl = form.getTokenURL();

    final ExternalOAuthApi externalOAuthApi = ExternalOAuthServiceGenerator.createService(ExternalOAuthApi.class,
                                                                                          baseUrl, clientId, clientSecret);

    return externalOAuthApi.getAccessToken(code, "authorization_code", "force", "offline")
        .filter(new Func1<AccessToken, Boolean>() {
          @Override public Boolean call(AccessToken accessToken) {
            return accessToken != null;
          }
        })
        .flatMap(new Func1<AccessToken, Observable<ExternalOAuth>>() {
          @Override public Observable<ExternalOAuth> call(final AccessToken accessToken) {
            return Observable.create(new Observable.OnSubscribe<ExternalOAuth>() {
              @Override public void call(Subscriber<? super ExternalOAuth> subscriber) {
                ExternalOAuth externalOAuth = ImmutableExternalOAuth.builder()
                    .authServiceId(form.getValue().toString())
                    .token(accessToken.getAccessToken())
                    .expiresIn(accessToken.getExpiresIn())
                    .build();

                if (accessToken.getRefreshToken() != null) {
                  // save token
                  externalOAuthStore.updateExternalOauth(externalOAuth);
                }
                subscriber.onNext(externalOAuth);
              }
            });
          }
        });
  }
}
