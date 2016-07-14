package com.skedgo.android.tripkit.booking;

import android.text.TextUtils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {

  private ExternalOAuthStore externalOAuthStore;

  public ExternalOAuthServiceImpl(ExternalOAuthStore externalOAuthStore) {
    this.externalOAuthStore = externalOAuthStore;
  }

  @Override public Observable<ExternalOAuth> getAccessToken(final BookingForm form,
                                                            final String code, final String grantType,
                                                            final String callback) {

    final String clientId = form.getClientID();
    final String clientSecret = form.getClientSecret();
    final String baseUrl = form.getTokenURL();
    final String scope = TextUtils.isEmpty(form.getScope()) ? null : form.getScope();
    final Object serviceId = form.getValue();

    // TODO: how to avoid this constant?
    boolean credentialsInHeader = serviceId == null || !("uber".equals(serviceId.toString()));

    final ExternalOAuthApi externalOAuthApi =
        ExternalOAuthServiceGenerator.createService(ExternalOAuthApi.class, baseUrl, clientId,
                                                    clientSecret, credentialsInHeader);

    return externalOAuthApi.getAccessToken(clientSecret, clientId, code, "authorization_code", callback, scope)
        .filter(new Func1<AccessTokenResponse, Boolean>() {
          @Override public Boolean call(AccessTokenResponse accessTokenResponse) {
            return accessTokenResponse != null;
          }
        })
        .flatMap(new Func1<AccessTokenResponse, Observable<ExternalOAuth>>() {
          @Override public Observable<ExternalOAuth> call(final AccessTokenResponse accessTokenResponse) {
            return Observable.create(new Observable.OnSubscribe<ExternalOAuth>() {
              @Override public void call(Subscriber<? super ExternalOAuth> subscriber) {

                String serviceIdString = "";
                if (serviceId != null) {
                  serviceIdString = serviceId.toString();
                }
                ExternalOAuth externalOAuth = ImmutableExternalOAuth.builder()
                    .authServiceId(serviceIdString)
                    .token(accessTokenResponse.accessToken())
                    .refreshToken(accessTokenResponse.refreshToken())
                    .expiresIn(accessTokenResponse.expiresIn())
                    .build();

                if (accessTokenResponse.refreshToken() != null) {
                  // save token
                  externalOAuthStore.updateExternalOauth(externalOAuth).subscribe();
                }
                subscriber.onNext(externalOAuth);
              }
            });
          }
        });
  }
}
