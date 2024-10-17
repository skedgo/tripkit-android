package com.skedgo.tripkit.booking

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ExternalOAuthApi {
    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(
        @Field("client_secret") client_secret: String?,
        @Field("client_id") client_id: String?,
        @Field("code") code: String?,
        @Field("grant_type") grantType: String?,
        @Field("redirect_uri") redirectUri: String?,
        @Field("scope") scope: String?
    ): Observable<AccessTokenResponse>
}
