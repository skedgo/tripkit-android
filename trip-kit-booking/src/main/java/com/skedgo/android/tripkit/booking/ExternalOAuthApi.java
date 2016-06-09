package com.skedgo.android.tripkit.booking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ExternalOAuthApi {
  @FormUrlEncoded
  @POST("token") Call<AccessToken> getAccessToken(
      @Field("code") String code,
      @Field("grant_type") String grantType,
      @Field("approval_prompt") String approval_prompt,
      @Field("access_type") String access_type);
}
