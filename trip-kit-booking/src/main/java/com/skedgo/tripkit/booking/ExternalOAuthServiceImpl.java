package com.skedgo.tripkit.booking;

import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class ExternalOAuthServiceImpl implements ExternalOAuthService {
    private final ExternalOAuthServiceGenerator externalOAuthServiceGenerator;

    public ExternalOAuthServiceImpl(ExternalOAuthServiceGenerator externalOAuthServiceGenerator) {
        this.externalOAuthServiceGenerator = externalOAuthServiceGenerator;
    }

    @Override
    public Observable<ExternalOAuth> getAccessToken(
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
            .filter(new Predicate<AccessTokenResponse>() {
                @Override
                public boolean test(AccessTokenResponse response) {
                    return response != null && response.refreshToken() != null;
                }
            })
            .map(new Function<AccessTokenResponse, ExternalOAuth>() {
                @Override
                public ExternalOAuth apply(AccessTokenResponse response) {
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
            });
    }
}