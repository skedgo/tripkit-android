package com.skedgo.tripkit.booking

import io.reactivex.Observable

class ExternalOAuthServiceImpl(
    private val externalOAuthServiceGenerator: ExternalOAuthServiceGenerator
) : ExternalOAuthService {

    override fun getAccessToken(
        form: BookingForm,
        code: String,
        grantType: String,
        callback: String
    ): Observable<ExternalOAuth> {
        val clientId = form.getClientID()
        val clientSecret = form.getClientSecret()
        val baseUrl = form.getTokenURL()
        val scope = if (form.getScope().isNullOrEmpty()) null else form.getScope()
        val serviceId = form.mValue

        // TODO: how to avoid this constant?
        val credentialsInHeader = serviceId == null || serviceId.toString() != "uber"

        val externalOAuthApi = externalOAuthServiceGenerator.createService(
            baseUrl.orEmpty(),
            clientId,
            clientSecret,
            credentialsInHeader
        )

        return externalOAuthApi.getAccessToken(
            clientSecret,
            clientId,
            code,
            "authorization_code",
            callback,
            scope
        )
            .filter { response -> response != null && response.refreshToken() != null }
            .map { response ->
                val serviceIdString = serviceId?.toString() ?: ""
                ImmutableExternalOAuth.builder()
                    .authServiceId(serviceIdString)
                    .token(response.accessToken())
                    .refreshToken(response.refreshToken())
                    .expiresIn(response.expiresIn())
                    .build()
            }
    }
}