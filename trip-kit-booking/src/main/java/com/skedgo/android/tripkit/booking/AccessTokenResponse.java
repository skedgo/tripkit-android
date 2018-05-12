package com.skedgo.android.tripkit.booking;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(AccessTokenResponse.class)
public interface AccessTokenResponse {

  @SerializedName("access_token") String accessToken();
  @SerializedName("token_type") String tokenType();
  @SerializedName("expires_in") long expiresIn();
  @Nullable @SerializedName("refresh_token") String refreshToken();

}
