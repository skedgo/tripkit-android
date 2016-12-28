package com.skedgo.android.tripkit.booking;

import android.text.TextUtils;

import rx.Observable;
import rx.functions.Func1;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {
  private final ExternalOAuthStore externalOAuthStore;
  private final ExternalOAuthServiceGenerator externalOAuthServiceGenerator;

  public ExternalOAuthServiceImpl(ExternalOAuthStore externalOAuthStore,
                                  ExternalOAuthServiceGenerator externalOAuthServiceGenerator) {
    this.externalOAuthStore = externalOAuthStore;
    this.externalOAuthServiceGenerator = externalOAuthServiceGenerator;
  }

  @Override public Observable<ExternalOAuth> getAccessToken(
      final BookingForm form,
      final String code,
      final String grantType,
      final String callback
  ) {
    final String clientId = form.getClientID();
    final String clientSecret = form.getClientSecret();
    final String baseUrl = form.getTokenURL();
    final String scope = TextUtils.isEmpty(form.getScope()) ? null : form.getScope();
    final Object serviceId = form.getValue();

    // TODO: how to avoid this constant?
    boolean credentialsInHeader = serviceId == null || !("uber".equals(serviceId.toString()));

    final ExternalOAuthApi externalOAuthApi =
        externalOAuthServiceGenerator.createService(baseUrl, clientId,
                                                    clientSecret, credentialsInHeader
        );

    return externalOAuthApi.getAccessToken(clientSecret, clientId, code, "authorization_code", callback, scope)
        .filter(new Func1<AccessTokenResponse, Boolean>() {
          @Override public Boolean call(AccessTokenResponse response) {
            return response != null && response.refreshToken() != null;
          }
        })
        .map(new Func1<AccessTokenResponse, ExternalOAuth>() {
          @Override public ExternalOAuth call(AccessTokenResponse response) {
            String serviceIdString = "";
            if (serviceId != null) {
              serviceIdString = serviceId.toString();
            }
            return ImmutableExternalOAuth.builder()
                .authServiceId(serviceIdString)
                .token(response.accessToken())
                .refreshToken(response.refreshToken())
                .expiresIn(response.expiresIn())
                .build();
          }
        })
        .flatMap(new Func1<ExternalOAuth, Observable<ExternalOAuth>>() {
          @Override public Observable<ExternalOAuth> call(ExternalOAuth externalOAuth) {
            return externalOAuthStore.updateExternalOauth(externalOAuth);
          }
        });
  }
}