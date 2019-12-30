package com.skedgo.tripkit.booking;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import io.reactivex.Observable;

public interface ExternalOAuthApi {
  @FormUrlEncoded
  @POST("token") Observable<AccessTokenResponse> getAccessToken(
      @Field("client_secret") String client_secret,
      @Field("client_id") String client_id,
      @Field("code") String code,
      @Field("grant_type") String grantType,
      @Field("redirect_uri") String redirectUri,
      @Field("scope") String scope);
}
