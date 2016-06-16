package com.skedgo.android.tripkit.booking;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ExternalOAuthApi {
  @FormUrlEncoded
  @POST("token") Observable<AccessToken> getAccessToken(
      @Field("code") String code,
      @Field("grant_type") String grantType,
      @Field("scope") String scope);
}
