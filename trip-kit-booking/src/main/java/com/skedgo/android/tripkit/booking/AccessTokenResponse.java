package com.skedgo.android.tripkit.booking;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
public interface AccessTokenResponse {

  @SerializedName("access_token") String accessToken();
  @SerializedName("token_type") String tokenType();
  @SerializedName("expires_in") int expiresIn();
  @Nullable @SerializedName("refresh_token") String refreshToken();

}
